package com.fastbee.modbus.codec;

import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.common.core.protocol.modbus.ModbusCode;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.utils.gateway.CRC16Utils;
import com.fastbee.modbus.pak.TcpDtu;
import com.fastbee.modbus.model.ModbusRtu;
import com.fastbee.protocol.WModelManager;
import com.fastbee.protocol.base.model.ActiveModel;
import com.fastbee.protocol.util.ArrayMap;
import com.fastbee.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * modbus-rtu协议解码器
 * @author bill
 */
@Slf4j
@Component
@NoArgsConstructor
public class ModbusMessageDecoder {

    @Autowired
    private  WModelManager modelManager;
    private  ArrayMap<ActiveModel> headerSchemaMap;

    public ModbusMessageDecoder(String...basePackages) {
        this.modelManager = new WModelManager(basePackages);
        this.headerSchemaMap = this.modelManager.getActiveMap(ModbusRtu.class);
    }

    public ModbusRtu decode(DeviceData deviceData){
        try {
            return decode(deviceData,null);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    public ModbusRtu decode(DeviceData deviceData, ExplainUtils explain){
        this.build();
        ByteBuf in = deviceData.getBuf();
        int start = in.getUnsignedByte(0);
        int messageId = in.getUnsignedByte(1);
        int currentMessageId = messageId;
        if (!(start == TcpDtu.起始位 && (messageId == TcpDtu.注册报文 || messageId == TcpDtu.心跳包))){
            verify(in);
            currentMessageId = 0;
            /*是否启动modbus客户端模拟测试*/
            if (deviceData.isEnabledTest()){
                currentMessageId = 1;
            }
            if (deviceData.getCode() == ModbusCode.Write06 || messageId == ModbusCode.Write06.getCode()){
                currentMessageId = 2;
            }else if (deviceData.getCode() == ModbusCode.Read01 || messageId == ModbusCode.Read01.getCode() ||
                      deviceData.getCode() == ModbusCode.Read02 || messageId == ModbusCode.Read02.getCode()){
                currentMessageId = 4;
            }
        }
        messageId = currentMessageId;
        ActiveModel<ModbusRtu> activeModel = headerSchemaMap.get(messageId);
        ModbusRtu message = new ModbusRtu();
        message.setPayload(in);
        message.setVerified(false);
        activeModel.mergeFrom(in,message,explain);
        log.info("=>解析:[{}]",message);
        return message;
    }

    private void verify(ByteBuf in){
        ByteBuf copy = in.duplicate();
        byte[] source = new byte[in.writerIndex()];
        copy.readBytes(source);
        byte[] checkBytes = {source[source.length - 2],source[source.length -1]};
        byte[] sourceCheck = ArrayUtils.subarray(source,0,source.length -2);
        String crc = CRC16Utils.getCRC(sourceCheck);
        if (!crc.equalsIgnoreCase(ByteBufUtil.hexDump(checkBytes))){
            log.warn("=>CRC校验异常,报文={}",ByteBufUtil.hexDump(source));
            throw new ServiceException("CRC校验异常");
        }
    }

    private void build(){
        if (this.headerSchemaMap == null) {
                this.headerSchemaMap = this.modelManager.getActiveMap(ModbusRtu.class);
        }
    }


}
