package com.fastbee.protocol.base.model;

import com.fastbee.protocol.base.struc.BaseStructure;
import com.fastbee.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

/**
 * 运行时根据Class生成的消息结构model，序列化对象
 * @author bill
 */
public class ActiveModel<T> implements WModel<T>{

    protected int version;
    protected int length;
    protected Class<T> typeClass;
    protected BaseStructure[] structures;
    protected Constructor<T> constructor;

    public ActiveModel(Class<T> typeClass, int version, BaseStructure[] structures) {
        this.typeClass = typeClass;
        this.version = version;
        this.structures = structures;
        int length = 0;
        for (BaseStructure structure : structures)
            length += structure.length();
        this.length = length;
        try {
            this.constructor = typeClass.getDeclaredConstructor((Class[]) null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T newInstance() {
        try {
            return constructor.newInstance((Object[]) null);
        } catch (Exception e) {
            throw new RuntimeException("newInstance failed " + typeClass.getName(), e);
        }
    }

    public T mergeFrom(ByteBuf input, T result) {
        int i = 0;
        try {
            for (; i < structures.length; i++) {
                structures[i].readAndSet(input, result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    public T mergeFrom(ByteBuf input, T result, ExplainUtils explain) {
        int i = 0;
        try {
            if (explain == null)
                for (; i < structures.length; i++)
                    structures[i].readAndSet(input, result);
            else
                for (; i < structures.length; i++)
                    structures[i].readAndSet(input, result, explain);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    @Override
    public T readFrom(ByteBuf input) {
        int i = 0;
        try {
            T result = constructor.newInstance((Object[]) null);
            for (; i < structures.length; i++) {
                structures[i].readAndSet(input, result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    @Override
    public T readFrom(ByteBuf input, ExplainUtils explain) {
        int i = 0;
        try {
            T result = constructor.newInstance((Object[]) null);
            if (explain == null) {
                for (; i < structures.length; i++) {
                    structures[i].readAndSet(input, result);
                }
            } else {
                for (; i < structures.length; i++) {
                    structures[i].readAndSet(input, result, explain);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    @Override
    public void writeTo(ByteBuf output, T message) {
        int i = 0;
        try {
            for (; i < structures.length; i++) {
                structures[i].getAndWrite(output, message);
            }
        } catch (Exception e) {
            throw new RuntimeException("Write failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    @Override
    public void writeTo(ByteBuf output, T message, ExplainUtils explain) {
        int i = 0;
        try {
            if (explain == null) {
                for (; i < structures.length; i++) {
                    structures[i].getAndWrite(output, message);
                }
            } else {
                for (; i < structures.length; i++) {
                    structures[i].getAndWrite(output, message, explain);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Write failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    public Class<T> typeClass() {
        return typeClass;
    }

    public int version() {
        return version;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(48);
        sb.append("{typeClass=").append(typeClass.getSimpleName());
        sb.append(", version=").append(version);
        sb.append(", length=").append(length);
        sb.append('}');
        return sb.toString();
    }
}
