package com.fastbee.protocol.service;

import com.fastbee.common.core.mq.message.ProtocolDto;
import com.fastbee.protocol.base.protocol.IProtocol;
import com.fastbee.protocol.domain.DeviceProtocol;

import java.util.List;

/**
 * @author gsb
 * @date 2022/10/10 17:07
 */
public interface IProtocolManagerService {

    /**
     *获取所有的协议，包含脚本解析协议和系统内部定义协议
     */
    public List<ProtocolDto> getAllProtocols();

    /**
     * 根据协议编码获取系统内部协议
      * @param protocolCode 协议编码
     * @return 协议
     */
   IProtocol getProtocolByProtocolCode(String protocolCode);

    /**
     * 根据设备编号获取系统内部协议实例
     * @param serialNumber 产品编号
     * @return 协议实例
     */
   DeviceProtocol getProtocolBySerialNumber(String serialNumber);

}
