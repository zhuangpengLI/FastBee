package com.fastbee.protocol.base.model;

import com.fastbee.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;

/**
 * 消息结构
 *
 * @author gsb
 * @date 2022/11/9 11:55
 */
public interface WModel<T> {

    /**
     * 读缓存
     */
    T readFrom(ByteBuf in);

    void writeTo(ByteBuf out, T value);

    default T readFrom(ByteBuf in, int length) {
        /*输入的报文长度*/
        int readLength = in.readerIndex() + length;
        /*读索引位*/
        int writerIndex = in.writerIndex();
        in.writerIndex(readLength);
        T value = readFrom(in);
        in.setIndex(readLength, writerIndex);
        return value;
    }

    default void writeTo(ByteBuf out, int length, T value) {
        int writeLength = out.writerIndex() + length;
        writeTo(out, value);
        out.writerIndex(writeLength);
    }

    default T readFrom(ByteBuf in, ExplainUtils explain) {
        int start = in.readerIndex();
        T value = readFrom(in);
        explain.readField(start,desc(), value,in);
        return value;
    }

    default void writeTo(ByteBuf out, T value, ExplainUtils explain) {
        int begin = out.writerIndex();
        writeTo(out, value);
        explain.writeField(begin, desc(), value, out);
    }

    default T readFrom(ByteBuf in, int length, ExplainUtils explain) {
        int readerLength = in.readerIndex() + length;
        int writerIndex = in.writerIndex();
        in.writerIndex(readerLength);
        T value = readFrom(in, explain);
        in.setIndex(readerLength, writerIndex);
        return value;
    }

    default void writeTo(ByteBuf out, int length, T value, ExplainUtils explain) {
        int writerLength = out.writerIndex() + length;
        writeTo(out, value, explain);
        out.writerIndex(writerLength);
    }

    /** 内存分配 */
    default int length() {
        return 32;
    }

    default String desc() {
        return "";
    }
}
