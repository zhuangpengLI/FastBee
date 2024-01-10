package com.fastbee.mq.redischannel.consumer;

import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.MQSendMessageBo;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.service.IDeviceService;
import com.fastbee.mq.service.IMqttMessagePublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 指令(服务)下发处理类
 *
 * @author gsb
 * @date 2022/10/11 8:17
 */
@Slf4j
@Component
public class FunctionInvokeConsumer {


    @Autowired
    private IMqttMessagePublish functionSendService;
    @Autowired
    private IDeviceService deviceService;

    @Async(FastBeeConstant.TASK.FUNCTION_INVOKE_TASK)
    public void handler(MQSendMessageBo bo) {
        try {
            Device device = deviceService.selectDeviceBySerialNumber(bo.getSerialNumber());
            Optional.ofNullable(device).orElseThrow(()->new ServiceException("服务下发的设备:["+bo.getSerialNumber()+"]不存在"));
            //处理数据下发
            functionSendService.funcSend(bo);
        } catch (Exception e) {
            log.error("=>服务下发异常", e);
        }
    }
}
