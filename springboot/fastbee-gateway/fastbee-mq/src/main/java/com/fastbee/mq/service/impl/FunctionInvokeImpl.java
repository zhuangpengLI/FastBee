package com.fastbee.mq.service.impl;

import com.fastbee.common.core.mq.DeviceReplyBo;
import com.fastbee.common.core.mq.InvokeReqDto;
import com.fastbee.common.core.mq.MQSendMessageBo;
import com.fastbee.common.core.mq.MessageReplyBo;
import com.fastbee.common.core.protocol.modbus.ModbusCode;
import com.fastbee.common.core.redis.RedisCache;
import com.fastbee.common.core.redis.RedisKeyBuilder;
import com.fastbee.common.enums.ThingsModelType;
import com.fastbee.common.utils.bean.BeanUtils;
import com.fastbee.iot.util.SnowflakeIdWorker;
import com.fastbee.mq.redischannel.producer.MessageProducer;
import com.fastbee.mq.service.IFunctionInvoke;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gsb
 * @date 2022/12/5 11:34
 */
@Slf4j
@Service
public class FunctionInvokeImpl implements IFunctionInvoke {

    @Resource
    private RedisCache redisCache;

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(2);

    /**
     * 服务调用，等待设备响应
     * @param reqDto 服务下发对象
     * @return 数据结果
     */
    @Override
    public Map<String ,Object> invokeReply(InvokeReqDto reqDto){
       Map<String,Object> result = new HashMap<>();
       invokeNoReply(reqDto);
       //TODO- 根据消息id查询回执，暂时没有消息Id回执
       return result;
    }

    /**
     * 服务调用,设备不响应
     * @param reqDto 服务下发对象
     * @return 消息id messageId
     */
    @Override
    public String invokeNoReply(InvokeReqDto reqDto){
        log.debug("=>下发指令请求：[{}]",reqDto);
        MQSendMessageBo bo = new MQSendMessageBo();
        BeanUtils.copyBeanProp(bo,reqDto);
        long id = snowflakeIdWorker.nextId();
        String messageId = id+"";
        bo.setMessageId(messageId+"");
        bo.setType(ThingsModelType.getType(reqDto.getType()));
        MessageProducer.sendFunctionInvoke(bo);
        //10s，设备不回复，认为指令下发失败
        DeviceReplyBo replyBo = new DeviceReplyBo();
        replyBo.setId(reqDto.getIdentifier());
        replyBo.setMessageId(messageId);
        replyBo.setValue(reqDto.getValue().get(reqDto.getIdentifier()).toString());
        String cacheKey = RedisKeyBuilder.buildDownMessageIdCacheKey(reqDto.getSerialNumber());
        redisCache.setCacheObject(cacheKey, replyBo, 10000, TimeUnit.MILLISECONDS);
        return messageId;
    }

    /**
     * TODO- 轮询拿返回值
     */
    private MessageReplyBo queryResult(InvokeReqDto reqDto){
        MessageReplyBo replyBo = null;
        return replyBo;
    }
}
