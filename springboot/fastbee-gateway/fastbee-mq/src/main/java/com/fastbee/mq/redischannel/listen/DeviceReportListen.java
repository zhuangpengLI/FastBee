package com.fastbee.mq.redischannel.listen;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReportBo;
import com.fastbee.mq.redischannel.consumer.DeviceReportMsgConsumer;
import com.fastbee.mq.redischannel.queue.DeviceReplyQueue;
import com.fastbee.mq.redischannel.queue.DeviceReportQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 设备主动上报消息监听
 *
 * @author bill
 */
@Slf4j
@Component
public class DeviceReportListen {

    @Autowired
    private DeviceReportMsgConsumer reportMsgConsumer;

    @Async(FastBeeConstant.TASK.MESSAGE_CONSUME_TASK_PUB)
    public void listen() {
        while (true) {
            try {
                /*取出数据*/
                DeviceReportBo reportBo = DeviceReportQueue.take();
                /*处理数据*/
                reportMsgConsumer.consume(reportBo);
            } catch (Exception e) {
                log.error("=>设备上报数据监听异常", e);
            }
        }
    }
}
