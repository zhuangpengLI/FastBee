package com.fastbee.jsonPak;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fastbee.common.annotation.SysProtocol;
import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReport;
import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.common.core.thingsModel.ThingsModelSimpleItem;
import com.fastbee.common.core.thingsModel.ThingsModelValuesInput;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.protocol.base.protocol.IProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsb
 * @date 2022/10/10 16:55
 */
@Slf4j
@Component
@SysProtocol(name = "JSONObject解析协议",protocolCode = FastBeeConstant.PROTOCOL.JsonObject,description = "系统内置JSONObject解析协议")
public class JsonPakProtocolService implements IProtocol {

    /**
     * 解析json格式数据
     * 上报数据格式：  [{\"id\":\"switch\",\"value\":\"0\"},{\"id\":\"gear\",\"value\":\"0\"}]
     */
    @Override
    public DeviceReport decode(DeviceData deviceData, String clientId) {
        try {
            DeviceReport reportMessage = new DeviceReport();
            // bytep[] 转String
            String data = new String(deviceData.getData(),StandardCharsets.UTF_8);
            Map<String,Object> values = JSON.parseObject(data, Map.class);
            List<ThingsModelSimpleItem> result = new ArrayList<>();
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                ThingsModelSimpleItem item = new ThingsModelSimpleItem();
                item.setTs(DateUtils.getNowDate());
                item.setValue(entry.getValue()+"");
                item.setId(entry.getKey());
                result.add(item);
            }
            ThingsModelValuesInput valuesInput = new ThingsModelValuesInput();
            valuesInput.setThingsModelValueRemarkItem(result);
            reportMessage.setValuesInput(valuesInput);
             reportMessage.setIsPackage(true);
             reportMessage.setMessageId("0");
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
            String msg = message.getBody().toString();
            return msg.getBytes(StandardCharsets.UTF_8);
        }catch (Exception e){
            log.error("=>指令编码异常,device={},data={}",message.getSerialNumber(),
                    message.getDownMessage().getBody());
            return null;
        }
    }
}
