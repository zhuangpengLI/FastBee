package com.fastbee.protocol.base.struc;

import com.fastbee.protocol.base.model.ModelRegistry;
import com.fastbee.protocol.base.model.WModel;
import com.fastbee.protocol.util.ExplainUtils;
import com.fastbee.protocol.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 *
 * @author bill
 */
public class TotalArrayPrimitiveStructure extends BaseStructure {

    private final WModel model;
    private final int totalUnit;
    private final int valueUnit;
    private final IntTool intTool;

    public TotalArrayPrimitiveStructure(WModel model, int totalUnit, Class arrayClass) {
        this.model = model;
        this.totalUnit = totalUnit;
        this.valueUnit = ModelRegistry.getLength(arrayClass);
        this.intTool = IntTool.getInstance(totalUnit);
    }

    @Override
    public Object readFrom(ByteBuf in) {
        int total = intTool.read(in);
        if (total <= 0) {
            return null;
        }
        int length = valueUnit * (total/2);
        return model.readFrom(in, length);
    }

    @Override
    public void writeTo(ByteBuf out, Object value) {
        if (value == null) {
            intTool.write(out, 0);
        } else {
            int total = Array.getLength(value);
            intTool.write(out, total);
            model.writeTo(out, value);
        }
    }

    @Override
    public Object readFrom(ByteBuf in, ExplainUtils explain) {
        int total = intTool.read(in);
        explain.lengthField(in.readerIndex() - totalUnit, description + "数量", total, totalUnit);
        if (total <= 0)
            return null;
        int length = valueUnit * total;
        return model.readFrom(in, length, explain);
    }

    @Override
    public void writeTo(ByteBuf out, Object value, ExplainUtils explain) {
        if (value == null) {
            explain.lengthField(out.writerIndex(), description + "数量", 0, totalUnit);
            intTool.write(out, 0);
        } else {
            int total = Array.getLength(value);
            explain.lengthField(out.writerIndex(), description + "数量", total, totalUnit);
            intTool.write(out, total);
            model.writeTo(out, value, explain);
        }
    }
}
