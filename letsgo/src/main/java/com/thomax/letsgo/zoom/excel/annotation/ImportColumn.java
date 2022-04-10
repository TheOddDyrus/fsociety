package com.thomax.letsgo.zoom.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportColumn {

    /**
     * 列名
     */
    String name() default "";

    /**
     * 是否支持为空
     */
    boolean enableEmpty() default false;

    /**
     * 格式校验（正则表达式）
     */
    String format() default "";

}
