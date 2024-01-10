package com.fastbee.mq.rocketmq.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 网关通用模型
 * @author bill
 */
@Data
public class MQSendMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**topic*/
    private String topicName;

    /**消息-json格式*/
    private String message;
}
