package com.fastbee.bootstrap.udp;

import com.fastbee.bootstrap.tcp.config.ModbusHandlerInterceptor;
import com.fastbee.common.enums.ServerType;
import com.fastbee.modbus.codec.MessageAdapter;
import com.fastbee.server.Server;
import com.fastbee.server.config.NettyConfig;
import com.fastbee.base.core.HandlerMapping;
import com.fastbee.base.session.SessionManager;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * UDP服务端启动
 * @author bill
 */
@Order(12)
@Configuration
@ConditionalOnProperty(value = "server.udp.enabled", havingValue = "true")//设置是否启动
@ConfigurationProperties(value = "server.udp")
@Data
public class UDPBootStrap {

    private int port;
    private byte delimiter;

    private MessageAdapter messageAdapter;
    private HandlerMapping handlerMapping;
    private ModbusHandlerInterceptor handlerInterpolator;
    private SessionManager sessionManager;

    public UDPBootStrap(MessageAdapter messageAdapter, HandlerMapping handlerMapping, ModbusHandlerInterceptor interpolator, SessionManager sessionManager){
        this.messageAdapter = messageAdapter;
        this.handlerMapping = handlerMapping;
        this.handlerInterpolator = interpolator;
        this.sessionManager = sessionManager;
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    public Server UDPServer(){
        return NettyConfig.custom()
                .setPort(port)
                //.setDelimiters(new Delimiter(new byte[]{0x0D},false)) //分隔符配置
                .setDecoder(messageAdapter)
                .setEncoder(messageAdapter)
                .setHandlerMapping(handlerMapping)
                .setHandlerInterceptor(handlerInterpolator)
                .setSessionManager(sessionManager)
                .setName(ServerType.UDP.getDes())
                .setType(ServerType.UDP)
                .build();
    }

}
