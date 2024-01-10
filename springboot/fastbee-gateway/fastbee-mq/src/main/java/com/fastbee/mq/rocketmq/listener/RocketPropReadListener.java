package com.fastbee.mq.rocketmq.listener;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.message.DeviceDownMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author gsb
 * @date 2022/10/11 16:49
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = FastBeeConstant.CHANNEL.PROP_READ_GROUP , topic = FastBeeConstant.CHANNEL.PROP_READ)
@ConditionalOnProperty(prefix ="cluster", name = "type",havingValue = FastBeeConstant.MQTT.ROCKET_MQ)
public class RocketPropReadListener implements RocketMQListener<DeviceDownMessage> {

    @Override
    public void onMessage(DeviceDownMessage deviceDownMessage) {

    }
}
