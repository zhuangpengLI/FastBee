package com.fastbee.modbus.pak;

/**
 * TCP-Dtu协议
 * @author gsb
 * @date 2022/11/25 14:12
 */
public interface TcpDtu {

    int 注册报文 = 0x80;
    int 心跳包 = 0x81;
    int 起始位 = 0x7e;
    int 整包消息 = 0x00;


}
