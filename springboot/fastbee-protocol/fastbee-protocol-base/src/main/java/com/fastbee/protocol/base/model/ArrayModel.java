package com.fastbee.protocol.base.model;

import com.fastbee.protocol.base.struc.BaseStructure;
import io.netty.buffer.ByteBuf;

/**
 * @author gsb
 * @date 2022/11/9 17:33
 */
public class ArrayModel {
    public static final WModel<char[]> CHARS = new CharArray();
    public static final WModel<byte[]> BYTES = new ByteArray();
    public static final WModel<short[]> SHORTS = new ShortArray();
    public static final WModel<int[]> INTS = new IntArray();
    public static final WModel<float[]> FLOATS = new FloatArray();
    public static final WModel<long[]> LONGS = new LongArray();
    public static final WModel<double[]> DOUBLES = new DoubleArray();

    protected static class ByteArray extends BaseStructure<byte[]> {
        @Override
        public byte[] readFrom(ByteBuf input) {
            byte[] array = new byte[input.readableBytes()];
            input.readBytes(array);
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, byte[] array) {
            if (array == null) {
                return;
            }
            output.writeBytes(array);
        }
    }

    protected static class CharArray extends BaseStructure<char[]> {
        @Override
        public char[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 1;
            char[] array = new char[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readChar();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, char[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeChar(array[i]);
            }
        }
    }

    protected static class ShortArray extends BaseStructure<short[]> {
        @Override
        public short[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 1;
            short[] array = new short[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readShort();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, short[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeShort(array[i]);
            }
        }
    }

    protected static class IntArray extends BaseStructure<int[]> {
        @Override
        public int[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 2;
            int[] array = new int[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readInt();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, int[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeInt(array[i]);
            }
        }
    }

    protected static class LongArray extends BaseStructure<long[]> {
        @Override
        public long[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 3;
            long[] array = new long[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readLong();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, long[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeLong(array[i]);
            }
        }
    }

    protected static class FloatArray extends BaseStructure<float[]> {
        @Override
        public float[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 2;
            float[] array = new float[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readFloat();
            }
            return array;
        }


        @Override
        public void writeTo(ByteBuf output, float[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeFloat(array[i]);
            }
        }
    }

    protected static class DoubleArray extends BaseStructure<double[]> {
        @Override
        public double[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 3;
            double[] array = new double[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readDouble();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, double[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeDouble(array[i]);
            }
        }
    }
}
