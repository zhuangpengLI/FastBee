package com.fastbee.protocol;

import com.fastbee.protocol.base.model.WModel;
import com.fastbee.protocol.util.SingleVersionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author bill
 */
public class PrepareLoadStore<T> {

    private final Map<T, WModel> models = new TreeMap<>();

    public PrepareLoadStore<T> addSchema(T key, WModel schema) {
        models.put(key, schema);
        return this;
    }

    public PrepareLoadStore<T> addSchema(T key, Class typeClass) {
        WModel<Object> model = SingleVersionUtils.getActiveModel(typeClass);
        models.put(key, model);
        return this;
    }

    public Map<T, WModel> build() {
        Map<T, WModel> a = new HashMap<>(models.size());
        a.putAll(models);
        return a;
    }
}
