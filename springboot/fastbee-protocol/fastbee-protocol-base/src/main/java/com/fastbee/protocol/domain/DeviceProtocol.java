package com.fastbee.protocol.domain;

import com.fastbee.protocol.base.protocol.IProtocol;
import lombok.Data;

/**
 * 设备协议model
 * @author gsb
 * @date 2022/10/25 13:38
 */
@Data
public class DeviceProtocol {

    /**协议实例*/
    private IProtocol protocol;

    /**产品id*/
    private Long productId;

    /**设备编号*/
    private String serialNumber;
}
