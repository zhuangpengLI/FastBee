package com.fastbee.pakModbus.codec;

import com.fastbee.common.annotation.SysProtocol;
import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReport;
import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.common.core.mq.message.DeviceDownMessage;
import com.fastbee.common.core.mq.message.PropRead;
import com.fastbee.common.core.thingsModel.ThingsModelSimpleItem;
import com.fastbee.common.core.thingsModel.ThingsModelValuesInput;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.common.utils.gateway.mq.TopicsUtils;
import com.fastbee.pakModbus.model.PakModbusRtu;
import com.fastbee.protocol.base.protocol.IProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 包装过的modbus-rtu协议
 *
 * @author gsb
 * @date 2022/11/15 11:16
 */
@Slf4j
@Component
@SysProtocol(name = "包装过的Modbus-rtu协议", protocolCode = FastBeeConstant.PROTOCOL.ModbusRtuPak, description = "系统内置包装后的modbus-rtu协议")
public class ModbusRtuPakProtocol implements IProtocol {

    @Autowired
    private ModbusRtuPakDecoder rtuPakDecoder;
    @Resource
    private TopicsUtils topicsUtils;

    @Override
    public DeviceReport decode(DeviceData deviceData, String clientId) {
        DeviceReport report = new DeviceReport();
        PakModbusRtu message = rtuPakDecoder.decode(deviceData.getBuf());
        PropRead prop = deviceData.getProp();
        ThingsModelValuesInput modelValuesInput = new ThingsModelValuesInput();
        List<ThingsModelSimpleItem> values = new ArrayList<>();
        for (int i = 0; i < prop.getCount(); i++) {
            ThingsModelSimpleItem things = new ThingsModelSimpleItem();
            things.setId(prop.getAddress()+i+"");
            int val = message.getData()[i];
            things.setValue(val + "");
            values.add(things);
        }
        report.setSlaveId(prop.getSlaveId());
        report.setValuesInput(modelValuesInput);
        return report;
    }


    @Override
    public byte[] encode(DeviceData message, String clientId) {
        return new byte[0];
    }

    private void buildReport(DeviceReport message, DeviceData deviceSource) {
        DeviceDownMessage downMessage = deviceSource.getDownMessage();
        message.setMessageId(downMessage.getMessageId());
        message.setProductId(downMessage.getProductId());
        message.setPlatformDate(DateUtils.getNowDate());
        message.setSerialNumber(deviceSource.getSerialNumber());
    }



}
