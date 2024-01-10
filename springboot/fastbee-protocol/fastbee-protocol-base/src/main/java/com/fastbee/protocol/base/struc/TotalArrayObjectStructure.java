package com.fastbee.protocol.base.struc;

import com.fastbee.protocol.base.model.WModel;
import com.fastbee.protocol.util.ExplainUtils;
import com.fastbee.protocol.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 * @author bill
 */
public class TotalArrayObjectStructure<T> extends BaseStructure<T[]> {

    private final WModel<T> model;
    private final int totalUnit;
    private final IntTool intTool;
    private final Class<T> arrayClass;

    public TotalArrayObjectStructure(WModel<T> model, int totalUnit, Class<T> arrayClass) {
        this.model = model;
        this.totalUnit = totalUnit;
        this.intTool = IntTool.getInstance(totalUnit);
        this.arrayClass = arrayClass;
    }

    @Override
    public T[] readFrom(ByteBuf in) {
        int total = intTool.read(in);
        if (total <= 0)
            return null;
        T[] value = (T[]) Array.newInstance(arrayClass, total);
        for (int i = 0; i < total; i++) {
            T t = model.readFrom(in);
            value[i] = t;
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf out, T[] value) {
        if (value == null) {
            intTool.write(out, 0);
        } else {
            int length = value.length;
            intTool.write(out, length);
            for (int i = 0; i < length; i++) {
                T t = value[i];
                model.writeTo(out, t);
            }
        }
    }

    @Override
    public T[] readFrom(ByteBuf in, ExplainUtils explain) {
        int total = intTool.read(in);
        explain.lengthField(in.readerIndex() - totalUnit, description + "数量", total, totalUnit);
        if (total <= 0)
            return null;
        T[] value = (T[]) Array.newInstance(arrayClass, total);
        for (int i = 0; i < total; i++) {
            T t = model.readFrom(in, explain);
            value[i] = t;
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf out, T[] value, ExplainUtils explain) {
        if (value == null) {
            explain.lengthField(out.writerIndex(), description + "数量", 0, totalUnit);
            intTool.write(out, 0);
        } else {
            int total = value.length;
            explain.lengthField(out.writerIndex(), description + "数量", total, totalUnit);
            intTool.write(out, total);
            for (int i = 0; i < total; i++) {
                T t = value[i];
                model.writeTo(out, t, explain);
            }
        }
    }
}
