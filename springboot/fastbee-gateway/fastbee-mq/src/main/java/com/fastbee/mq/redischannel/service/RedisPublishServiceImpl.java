package com.fastbee.mq.redischannel.service;

import com.fastbee.common.core.redis.RedisCache;
import com.fastbee.mq.service.IMessagePublishService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 设备消息推送至RedisChannel
 * @author bill
 */
@NoArgsConstructor
public class RedisPublishServiceImpl implements IMessagePublishService {

    @Autowired
    private RedisCache redisCache;

    /**
     * 消息推送到redisChannel
     * @param message 设备消息
     * @param channel 推送channel
     */
    @Override
    public void publish(Object message,String channel) {
        redisCache.publish(message,channel);
    }
}
