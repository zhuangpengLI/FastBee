package com.fastbee.protocol.base.struc;

import com.fastbee.protocol.base.model.WModel;
import com.fastbee.protocol.util.ExplainUtils;
import com.fastbee.protocol.util.IntTool;
import com.fastbee.protocol.util.Msg;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 每个元素长度单位的集合域，位于消息末尾
 * @author bill
 */
public class LengthUnitCollectionStructure<T> extends BaseStructure<Collection<T>> {

    private final WModel<T> model;
    private final int lengthUnit;
    private final IntTool intTool;


    public LengthUnitCollectionStructure(WModel<T> model, int lengthUnit) {
        this.model = model;
        this.lengthUnit = lengthUnit;
        this.intTool = IntTool.getInstance(lengthUnit);
    }

    @Override
    public Collection<T> readFrom(ByteBuf input) {
        Collection list = new ArrayList<>();
        while (input.isReadable()) {
            int length = intTool.read(input);
            T t = model.readFrom(input, length);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list) {
        if (list != null) {
            for (T t : list) {
                if (t != null) {
                    int begin = output.writerIndex();
                    intTool.write(output, 0);
                    model.writeTo(output, t);
                    int length = output.writerIndex() - begin - lengthUnit;
                    intTool.set(output, begin, length);
                }
            }
        }
    }

    @Override
    public Collection<T> readFrom(ByteBuf input, ExplainUtils explain) {
        Collection list = new ArrayList<>();
        while (input.isReadable()) {
            int length = intTool.read(input);
            explain.lengthField(input.readerIndex() - lengthUnit, description + "长度", length, lengthUnit);
            T t = model.readFrom(input, length, explain);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list, ExplainUtils explain) {
        if (list != null) {
            for (T t : list) {
                if (t != null) {
                    int begin = output.writerIndex();
                    Msg msg = explain.lengthField(begin, description + "长度", 0, lengthUnit);
                    intTool.write(output, 0);
                    model.writeTo(output, t, explain);
                    int length = output.writerIndex() - begin - lengthUnit;
                    intTool.set(output, begin, length);
                    msg.setLength(length, lengthUnit);
                }
            }
        }
    }
}
