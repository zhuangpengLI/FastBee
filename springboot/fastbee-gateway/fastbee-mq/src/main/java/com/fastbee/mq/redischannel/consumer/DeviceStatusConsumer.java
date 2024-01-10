package com.fastbee.mq.redischannel.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceStatusBo;
import com.fastbee.common.core.mq.MQSendMessageBo;
import com.fastbee.common.core.thingsModel.ThingsModelSimpleItem;
import com.fastbee.common.enums.DeviceStatus;
import com.fastbee.common.enums.ThingsModelType;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.Product;
import com.fastbee.iot.model.ThingsModels.ThingsModelShadow;
import com.fastbee.iot.service.IDeviceService;
import com.fastbee.iot.service.IProductService;
import com.fastbee.iot.service.cache.IDeviceCache;
import com.fastbee.iot.util.SnowflakeIdWorker;
import com.fastbee.mq.model.ReportDataBo;
import com.fastbee.mq.redischannel.producer.MessageProducer;
import com.fastbee.mq.service.IMqttMessagePublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备状态消息处理类
 * @author gsb
 * @date 2022/10/10 11:02
 */
@Slf4j
@Component
public class DeviceStatusConsumer {

    @Autowired
    private IDeviceCache deviceCache;
    @Resource
    private IDeviceService deviceService;
    @Resource
    private IProductService productService;
    @Resource
    private IMqttMessagePublish mqttMessagePublish;
    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(2);


    @Async(FastBeeConstant.TASK.DEVICE_STATUS_TASK)
    public void consume(DeviceStatusBo bo){
       try {
           /*更新设备状态*/
           Device device = deviceCache.updateDeviceStatusCache(bo);
           //处理影子模式值
           this.handlerShadow(device,bo.getStatus());
       }catch (Exception e){
           log.error("=>设备状态处理异常",e);
       }
    }

    private void handlerShadow(Device device,DeviceStatus status){
        //获取设备协议编码
        Product product = productService.selectProductByProductId(device.getProductId());
        /* 设备上线 处理影子值*/
        if (status.equals(DeviceStatus.ONLINE) && device.getIsShadow() ==1){
            ThingsModelShadow shadow = deviceService.getDeviceShadowThingsModel(device);
            List<ThingsModelSimpleItem> properties = shadow.getProperties();
            List<ThingsModelSimpleItem> functions = shadow.getFunctions();
            //JsonArray组合发送
            if (FastBeeConstant.PROTOCOL.JsonArray.equals(product.getProtocolCode())) {
                if (!CollectionUtils.isEmpty(properties)) {
                    mqttMessagePublish.publishProperty(device.getProductId(), device.getSerialNumber(), properties, 3);
                }
                if (!CollectionUtils.isEmpty(functions)) {
                    mqttMessagePublish.publishFunction(device.getProductId(), device.getSerialNumber(), functions, 3);
                }
            } else { //其他协议单个发送
                functions.addAll(properties);
                if (!CollectionUtils.isEmpty(functions)) {
                    for (ThingsModelSimpleItem function : functions) {
                        MQSendMessageBo bo = new MQSendMessageBo();
                        bo.setTransport(product.getTransport());
                        bo.setProtocolCode(product.getProtocolCode());
                        bo.setIsShadow(false);
                        bo.setProductId(product.getProductId());
                        bo.setIdentifier(function.getId());
                        bo.setSerialNumber(device.getSerialNumber());
                        bo.setType(ThingsModelType.SERVICE);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(function.getId(),function.getValue());
                        bo.setValue(jsonObject);
                        long id = snowflakeIdWorker.nextId();
                        bo.setMessageId(id +"");
                        bo.setSlaveId(function.getSlaveId());
                        //发送到MQ处理
                        MessageProducer.sendFunctionInvoke(bo);
                    }
                }
            }
        }
    }
}
