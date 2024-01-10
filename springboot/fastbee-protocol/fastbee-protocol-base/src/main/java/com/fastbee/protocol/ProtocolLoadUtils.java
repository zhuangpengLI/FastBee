package com.fastbee.protocol;

import com.fastbee.protocol.base.annotation.Column;
import com.fastbee.protocol.base.annotation.Columns;
import com.fastbee.protocol.base.annotation.MergeSubClass;
import com.fastbee.protocol.base.model.ActiveModel;
import com.fastbee.protocol.base.model.ModelRegistry;
import com.fastbee.protocol.base.model.WModel;
import com.fastbee.protocol.base.struc.BaseStructure;
import com.fastbee.protocol.util.ArrayMap;
import com.fastbee.protocol.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 消息架构加载
 * @author bill
 */
public class ProtocolLoadUtils {
    private static final Map<String, ArrayMap<ActiveModel>> CACHE = new WeakHashMap<>();

    public static ArrayMap<ActiveModel> getActiveMap(Class typeClass) {
        return getActiveMap(CACHE, typeClass);
    }

    public static ActiveModel getActiveMap(Class typeClass, int version) {
        ArrayMap<ActiveModel> schemaMap = getActiveMap(CACHE, typeClass);
        if (schemaMap == null) return null;
        return schemaMap.getOrDefault(version);
    }

    public static ArrayMap<ActiveModel> getActiveMap(Map<String, ArrayMap<ActiveModel>> root, final Class typeClass) {
        ArrayMap<ActiveModel> schemaMap = root.get(typeClass.getName());
        //不支持循环引用
        if (schemaMap != null) return schemaMap;

        List<Field> fs = findFields(typeClass);
        if (fs.isEmpty()) return null;

        root.put(typeClass.getName(), schemaMap = new ArrayMap<>());

        Map<Integer, Set<BaseStructure>> multiVersionFields = findMultiVersionFields(root, fs);
        Set<BaseStructure> defFields = multiVersionFields.get(Integer.MAX_VALUE);
        for (Map.Entry<Integer, Set<BaseStructure>> entry : multiVersionFields.entrySet()) {

            Integer version = entry.getKey();
            Set<BaseStructure> fieldList = entry.getValue();
            if (defFields != null && !version.equals(Integer.MAX_VALUE)) {
                for (BaseStructure defField : defFields) {
                    if (!fieldList.contains(defField))
                        fieldList.add(defField);
                }
            }

            BaseStructure[] fields = fieldList.toArray(new BaseStructure[fieldList.size()]);
            Arrays.sort(fields);

            ActiveModel schema = new ActiveModel(typeClass, version, fields);
            schemaMap.put(version, schema);
        }
        root.put(typeClass.getName(), schemaMap.fillDefaultValue());
        return schemaMap;
    }

    private static List<Field> findFields(Class typeClass) {
        LinkedList<Field> fs = new LinkedList<>();

        boolean addFirst = false;
        Class<?> temp = typeClass;

        while (temp != null) {
            if (addFirst)
                fs.addAll(0, Arrays.asList(temp.getDeclaredFields()));
            else
                fs.addAll(Arrays.asList(temp.getDeclaredFields()));
            MergeSubClass marge = temp.getAnnotation(MergeSubClass.class);
            if (marge == null)
                break;
            addFirst = marge.addBefore();
            temp = typeClass.getSuperclass();
        }

        List<Field> result = new ArrayList<>(fs.size());
        for (Field f : fs) {
            if (f.isAnnotationPresent(Columns.class) || f.isAnnotationPresent(Column.class)) {
                f.setAccessible(true);
                result.add(f);
            }
        }
        return result;
    }

    private static Map<Integer, Set<BaseStructure>> findMultiVersionFields(Map<String, ArrayMap<ActiveModel>> root, List<Field> fs) {
        final int size = fs.size();
        Map<Integer, Set<BaseStructure>> multiVersionFields = new TreeMap<Integer, Set<BaseStructure>>() {
            @Override
            public Set<BaseStructure> get(Object key) {
                Set result = super.get(key);
                if (result == null) super.put((Integer) key, result = new HashSet(size));
                return result;
            }
        };

        for (int i = 0; i < size; i++) {
            Field f = fs.get(i);

            Column column = f.getDeclaredAnnotation(Column.class);
            if (column != null) {
                fillField(root, multiVersionFields, column, f, i);
            } else {
                Column[] clos = f.getDeclaredAnnotation(Columns.class).value();
                for (int j = 0; j < clos.length; j++)
                    fillField(root, multiVersionFields, clos[j], f, i);
            }
        }
        return multiVersionFields;
    }

    private static void fillField(Map<String, ArrayMap<ActiveModel>> root, Map<Integer, Set<BaseStructure>> multiVersionFields, Column column, Field field, int position) {
        BaseStructure BaseStructure = ModelRegistry.get(column, field);
        int[] versions = getVersions(column, ALL);
        if (BaseStructure != null) {
            for (int ver : versions) {
                multiVersionFields.get(ver).add(BaseStructure.init(column, field, position));
            }
        } else {
            ArrayMap<ActiveModel> modelMap = getActiveMap(root, ClassUtils.getGenericType(field));
            if (versions == ALL)
                versions = modelMap.keys();
            for (int ver : versions) {
                WModel model = modelMap.getOrDefault(ver);
                BaseStructure = ModelRegistry.get(column, field, model);
                multiVersionFields.get(ver).add(BaseStructure.init(column, field, position));
            }
        }
    }

    private static final int[] ALL = {Integer.MAX_VALUE};

    private static int[] getVersions(Column column, int[] def) {
        int[] result = column.version();
        if (result.length == 0)
            result = def;
        return result;
    }
}
