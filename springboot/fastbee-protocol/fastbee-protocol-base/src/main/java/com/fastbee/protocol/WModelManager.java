package com.fastbee.protocol;

import com.fastbee.protocol.base.annotation.Protocol;
import com.fastbee.protocol.base.model.ActiveModel;
import com.fastbee.protocol.util.ArrayMap;
import com.fastbee.protocol.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息架构管理类
 *
 * @author bill
 */
public class WModelManager {

    private final Map<Integer, ArrayMap<ActiveModel>> typeIdMapping;

    private final Map<String, ArrayMap<ActiveModel>> typeClassMapping;

    public WModelManager() {
        this(128);
    }

    public WModelManager(int initialCapacity) {
        this.typeIdMapping = new HashMap<>(initialCapacity);
        this.typeClassMapping = new HashMap<>(initialCapacity);
    }

    public WModelManager(String... basePackages) {
        this(256, basePackages);
    }

    public WModelManager(int initialCapacity, String... basePackages) {
        this(initialCapacity);
        for (String basePackage : basePackages) {
            List<Class> types = ClassUtils.getClassList(basePackage);
            for (Class<?> type : types) {
                Protocol protocol = type.getAnnotation(Protocol.class);
                if (protocol != null) {
                    int[] values = protocol.value();
                    for (Integer typeId : values) {
                        loadRuntimeSchema(typeId, type);
                    }
                }
            }
        }
    }

    public void loadRuntimeSchema(Integer typeId, Class typeClass) {
        ArrayMap<ActiveModel> schemaMap = ProtocolLoadUtils.getActiveMap(typeClassMapping, typeClass);
        if (schemaMap != null) {
            typeIdMapping.put(typeId, schemaMap);
        }
    }

    public <T> ActiveModel<T> getActiveMap(Class<T> typeClass, int version) {
        ArrayMap<ActiveModel> schemaMap = ProtocolLoadUtils.getActiveMap(typeClassMapping, typeClass);
        if (schemaMap == null) {
            return null;
        }
        return schemaMap.getOrDefault(version);
    }

    public ArrayMap<ActiveModel> getActiveMap(Class typeClass) {
        return ProtocolLoadUtils.getActiveMap(typeClassMapping, typeClass);
    }

    public ActiveModel getActiveMap(Integer typeId, int version) {
        ArrayMap<ActiveModel> schemaMap = typeIdMapping.get(typeId);
        if (schemaMap == null) {
            return null;
        }
        return schemaMap.getOrDefault(version);
    }

    public ArrayMap<ActiveModel> getActiveMap(Integer typeId) {
        return typeIdMapping.get(typeId);
    }
}