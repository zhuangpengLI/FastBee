package com.fastbee.protocol.base.model;

import com.fastbee.protocol.base.struc.BaseStructure;
import com.fastbee.protocol.base.struc.LengthStructure;
import com.fastbee.protocol.util.CharsBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author gsb
 * @date 2022/11/9 17:39
 */
public class StringModel {
    public static final WModel<String> HEX = new HEX(-1);
    public static final WModel<String> BCD = new BCD(-1);
    public static final WModel<String> GBK = new STR(Charset.forName("GBK"), -1);
    public static final WModel<String> UTF8 = new STR(StandardCharsets.UTF_8, -1);
    public static final WModel<String> ASCII = new STR(StandardCharsets.US_ASCII, -1);

    public static BaseStructure<String> getInstance(String charset, int length, int lengthUnit) {
        final String cs = charset.toUpperCase();
        BaseStructure<String> model;
        if ("BCD".equals(cs)) {
            model = new BCD(length);
        } else if ("HEX".equals(cs)) {
            model = new HEX(length);
        } else {
            model = new STR(Charset.forName(charset), length);
        }

        if (lengthUnit > 0) {
            model = new LengthStructure<>(model, lengthUnit);
        }

        return model;
    }

    public static class STR extends BaseStructure<String> {
        private static final ByteBuffer EMPTY = ByteBuffer.allocate(0);
        private final Charset charset;
        private final int length;
        private final boolean fixed;

        private STR(Charset charset, int length) {
            this.charset = charset;
            this.length = length;
            this.fixed = length > -1;
        }

        @Override
        public String readFrom(ByteBuf input) {
            int len = input.readableBytes();

            if (fixed && len > length)
                len = length;

            byte[] bytes = new byte[len];
            input.readBytes(bytes);

            int st = 0;
            while ((st < len) && (bytes[st] == 0)) {
                st++;
            }
            while ((st < len) && (bytes[len - 1] == 0)) {
                len--;
            }
            return new String(bytes, st, len - st, charset);
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            if (fixed) {
                ByteBuffer buffer;
                if (value == null) {
                    buffer = EMPTY;
                } else {
                    buffer = charset.encode(value);
                }

                int srcPos = length - buffer.limit();

                if (srcPos > 0) {
                    output.writeBytes(buffer);
                    output.writeBytes(new byte[srcPos]);
                } else if (srcPos < 0) {
                    buffer.position(-srcPos);
                    output.writeBytes(buffer);
                } else {
                    output.writeBytes(buffer);
                }
            } else {
                if (value != null) {
                    output.writeBytes(charset.encode(value));
                }
            }
        }
    }

    public static class HEX extends BaseStructure<String> {
        protected final int length;
        protected final int charSize;
        protected final boolean fixed;

        public HEX(int length) {
            this.length = length;
            this.charSize = length << 1;
            this.fixed = length > -1;
        }

        @Override
        public String readFrom(ByteBuf input) {
            return readCharsBuilder(input).toString();
        }

        protected CharsBuilder readCharsBuilder(ByteBuf input) {
            int len = fixed ? length : input.readableBytes();
            byte[] bytes = new byte[len];
            input.readBytes(bytes);

            CharsBuilder cb = new CharsBuilder(charSize);
            StringUtil.toHexStringPadded(cb, bytes);
            return cb;
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            if (value == null) {
                if (fixed) {
                    output.writeBytes(new byte[length]);
                }
                return;
            }

            int charSize = this.charSize;
            int strLength = value.length();

            if (!fixed) {
                charSize = strLength + (strLength & 1);
            }

            char[] chars = new char[charSize];
            int i = charSize - strLength;
            if (i >= 0) {
                value.getChars(0, charSize - i, chars, i);
                while (i > 0) {
                    chars[--i] = '0';
                }
            } else {
                value.getChars(-i, charSize - i, chars, 0);
            }
            byte[] src = StringUtil.decodeHexDump(new CharsBuilder(chars));
            output.writeBytes(src);
        }
    }

    public static class BCD extends HEX {
        public BCD(int length) {
            super(length);
        }

        @Override
        public String readFrom(ByteBuf input) {
            return readCharsBuilder(input).leftStrip('0');
        }
    }
}
