package com.thomax.letsgo.zoom.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

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
     * 导出的单元格保留小数的位数
     */
    int exportDecimalLength() default 0;

    /**
     * 导入的单元格是否可以为空
     */
    boolean importEmpty() default false;

    /**
     * 导入的单元格格式校验（正则表达式）
     */
    String importFormat() default "";

}
