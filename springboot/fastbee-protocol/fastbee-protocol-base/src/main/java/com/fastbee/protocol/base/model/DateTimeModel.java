package com.fastbee.protocol.base.model;

import com.fastbee.protocol.base.struc.BaseStructure;
import com.fastbee.protocol.util.DateTool;
import io.netty.buffer.ByteBuf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author gsb
 * @date 2022/11/9 17:35
 */
public class DateTimeModel {

    public static final WModel<LocalTime> BYTE_TIME = new Time(DateTool.BYTE);
    public static final WModel<LocalDate> BYTE_DATE = new Date(DateTool.BYTE);
    public static final WModel<LocalDateTime> BYTE_DATETIME = new DateTime(DateTool.BYTE);

    public static final WModel<LocalTime> BCD_TIME = new Time(DateTool.BCD);
    public static final WModel<LocalDate> BCD_DATE = new Date(DateTool.BCD);
    public static final WModel<LocalDateTime> BCD_DATETIME = new DateTime(DateTool.BCD);

    protected static class DateTime extends BaseStructure<LocalDateTime> {
        protected final DateTool tool;

        protected DateTime(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[6];
            input.readBytes(bytes);
            return tool.toDateTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDateTime value) {
            output.writeBytes(tool.from(value));
        }
    }

    protected static class Date extends BaseStructure<LocalDate> {
        protected final DateTool tool;

        protected Date(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDate readFrom(ByteBuf input) {
            byte[] bytes = new byte[3];
            input.readBytes(bytes);
            return tool.toDate(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDate value) {
            output.writeBytes(tool.from(value));
        }
    }

    protected static class Time extends BaseStructure<LocalTime> {
        protected final DateTool tool;

        protected Time(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[3];
            input.readBytes(bytes);
            return tool.toTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalTime value) {
            output.writeBytes(tool.from(value));
        }
    }
}
