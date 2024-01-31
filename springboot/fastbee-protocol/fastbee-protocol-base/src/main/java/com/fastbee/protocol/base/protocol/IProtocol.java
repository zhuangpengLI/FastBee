package com.fastbee.protocol.base.protocol;


import com.fastbee.common.core.mq.DeviceReport;
import com.fastbee.common.core.mq.message.DeviceData;

/**
 * 基础协议
 * @author gsb
 * @date 2022/10/10 15:48
 */
public interface IProtocol {

    DeviceReport decode(DeviceData data, String clientId);

    byte[] encode(DeviceData message, String clientId);

}
