package com.fastbee.mq.redischannel.listen;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReportBo;
import com.fastbee.mq.redischannel.consumer.DeviceReplyMsgConsumer;
import com.fastbee.mq.redischannel.queue.DeviceReplyQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 设备回调消息监听
 *
 * @author bill
 */
@Slf4j
@Component
public class DeviceReplyListen {

    @Autowired
    private DeviceReplyMsgConsumer deviceReplyMsgHandler;

    @Async(FastBeeConstant.TASK.MESSAGE_CONSUME_TASK_PUB)
    public void listen() {
        while (true) {
            try {
                /*读队列消息*/
                DeviceReportBo reportBo = DeviceReplyQueue.take();
                /*处理消息*/
                deviceReplyMsgHandler.consume(reportBo);
            } catch (Exception e) {
                log.error("=>设备回调消息监听异常", e);
            }
        }
    }
}
