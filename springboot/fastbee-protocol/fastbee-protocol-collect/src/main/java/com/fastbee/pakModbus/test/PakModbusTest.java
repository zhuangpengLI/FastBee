package com.fastbee.pakModbus.test;

import com.fastbee.modbus.codec.ModbusMessageDecoder;
import com.fastbee.modbus.codec.ModbusMessageEncoder;
import com.fastbee.modbus.model.ModbusRtu;
import com.fastbee.pakModbus.codec.ModbusRtuPakDecoder;
import com.fastbee.pakModbus.model.PakModbusRtu;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
//import org.checkerframework.checker.units.qual.A;

/**
 * @author bill
 */
public class PakModbusTest {

    private static ModbusRtuPakDecoder decoder = new ModbusRtuPakDecoder("com.fastbee.pakModbus");

    public static void main(String[] args) {

        String hex = "FFAA0001010302030578B70D";
        ByteBuf buf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(hex));
        PakModbusRtu message = decoder.decode(buf);
        System.out.println("单个解析=" + message);
        buf.release();

        // String hexMore = "0103a0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000040000000000000000cff039c00200000ffffffff0000006e0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000fa0000000000000000ffc900100019001e001000190019005700074779";
        // ByteBuf bufMore = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(hexMore));
        // PakModbusRtu messageMore = decoder.decode(bufMore);
        // System.out.println("批量上传报文解析=" + messageMore);
        // bufMore.release();

        // ModbusRtu read03 = new ModbusRtu();
        // read03.setSlaveId(1);
        // read03.setAddress(1);
        // read03.setCode(3);
        // read03.setCount(3);
        // ByteBuf out = encoder.encode(read03);
        // System.out.println(ByteBufUtil.hexDump(out));
        //
        // ModbusRtu write06 = new ModbusRtu();
        // write06.setSlaveId(1);
        // write06.setAddress(1);
        // write06.setCode(6);
        // write06.setWriteData(17);
        // ByteBuf writeOut = encoder.encode(write06);
        // System.out.println(ByteBufUtil.hexDump(writeOut));
    }
}
