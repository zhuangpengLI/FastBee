package com.fastbee.mq.rocketmq.service;

import com.fastbee.common.core.mq.message.DeviceMessage;
import com.fastbee.mq.rocketmq.producer.RocketMqProducer;
import com.fastbee.mq.service.IMessagePublishService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 设备消息推送至RocketMq
 * @author bill
 */
public class RocketMqPublishServiceImpl implements IMessagePublishService {

    @Autowired
    private RocketMqProducer rocketMqProducer;

    /**
     * rocket通用生产消息方法
     * @param message 设备消息
     * @param channel 推送topic
     */
    @Override
    public void publish(Object message,String channel)
    {
        rocketMqProducer.send(channel,message);
    }
}
