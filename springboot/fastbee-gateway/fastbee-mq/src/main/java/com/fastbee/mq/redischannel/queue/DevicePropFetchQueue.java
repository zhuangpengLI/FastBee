package com.fastbee.mq.redischannel.queue;

import com.fastbee.common.core.mq.DeviceReportBo;
import com.fastbee.common.core.mq.message.DeviceDownMessage;
import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 设备属性获取存储列队
 * @author gsb
 * @date 2022/10/11 8:29
 */
public class DevicePropFetchQueue {
    private static final LinkedBlockingQueue<DeviceDownMessage> queue = new LinkedBlockingQueue<>();

    /*元素加入队列,最后*/
    public static void offer(DeviceDownMessage dto){
        queue.offer(dto);
    }
    /*取出队列元素 先进先出*/
    @SneakyThrows
    public static DeviceDownMessage take(){
        return queue.take();
    }
}
