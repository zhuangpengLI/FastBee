package com.fastbee.protocol.base.model;

import com.fastbee.protocol.PrepareLoadStore;
import com.fastbee.protocol.base.struc.BaseStructure;
import com.fastbee.protocol.util.IntTool;
import com.fastbee.protocol.util.KeyValuePair;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author bill
 */
@Slf4j
public abstract class MapModel<K,V> extends BaseStructure<Map.Entry<K, V>> {

    public final WModel<K> kwModel;
    public final int lengthUnit;
    public final IntTool intTool;
    public final Map<K, WModel> valueSchema;

    public MapModel(WModel<K> keySchema, int lengthUnit) {
        this.kwModel = keySchema;
        this.lengthUnit = lengthUnit;
        this.intTool = IntTool.getInstance(lengthUnit);
        PrepareLoadStore<K> loadStrategy = new PrepareLoadStore<>();
        addSchemas(loadStrategy);
        this.valueSchema = loadStrategy.build();
    }

    protected abstract void addSchemas(PrepareLoadStore<K> schemaRegistry);

    @Override
    public KeyValuePair<K, V> readFrom(ByteBuf in) {
        K key = kwModel.readFrom(in);
        KeyValuePair<K, V> result = new KeyValuePair<>(key);

        int length = intTool.read(in);
        if (length > 0) {
            int writerIndex = in.writerIndex();
            in.writerIndex(in.readerIndex() + length);

            WModel<V> model = valueSchema.get(key);
            if (model != null) {
                V value = model.readFrom(in, length);
                result.setValue(value);
            } else {
                byte[] bytes = new byte[length];
                in.readBytes(bytes);
                result.setValue((V) bytes);
            }
            in.writerIndex(writerIndex);

        } else if (length < 0) {
            WModel<V> model = valueSchema.get(key);
            if (model != null) {
                V value = model.readFrom(in);
                result.setValue(value);
            } else {
                byte[] bytes = new byte[in.readableBytes()];
                in.readBytes(bytes);
                result.setValue((V) bytes);
            }
        }
        return result;
    }

    @Override
    public void writeTo(ByteBuf out, Map.Entry<K, V> entry) {
        if (entry == null)
            return;
        K key = entry.getKey();
        kwModel.writeTo(out, key);

        WModel model = valueSchema.get(key);
        if (model != null) {
            int begin = out.writerIndex();
            intTool.write(out, 0);

            Object value = entry.getValue();
            if (value != null) {
                model.writeTo(out, value);
                int length = out.writerIndex() - begin - lengthUnit;
                intTool.set(out, begin, length);
            }
        } else {
            log.warn("未注册的信息:ID[{}], Value[{}]", key, entry.getValue());
        }
    }
}