package com.fastbee.flowDev.codec;

import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.utils.gateway.CRC16Utils;
import com.fastbee.common.utils.gateway.CRC8Utils;
import com.fastbee.flowDev.model.FlowDev;
import com.fastbee.protocol.WModelManager;
import com.fastbee.protocol.base.model.ActiveModel;
import com.fastbee.protocol.util.ArrayMap;
import com.fastbee.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gsb
 * @date 2023/5/17 16:37
 */
@Slf4j
@Component
@NoArgsConstructor
public class FlowDevDecoder {

    @Resource
    private WModelManager modelManager;
    private ArrayMap<ActiveModel> headerSchemaMap;

    public FlowDevDecoder(String...basePackages){
        this.modelManager = new WModelManager(basePackages);
        this.headerSchemaMap = this.modelManager.getActiveMap(FlowDev.class);
    }

    public FlowDev decode(DeviceData deviceData, ExplainUtils explain){
        this.build();
        ByteBuf in = deviceData.getBuf();
        verify(in);
        ActiveModel<FlowDev> activeModel = headerSchemaMap.get(0);
        FlowDev flowDev = new FlowDev();
        activeModel.mergeFrom(in,flowDev,explain);
        log.info("=>流量计数据解析:[{}]",flowDev);
        return flowDev;
    }

    /*CRC校验*/
    private void verify(ByteBuf in){
        ByteBuf copy = in.duplicate();
        byte[] source = new byte[in.writerIndex()];
        copy.readBytes(source);
        //取倒数第二位校验CRC8
        byte checkBytes = source[source.length -2];
        byte[] sourceCheck = ArrayUtils.subarray(source,3,source.length -2);
        byte crc = CRC8Utils.calcCrc8_E5(sourceCheck);
        if (crc != checkBytes){
            log.warn("=>CRC校验异常,报文={}",ByteBufUtil.hexDump(source));
            throw new ServiceException("CRC校验异常");
        }
    }

    private void build(){
        if (this.headerSchemaMap == null) {
            this.headerSchemaMap = this.modelManager.getActiveMap(FlowDev.class);
        }
    }
}
