package com.fastbee.protocol.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 位定义 0-关闭 1-k开启 ,如果不同状态清重写
 * @author bill
 */
@Getter
@AllArgsConstructor
public enum ModbusBitStatus {

    OPEN((byte) 0x01),
    CLOSED((byte) 0x00)
    ;
    private byte bit;

}
