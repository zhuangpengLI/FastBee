package com.fastbee.mq.rocketmq.listener;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReportBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * MQ监听设备推送消息(上报消息和回调消息)
 * @author gsb
 * @date 2022/10/11 9:51
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = FastBeeConstant.CHANNEL.PUBLISH_GROUP , topic = FastBeeConstant.CHANNEL.PUBLISH)
@ConditionalOnProperty(prefix ="cluster", name = "type",havingValue = FastBeeConstant.MQTT.ROCKET_MQ)
public class RocketPublishMsgListener implements RocketMQListener<DeviceReportBo> {

    @Override
    public void onMessage(DeviceReportBo bo) {
        log.debug("=>收到设备推送消息,message=[{}]",bo);
    }
}
