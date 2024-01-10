package com.fastbee.mq.redischannel.listen;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.message.DeviceDownMessage;
import com.fastbee.mq.redischannel.consumer.DevicePropFetchConsumer;
import com.fastbee.mq.redischannel.queue.DevicePropFetchQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 设备属性获取(定时获取)监听
 *
 * @author gsb
 * @date 2022/10/11 8:26
 */
@Slf4j
@Component
public class DevicePropFetchListen {

    @Autowired
    private DevicePropFetchConsumer devicePropFetchConsumer;

    @Async(FastBeeConstant.TASK.MESSAGE_CONSUME_TASK_FETCH)
    public void listen() {
        while (true) {
            try {
                DeviceDownMessage downMessage = DevicePropFetchQueue.take();
                devicePropFetchConsumer.consume(downMessage);
                Thread.sleep(200);
            } catch (Exception e) {
                log.error("=>设备属性获取异常", e);
            }
        }
    }
}
