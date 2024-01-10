package com.fastbee.mq.service.impl;

import com.fastbee.base.session.Session;
import com.fastbee.base.session.SessionManager;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.protocol.Message;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.modbus.model.ModbusRtu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author gsb
 * @date 2022/11/22 10:30
 */
@Component
@Slf4j
public class MessageManager {

    private static final Mono<Void> NEVER = Mono.never();
    private static final Mono OFFLINE_EXCEPTION = Mono.error( new ServiceException("离线的客户端",4000));
    private static final Mono OFFLINE_RESULT = Mono.just(new AjaxResult(4000, "离线的客户端"));
    private static final Mono SEND_FAIL_RESULT = Mono.just(new AjaxResult(4001, "消息发送失败"));

    private SessionManager sessionManager;

    public MessageManager(SessionManager sessionManager){
        this.sessionManager = sessionManager;
    }

    public Mono<Void> notifyR(String sessionId, ModbusRtu request){
        Session session = sessionManager.getSession(sessionId);
        if (session == null){
            return OFFLINE_EXCEPTION;
        }
        return session.notify(request);
    }

    public <T> Mono<AjaxResult> requestR(String sessionId, Message request, Class<T> responseClass){
        Session session = sessionManager.getSession(sessionId);
        if (session == null){
            return OFFLINE_RESULT;
        }
        return session.request(request,responseClass)
                .map(message -> AjaxResult.success(message))
                .onErrorResume(e ->{
                    log.warn("消息发送失败:{}",e);
                    return SEND_FAIL_RESULT;
                });
    }

    /**
     * 下发指令等待回复
     * @param sessionId
     * @return
     */
    public <T> Mono<T> request(String sessionId, ModbusRtu request, Class<T> responseClass, long timeout){
        return request(sessionId,request,responseClass).timeout(Duration.ofMillis(timeout));
    }

    /**
     * 下发指令,不等待回复
     * @param sessionId
     * @return
     */
    public <T> Mono<T> request(String sessionId, ModbusRtu request, Class<T> responseClass){
        Session session = sessionManager.getSession(sessionId);
        if (session == null){
            return OFFLINE_EXCEPTION;
        }
        return session.request(request,responseClass);
    }

}
