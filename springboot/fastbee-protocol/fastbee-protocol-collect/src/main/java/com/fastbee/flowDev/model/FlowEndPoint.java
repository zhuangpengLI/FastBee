package com.fastbee.flowDev.model;

import com.fastbee.base.core.annotation.Node;
import com.fastbee.base.core.annotation.PakMapping;
import com.fastbee.base.session.Session;
import com.fastbee.common.core.mq.DeviceReport;
import com.fastbee.common.core.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gsb
 * @date 2023/5/19 14:09
 */
@Component
@Slf4j
@Node
public class FlowEndPoint {

    @PakMapping(types = 0x68)
    public Message register(DeviceReport message, Session session){
        //检测设备是否注册，未注册，进行注册
        if (!session.isRegistered()){
            // 注册设备
            session.register(message);
        }
        return message;
    }
}
