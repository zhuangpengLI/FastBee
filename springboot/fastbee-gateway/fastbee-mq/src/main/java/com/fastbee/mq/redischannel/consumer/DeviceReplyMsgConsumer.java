package com.fastbee.mq.redischannel.consumer;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReportBo;
import com.fastbee.mq.service.IDeviceReportMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 设备消息回复
 *
 * @author gsb
 * @date 2022/10/10 11:12
 */
@Component
@Slf4j
public class DeviceReplyMsgConsumer {


    @Resource
    private IDeviceReportMessageService deviceReportMessageService;


    /*设备回调消息，统一用上报model*/
    @Async(FastBeeConstant.TASK.DEVICE_REPLY_MESSAGE_TASK)
    public void consume(DeviceReportBo bo) {
        try {
            String topicName = bo.getTopicName();

            if (topicName.endsWith(FastBeeConstant.TOPIC.MSG_REPLY)) {
                //普通设备回复消息
                deviceReportMessageService.parseReplyMsg(bo);
            } else if (topicName.endsWith(FastBeeConstant.TOPIC.UPGRADE_REPLY) ||
                    topicName.endsWith(FastBeeConstant.TOPIC.SUB_UPGRADE_REPLY)) {
            }
        } catch (Exception e) {
            log.error("=>设备回复消息消费异常", e);
        }
    }
}
