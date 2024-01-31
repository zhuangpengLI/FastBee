package com.fastbee.protocol.base.annotation;

import java.lang.annotation.*;

/**
 * 将父类字段合并到子类
 * @author bill
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MergeSubClass {

    /**
     * 合并父类属性到当前类属性前
     */
    boolean addBefore() default false;
}
