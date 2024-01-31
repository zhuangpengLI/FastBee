package com.fastbee.bootstrap.tcp;

import com.fastbee.bootstrap.tcp.config.ModbusHandlerInterceptor;
import com.fastbee.common.enums.ServerType;
import com.fastbee.modbus.codec.MessageAdapter;
import com.fastbee.server.Server;
import com.fastbee.base.codec.Delimiter;
import com.fastbee.server.config.NettyConfig;
import com.fastbee.base.core.HandlerMapping;
import com.fastbee.base.session.SessionManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * TCP服务端启动
 * @author bill
 */
@Order(11)
@Configuration
@ConditionalOnProperty(value = "server.tcp.enabled", havingValue = "true")//设置是否启动
@ConfigurationProperties(value = "server.tcp")
@Data
public class TCPBootStrap {

    private int port;
    private int keepAlive;
    private byte delimiter;
    @Autowired
    private MessageAdapter messageAdapter;
    private HandlerMapping handlerMapping;
    private ModbusHandlerInterceptor handlerInterpolator;
    private SessionManager sessionManager;

    public TCPBootStrap( HandlerMapping handlerMapping, ModbusHandlerInterceptor interpolator, SessionManager sessionManager){
        this.handlerMapping = handlerMapping;
        this.handlerInterpolator = interpolator;
        this.sessionManager = sessionManager;
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    public Server TCPServer(){
        return NettyConfig.custom()
                .setIdleStateTime(keepAlive,0,0)
                .setPort(port)
                 //设置报文最大长度  modbus-rtu
                .setMaxFrameLength(100)
                .setDelimiters(new Delimiter(new byte[]{0x7e}, true))
                .setDecoder(messageAdapter)
                .setEncoder(messageAdapter)
                .setHandlerMapping(handlerMapping)
                .setHandlerInterceptor(handlerInterpolator)
                .setSessionManager(sessionManager)
                .setName(ServerType.TCP.getDes())
                .setType(ServerType.TCP)
                .build();
    }
}