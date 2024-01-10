package com.fastbee.protocol.base.struc;

import com.fastbee.protocol.base.model.WModel;
import com.fastbee.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 集合域，位于消息末尾
 * @author bill
 */
@AllArgsConstructor
public class CollectionStructure<T> extends BaseStructure<Collection<T>> {

    private final WModel<T> schema;


    @Override
    public Collection<T> readFrom(ByteBuf input) {
        Collection list = new ArrayList<>();
        while (input.isReadable()) {
            T t = schema.readFrom(input);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list) {
        if (list != null) {
            for (T t : list) {
                schema.writeTo(output, t);
            }
        }
    }

    @Override
    public Collection<T> readFrom(ByteBuf input, ExplainUtils explain) {
        Collection list = new ArrayList<>();
        while (input.isReadable()) {
            T t = schema.readFrom(input, explain);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list, ExplainUtils explain) {
        if (list != null) {
            for (T t : list) {
                schema.writeTo(output, t, explain);
            }
        }
    }
}
