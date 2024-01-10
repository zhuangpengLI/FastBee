package com.fastbee.mq.config;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.mq.redischannel.service.RedisPublishServiceImpl;
import com.fastbee.mq.rocketmq.service.RocketMqPublishServiceImpl;
import com.fastbee.mq.service.IMessagePublishService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mq集群配置
 * @author gsb
 * @date 2022/10/10 8:27
 */
@Configuration
//是否开启集群，默认不开启
@ConditionalOnExpression("${cluster.enable:false}")
public class MqConfig {

    @Bean
    @ConditionalOnProperty(prefix ="cluster", name = "type" ,havingValue = FastBeeConstant.MQTT.REDIS_CHANNEL,matchIfMissing = true)
    public IMessagePublishService redisChannelPublish(){
       return new RedisPublishServiceImpl();
    }

    //@Bean
    @ConditionalOnProperty(prefix ="cluster", name = "type",havingValue = FastBeeConstant.MQTT.ROCKET_MQ)
    public IMessagePublishService rocketMqPublish() {
        return new RocketMqPublishServiceImpl();
    }


}
