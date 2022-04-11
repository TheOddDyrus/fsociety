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
     * 国际化的Key
     */
    String i18nKey() default "";

    /**
     * 是否支持为空
     */
    boolean enableEmpty() default false;

    /**
     * 格式校验（正则表达式）
     */
    String format() default "";

    /**
     * 导入模板字段顺序
     */
    int templateIndex() default 0;

    /**
     * 导入模板单元格宽度
     */
    int templateWidth() default 20;

    /**
     * 导入模板中第一条数据的值
     */
    String templateExample() default "";

}
