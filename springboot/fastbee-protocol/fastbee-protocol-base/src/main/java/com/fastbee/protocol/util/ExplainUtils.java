package com.fastbee.protocol.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.util.LinkedList;

/**
 * 编解码分析
 *
 * @author gsb
 * @date 2022/11/9 16:35
 */
public class ExplainUtils extends LinkedList<Msg> {

    public void readField(int index, String desc, Object value, ByteBuf input) {
        if (value != null) {
            this.add(Msg.field(index, desc, value, ByteBufUtil.hexDump(input, index, input.readerIndex() - index)));
        }
    }

    public void writeField(int index, String desc, Object value, ByteBuf output) {
        if (value != null) {
            this.add(Msg.field(index, desc, value, ByteBufUtil.hexDump(output, index, output.writerIndex() - index)));
        }
    }

    public Msg lengthField(int index, String desc, int length, int lengthUnit) {
        Msg info = Msg.lengthField(index, desc, length, lengthUnit);
        this.add(info);
        return info;
    }

    public void setLastDesc(String desc) {
        this.get(this.size() - 1).desc = desc;
    }

    public void println() {
        for (Msg info : this) {
            System.out.println(info);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.size() << 5);
        for (Msg info : this) {
            sb.append(info).append('\n');
        }
        return sb.toString();
    }
}
