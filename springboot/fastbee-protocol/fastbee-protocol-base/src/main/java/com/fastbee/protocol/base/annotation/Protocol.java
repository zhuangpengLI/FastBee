package com.fastbee.protocol.base.annotation;

import java.lang.annotation.*;

/**
 * 协议类型标注
 * @author bill
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Protocol {

    int[] value() default {};

    String desc() default "";
}
