package com.fastbee.mq.rocketmq.listener;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceStatusBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * RocketMQ监听设备状态消息
 * @author gsb
 * @date 2022/10/11 9:37
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = FastBeeConstant.CHANNEL.DEVICE_STATUS_GROUP , topic = FastBeeConstant.CHANNEL.DEVICE_STATUS)
@ConditionalOnProperty(prefix ="cluster", name = "type",havingValue = FastBeeConstant.MQTT.ROCKET_MQ)
public class RocketDeviceStatusListener implements RocketMQListener<DeviceStatusBo> {

    @Override
    public void onMessage(DeviceStatusBo deviceStatusBo) {
        log.debug("=>收到设备状态消息,message=[{}]",deviceStatusBo);
    }
}
