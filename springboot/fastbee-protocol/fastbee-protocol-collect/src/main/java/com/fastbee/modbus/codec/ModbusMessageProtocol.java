package com.fastbee.modbus.codec;

import com.alibaba.fastjson2.JSONObject;
import com.fastbee.common.annotation.SysProtocol;
import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReport;
import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.common.core.mq.message.DeviceDownMessage;
import com.fastbee.common.core.mq.message.PropRead;
import com.fastbee.common.core.protocol.modbus.ModbusCode;
import com.fastbee.common.core.redis.RedisCache;
import com.fastbee.common.core.redis.RedisKeyBuilder;
import com.fastbee.common.core.thingsModel.ThingsModelSimpleItem;
import com.fastbee.common.core.thingsModel.ThingsModelValuesInput;
import com.fastbee.common.enums.FunctionReplyStatus;
import com.fastbee.common.enums.ModbusDataType;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.utils.CaculateUtils;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.common.utils.gateway.CRC16Utils;
import com.fastbee.common.utils.gateway.protocol.ByteUtils;
import com.fastbee.common.utils.json.JsonUtils;
import com.fastbee.iot.model.ThingsModelItem.EnumItem;
import com.fastbee.iot.model.ThingsModels.PropertyDto;
import com.fastbee.iot.service.IThingsModelService;
import com.fastbee.modbus.model.ModbusRtu;
import com.fastbee.protocol.base.protocol.IProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author bill
 */
@Slf4j
@Component
@SysProtocol(name = "ModbusRtu协议", protocolCode = FastBeeConstant.PROTOCOL.ModbusRtu, description = "系统内置ModbusRtu解析协议")
public class ModbusMessageProtocol implements IProtocol {


    @Resource
    private ModbusMessageDecoder messageDecoder;
    @Resource
    private ModbusMessageEncoder messageEncoder;
    @Resource
    private RedisCache redisCache;
    @Resource
    private IThingsModelService thingsModelService;

    @Override
    public DeviceReport decode(DeviceData deviceData, String clientId) {
        try {
            DeviceReport report = new DeviceReport();
            ModbusRtu message = messageDecoder.decode(deviceData);
            if (message.getmId() != 0) {
                report.setClientId(message.getMac());
                report.setMessageId(String.valueOf(message.getmId()));
                return report;
            }
            //如果返回06编码，说明是设备回复，更新对应寄存器的值，并发送通知前端
            if (message.getCode() == ModbusCode.Write06.getCode()) {
                report.setAddress(message.getAddress());
                report.setSlaveId(message.getSlaveId());
                report.setReplyMessage(message.getWriteData() + "");
                report.setClientId(deviceData.getSerialNumber());
                report.setSerialNumber(deviceData.getSerialNumber());
                report.setProductId(deviceData.getProductId());
                report.setIsReply(true);
                report.setProtocolCode(FastBeeConstant.PROTOCOL.ModbusRtu);
                report.setStatus(FunctionReplyStatus.SUCCESS);
                return report;
            }
            PropRead prop = this.getCacheProp(message.getHex(), deviceData.getSerialNumber());
            String identify = prop.getAddress() + "#" + prop.getSlaveId();
            PropertyDto thingModels = thingsModelService.getSingleThingModels(deviceData.getProductId(), identify);

            ThingsModelValuesInput modelValuesInput = new ThingsModelValuesInput();
            List<ThingsModelSimpleItem> values = new ArrayList<>();
            Integer quantity = thingModels.getQuantity();
            if (!Objects.isNull(quantity) && quantity < 2){
                for (int i = 0; i < prop.getCount(); i++) {
                    ThingsModelSimpleItem things = new ThingsModelSimpleItem();
                    things.setId(prop.getAddress() + i + "");
                    things.setTs(DateUtils.getNowDate());
                    things.setSlaveId(prop.getSlaveId());
                    switch (prop.getCode()) {
                        case Read01:
                        case Read02:
                            things.setValue(message.getBitData() + "");
                            break;
                        case Read03:
                        case Read04:
                            things.setValue(message.getData()[i] + "");
                            break;
                    }
                    values.add(things);
                }
            }else {

                String parseType = thingModels.getParseType();
                if (StringUtils.isNotEmpty(parseType) && ModbusDataType.BIT.getType().equals(parseType)){
                    String s;
                    if (quantity > 1) {
                         s = ByteUtils.hexTo8Bit(message.getBitData(),quantity);
                    }else {
                         s = message.getBitData() + "";
                    }
                    Map<String,Object> params = new HashMap<>();
                    for (int i = 0; i < s.length(); i++) {
                        String enumList = thingModels.getDatatype().getString("enumList");
                        List<EnumItem> enumItems = JsonUtils.parseArray(enumList, EnumItem.class);
                        EnumItem item = enumItems.get(i);
                        params.put(item.getValue(), s.charAt(i));
                    }
                    ThingsModelSimpleItem things = new ThingsModelSimpleItem();
                    things.setId(prop.getAddress() + "");
                    things.setTs(DateUtils.getNowDate());
                    things.setSlaveId(prop.getSlaveId());
                    things.setValue(JSONObject.toJSONString(params));
                    things.setBit(true);
                    values.add(things);
                }else {
                    ThingsModelSimpleItem things = new ThingsModelSimpleItem();
                    things.setId(prop.getAddress() + "");
                    things.setTs(DateUtils.getNowDate());
                    things.setSlaveId(prop.getSlaveId());
                    String value = this.parseValue(thingModels, message);
                    things.setValue(value);
                    values.add(things);
                }
            }
            modelValuesInput.setThingsModelValueRemarkItem(values);
            report.setSlaveId(prop.getSlaveId());
            report.setValuesInput(modelValuesInput);
            report.setClientId(clientId);
            return report;
        } catch (Exception e) {
            log.error("=>解码异常[{}]", e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public byte[] encode(DeviceData message, String clientId) {
        ModbusRtu rtu = new ModbusRtu();
        DeviceDownMessage downMessage = message.getDownMessage();
        if (message.getType() == 2) {
            rtu = (ModbusRtu) message.getBody();
        } else {
            switch (message.getCode()) {
                case Read01:
                case Read02:
                case Read03:
                case Read04:
                    PropRead prop = (PropRead) message.getBody();
                    read03(prop, rtu);
                    break;
                case Write05:
                case Write06:
                    write0506(downMessage, rtu);
                    break;
            }
        }
        ByteBuf out = messageEncoder.encode(rtu);
        byte[] data = new byte[out.writerIndex()];
        out.readBytes(data);
        ReferenceCountUtil.release(out);
        return CRC(data);
    }

    /**
     * read03指令
     */
    private void read03(PropRead prop, ModbusRtu rtu) {
        rtu.setSlaveId(prop.getSlaveId() == 0 ? 1 : prop.getSlaveId());
        rtu.setCount(prop.getCount());
        rtu.setAddress(prop.getAddress());
        rtu.setCode(prop.getCode().getCode());
    }

    /**
     * writ05/06指令配置
     */
    private void write0506(DeviceDownMessage message, ModbusRtu rtu) {
        JSONObject values = (JSONObject) message.getBody();
        String address = values.keySet().toArray()[0].toString();
        rtu.setAddress(Integer.parseInt(address));
        String s = values.get(address).toString();
        int value;
        if (s.contains("0x")) {
            value = Integer.parseInt(s.substring(2), 16);
        } else {
            value = Integer.parseInt(s);
        }
        rtu.setWriteData(value);
        rtu.setCode(message.getCode().getCode());
        rtu.setSlaveId(message.getSlaveId() == null ? 1 : message.getSlaveId());
    }

    public byte[] CRC(byte[] source) {
        byte[] result = new byte[source.length + 2];
        byte[] crc16Byte = CRC16Utils.getCrc16Byte(source);
        System.arraycopy(source, 0, result, 0, source.length);
        System.arraycopy(crc16Byte, 0, result, result.length - 2, 2);
        return result;
    }

    /**
     * 判断是否是Modbus报文，并对数据赋值.
     */
    private PropRead getCacheProp(String hex, String serialNumber) {
        //获取设备以及从机编号获取是否有缓存
        String slaveIdStr = hex.substring(0, 2);
        int slaveId = Integer.parseInt(slaveIdStr, 16);
        String cacheKey = RedisKeyBuilder.buildPropReadCacheKey(serialNumber.toUpperCase() + "_" + slaveId);
        PropRead prop = redisCache.getCacheObject(cacheKey);
        if (null != prop) {
            /*判断报文是否跟下发的指令匹配*/
            if (prop.getSlaveId() == slaveId) {
                //redisCache.deleteObject(cacheKey);
                return prop;
            }
        }
        return new PropRead();
    }

    private String parseValue(PropertyDto dto, ModbusRtu message) {
        String value = "";
        String parseType = dto.getParseType();
        for (short msg : message.getData()) {
            value = value + ByteUtils.intToHex(Integer.parseInt(String.valueOf(msg)));
        }
        Long val = Long.parseLong(value, 16);
        byte[] bytes = ByteUtils.hexToByte(value);
        if (StringUtils.isNotEmpty(parseType)) {
            ModbusDataType type = ModbusDataType.convert(parseType);
            switch (type) {
                case U_SHORT:
                    value = CaculateUtils.toUnSign16(val);
                    break;
                case SHORT:
                case LONG_ABCD:
                    value = val +"";
                    break;
                case LONG_CDAB:
                    value = CaculateUtils.toSign32_CDAB(val);
                    break;
                case U_LONG_ABCD:
                    value = CaculateUtils.toUnSign32_ABCD(val);
                    break;
                case U_LONG_CDAB:
                    value = CaculateUtils.toUnSign32_CDAB(val);
                    break;
                case FLOAT_ABCD:
                    value = CaculateUtils.toFloat32_ABCD(bytes) +"";
                    break;
                case FLOAT_CDAB:
                    value = CaculateUtils.toFloat32_CDAB(bytes)+"";
                    break;
            }
        }
        return value;
    }


}
