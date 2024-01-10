package com.fastbee.mq.rocketmq.listener;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.MQSendMessageBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * MQ监听服务下发消息
 * @author gsb
 * @date 2022/10/11 9:53
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = FastBeeConstant.CHANNEL.FUNCTION_INVOKE_GROUP , topic = FastBeeConstant.CHANNEL.FUNCTION_INVOKE)
@ConditionalOnProperty(prefix ="cluster", name = "type",havingValue = FastBeeConstant.MQTT.ROCKET_MQ)
public class RocketFunctionInvokeListener implements RocketMQListener<MQSendMessageBo> {


    @Override
    public void onMessage(MQSendMessageBo bo) {

    }
}
