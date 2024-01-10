package com.fastbee.modbus.test;

import com.fastbee.common.core.mq.message.DeviceData;
import com.fastbee.common.core.protocol.modbus.ModbusCode;
import com.fastbee.common.utils.CaculateUtils;
import com.fastbee.common.utils.gateway.protocol.ByteUtils;
import com.fastbee.modbus.codec.ModbusMessageDecoder;
import com.fastbee.modbus.codec.ModbusMessageEncoder;
import com.fastbee.modbus.model.ModbusRtu;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * 协议解析本地测试
 * @author bill
 */
public class ModbusTest {

    private static ModbusMessageDecoder decoder = new ModbusMessageDecoder("com.fastbee.modbus");
    private static ModbusMessageEncoder encoder = new ModbusMessageEncoder("com.fastbee.modbus");

    public static void main(String[] args) throws Exception {

        String hex = "0103020016398a";
        ByteBuf buf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(hex));
        DeviceData data = DeviceData.builder()
                .buf(buf)
                .code(ModbusCode.Read03)
                .build();
        ModbusRtu message = decoder.decode(data);
        System.out.println("单个解析="+message);
        buf.release();

        String hexMore = "02030C3A60061A41EE8000BB229F3A9C68";
        ByteBuf bufMore = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(hexMore));
        DeviceData dataMore = DeviceData.builder()
                .buf(bufMore).build();
        ModbusRtu messageMore = decoder.decode(dataMore);
        System.out.println("批量上传报文解析="+messageMore);
        bufMore.release();
        ModbusRtu read03 = new ModbusRtu();
        read03.setSlaveId(1);
        read03.setAddress(1);
        read03.setCode(3);
        read03.setCount(3);
        ByteBuf out = encoder.encode(read03);
        System.out.println(ByteBufUtil.hexDump(out));

        ModbusRtu write06 = new ModbusRtu();
        write06.setSlaveId(1);
        write06.setAddress(1);
        write06.setCode(6);
        write06.setWriteData(17);
        ByteBuf writeOut = encoder.encode(write06);
        System.out.println(ByteBufUtil.hexDump(writeOut));


        String s = Integer.toHexString(Integer.parseInt("16878"));
        String s1 = Integer.toHexString(16878);
        System.out.println(s);
        String hexToBytes = "41EE8000";
        byte[] bytes = ByteBufUtil.decodeHexDump(hexToBytes);
        String v = CaculateUtils.toFloat(bytes);
        System.out.println(v);

        System.out.println(ByteUtils.intToHex(-32768));
    }
}
