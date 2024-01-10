package com.fastbee.modbusToJson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fastbee.common.annotation.SysProtocol;
import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReport;
import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.common.core.thingsModel.NeuronModel;
import com.fastbee.common.core.thingsModel.ThingsModelSimpleItem;
import com.fastbee.common.core.thingsModel.ThingsModelValuesInput;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.protocol.base.protocol.IProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsb
 * @date 2023/8/14 16:04
 */
@Slf4j
@Component
@SysProtocol(name = "Modbus转Json解析协议-繁易",protocolCode = FastBeeConstant.PROTOCOL.ModbusToJsonFY,description = "modbus转json解析协议-繁易")
public class ModbusToJsonFYProtocolService implements IProtocol {



    /**
     * {
     * 	"device1": [
     *                {
     * 			"name": "J2",
     * 			"quality": 0,
     * 			"value": 8.331631
     *        },
     *        {
     * 			"name": "J1",
     * 			"quality": 0,
     * 			"value": -130.123718
     *        }
     * 	],
     * 	"device2": [
     *        {
     * 			"name": "J4",
     * 			"quality": 0,
     * 			"value": -16.350224
     *        },
     *        {
     * 			"name": "J3",
     * 			"quality": 0,
     * 			"value": 94.769806
     *        }
     * 	]
     * }
     *
     */
    @Override
    public DeviceReport decode(DeviceData deviceData, String clientId) {
        try {
            DeviceReport reportMessage = new DeviceReport();
            String data = new String(deviceData.getData(),StandardCharsets.UTF_8);
            List<ThingsModelSimpleItem> result = new ArrayList<>();
            Map<String,Object> values = JSON.parseObject(data, Map.class);
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                String slaveKey = entry.getKey();
                Integer slaveId = StringUtils.matcherNum(slaveKey);
                List<FYModel> valueList = JSON.parseArray(JSON.toJSONString(entry.getValue()), FYModel.class);
                for (FYModel fyModel : valueList) {
                    ThingsModelSimpleItem item = new ThingsModelSimpleItem();
                    item.setTs(DateUtils.getNowDate());
                    item.setValue(fyModel.getValue());
                    item.setId(fyModel.getName());
                    item.setSlaveId(slaveId);
                    result.add(item);
                }
            }
            ThingsModelValuesInput valuesInput = new ThingsModelValuesInput();
            valuesInput.setThingsModelValueRemarkItem(result);
            reportMessage.setValuesInput(valuesInput);
            reportMessage.setClientId(clientId);
            reportMessage.setSerialNumber(clientId);
            return reportMessage;
        }catch (Exception e){
            throw new ServiceException("数据解析异常"+e.getMessage());
        }
    }

    @Override
    public byte[] encode(DeviceData message, String clientId) {
        try {
            String msg = JSONObject.toJSONString(message.getDownMessage().getBody());
            return msg.getBytes(StandardCharsets.UTF_8);
        }catch (Exception e){
            log.error("=>指令编码异常,device={},data={}",message.getSerialNumber(),
                    message.getDownMessage().getBody());
            return null;
        }
    }

}
