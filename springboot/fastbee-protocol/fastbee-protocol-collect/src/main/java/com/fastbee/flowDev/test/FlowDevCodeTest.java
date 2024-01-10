package com.fastbee.flowDev.test;

import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.flowDev.codec.FlowDevDecoder;
import com.fastbee.flowDev.codec.FlowDevEncoder;
import com.fastbee.flowDev.model.FlowDev;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * @author bill
 */
public class FlowDevCodeTest {

    private static FlowDevDecoder decoder = new FlowDevDecoder("com.fastbee");
    private static FlowDevEncoder encoder = new FlowDevEncoder("com.fastbee");


    public static void main(String[] args) {

        String flowData = "681B68B33701120008C100000000000000000000022050004341231811215716";
        ByteBuf in = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(flowData));
        DeviceData data = DeviceData.builder()
                .buf(in).build();
        FlowDev flowDev = decoder.decode(data, null);
        System.out.println(flowDev);

    }
}
