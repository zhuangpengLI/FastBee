package com.fastbee.mq.rocketmq.consumer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author bill
 */
@Component
@ConfigurationProperties(prefix = "rocketmq.producer")
@Data
public class ConsumerTopicConstant {

    /**网关默认主题*/
    private String mqTopic;
    /*设备状态topic*/
    private String deviceStatusTopic;
    /*设备主动上报topic*/
    private String deviceUpTopic;
    /*设备服务下发topic*/
    private String functionInvokeTopic;
    /*设备消息回调topic*/
    private String deviceReplyTopic;
    /*平台获取属性topic*/
    private String fetchPropTopic;
}
