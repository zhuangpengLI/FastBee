package com.fastbee.flowDev.codec;

import cn.hutool.core.util.ArrayUtil;
import com.fastbee.common.annotation.SysProtocol;
import com.fastbee.common.constant.FastBeeConstant;
import com.fastbee.common.core.mq.DeviceReport;
import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.common.core.mq.message.DeviceDownMessage;
import com.fastbee.common.core.thingsModel.ThingsModelSimpleItem;
import com.fastbee.common.core.thingsModel.ThingsModelValuesInput;
import com.fastbee.common.exception.ServiceException;
import com.fastbee.common.utils.BeanMapUtilByReflect;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.common.utils.gateway.CRC8Utils;
import com.fastbee.flowDev.model.FlowDev;
import com.fastbee.protocol.base.protocol.IProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gsb
 * @date 2023/5/17 16:56
 */
@Slf4j
@Component
@SysProtocol(name = "流量计解析协议",protocolCode = FastBeeConstant.PROTOCOL.FlowMeter,description = "流量计解析协议")
public class FlowDevProtocol implements IProtocol {

    @Resource
    private FlowDevDecoder devDecoder;
    @Resource
    private FlowDevEncoder devEncoder;


    @Override
    public DeviceReport decode(DeviceData data, String clientId) {
        try {
            DeviceReport report = new DeviceReport();
            FlowDev flowDev = devDecoder.decode(data,null);
            this.handlerData(flowDev);
            ThingsModelValuesInput modelValuesInput = new ThingsModelValuesInput();
            List<ThingsModelSimpleItem> items = BeanMapUtilByReflect.beanToItem(flowDev);
            modelValuesInput.setThingsModelValueRemarkItem(items);
            report.setValuesInput(modelValuesInput);
            report.setClientId(clientId);
            report.setMessageId(flowDev.getStart()+"");
            report.setIsPackage(true);
            return report;
        }catch (Exception e){
            log.error("=>数据解析出错",e);
            throw new ServiceException("数据解析出错"+e);
        }
    }

    @Override
    public byte[] encode(DeviceData message, String clientId) {
        FlowDev flowDev = new FlowDev();
        flowDev.setImei(clientId);
        flowDev.setDire(0x33);
        flowDev.setLength(0x08);
        ByteBuf buf = devEncoder.encode(flowDev, null);
        byte[] source = ByteBufUtil.getBytes(buf, 3, buf.writerIndex() - 5);
        byte[] result = new byte[ByteBufUtil.getBytes(buf).length];
        byte b = CRC8Utils.calcCrc8_E5(source);
        byte[] crc = new  byte[]{b,0x16};
        System.arraycopy(source,0,result,0,source.length);
        System.arraycopy(crc,0,result,result.length -2,2);
        System.out.println(ByteBufUtil.hexDump(buf));
        //删除缓存，防止netty内存溢出
        ReferenceCountUtil.release(buf);
        return result;
    }

    private FlowDev handlerData(FlowDev flowDev){
        //时间处理
        String ts = flowDev.getTs();
        if (StringUtils.isNotEmpty(ts)){
            Date date = DateUtils.dateTime(DateUtils.SS_MM_HH_DD_HH_YY, ts);
            String s = DateUtils.dateTimeYY(date);
            flowDev.setTs(s);
        }
        String sum = flowDev.getSum();
        if (StringUtils.isNotEmpty(sum)){
            String replace = sum.replace("0", "");
            flowDev.setSum(replace.equals("")? "0":replace);
        }
        String instant = flowDev.getInstant();
        if (StringUtils.isNotEmpty(instant)){
            String replace = instant.replace("0", "");
            flowDev.setInstant(replace.equals("")? "0":replace);
            int val = Integer.parseInt(flowDev.getInstant())/1000;
            flowDev.setInstant(val+"");
        }
        return flowDev;
    }
}
