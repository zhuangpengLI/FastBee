package com.fastbee.protocol.base.annotation;

import com.fastbee.protocol.base.model.WModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 报文解析字段注解
 * @author bill
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /** 长度 默认使用类型长度 -1读取剩余报文长度*/
    int length() default -1;

    /** 排序，默认按照model字段顺序编解码*/
    int index() default 0;

    /** 该字段的前置长度 1- BYTE 2-WORD 3.double 4.DWORD */
    int lengthUnit() default -1;

    /** 该字段的前置数量单位 1- BYTE 2-WORD 3.double 4.DWORD*/
    int totalUnit() default -1;

    /** 字符集 HEX ,UTF8 ,GBK, BCD...*/
    String charset() default "HEX";

    /**描述*/
    String desc() default "";

    /** 版本号 ，默认不区分*/
    int[] version() default {};

    /** 自定义报文转换器*/
    Class<? extends WModel> converter() default WModel.class;

}
