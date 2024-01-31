package com.fastbee.protocol.base.model;

import com.fastbee.protocol.base.struc.BaseStructure;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * @author gsb
 * @date 2022/11/9 17:37
 */
public class BufferModel {
    
    public static class ByteBufSchema extends BaseStructure<ByteBuf> {
        @Override
        public ByteBuf readFrom(ByteBuf input) {
            return input.readSlice(input.readableBytes());
        }

        @Override
        public void writeTo(ByteBuf output, ByteBuf value) {
            output.writeBytes(value);
        }
    }

    public static class ByteBufferSchema extends BaseStructure<ByteBuffer> {
        @Override
        public ByteBuffer readFrom(ByteBuf input) {
            ByteBuffer message = input.nioBuffer();
            input.skipBytes(input.readableBytes());
            return message;
        }

        @Override
        public void writeTo(ByteBuf output, ByteBuffer value) {
            output.writeBytes(value);
        }
    }
}
