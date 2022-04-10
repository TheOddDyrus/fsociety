package com.thomax.letsgo.zoom.excel.annotation;

import com.thomax.letsgo.zoom.excel.constant.ExcelFormat;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportColumn {

    /**
     * 列名
     */
    String name() default "未命名";

    /**
     * 序号
     */
    int index() default 0;

    /**
     * 单元格宽度
     */
    int width() default 20;

    /**
     * 单元格格式
     */
    ExcelFormat format() default ExcelFormat.NONE;

    /**
     * 保留小数的位数
     */
    int decimalLength() default 0;

}
