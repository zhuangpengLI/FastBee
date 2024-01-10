package com.fastbee.mq.redischannel.consumer;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReportBo;
import com.fastbee.common.core.redis.RedisCache;
import com.fastbee.mq.service.IDeviceReportMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 设备上报消息处理
 *
 * @author bill
 */
@Slf4j
@Component
public class DeviceReportMsgConsumer {


    @Autowired
    private IDeviceReportMessageService reportMessageService;

    @Async(FastBeeConstant.TASK.DEVICE_UP_MESSAGE_TASK)
    public void consume(DeviceReportBo bo) {
        try {
            //处理数据解析
            reportMessageService.parseReportMsg(bo);
        } catch (Exception e) {
            log.error("设备主动上报队列监听异常", e);
        }
    }


}
