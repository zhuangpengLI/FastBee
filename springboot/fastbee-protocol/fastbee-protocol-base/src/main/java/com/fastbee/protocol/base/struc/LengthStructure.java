package com.fastbee.protocol.base.struc;

import com.fastbee.protocol.base.model.WModel;
import com.fastbee.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

/**
 * 指定长度报文结构
 * @author bill
 */
@AllArgsConstructor
public class LengthStructure<T> extends BaseStructure<T> {

    private final WModel<T> schema;
    private final int length;

    @Override
    public T readFrom(ByteBuf input) {
        if (input.isReadable(length))
            return schema.readFrom(input, length);
        return null;
    }

    @Override
    public void writeTo(ByteBuf output, T value) {
        if (value != null)
            schema.writeTo(output, length, value);
    }

    @Override
    public T readFrom(ByteBuf input, ExplainUtils explain) {
        if (input.isReadable(length))
            return schema.readFrom(input, length, explain);
        return null;
    }

    @Override
    public void writeTo(ByteBuf output, T value, ExplainUtils explain) {
        if (value != null)
            schema.writeTo(output, length, value, explain);
    }
}
