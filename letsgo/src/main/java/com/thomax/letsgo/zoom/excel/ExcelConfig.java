package com.thomax.letsgo.zoom.excel;

import lombok.Data;

@Data
public class ExcelConfig {

    /**
     * 实体类属性名
     */
    private String prop;

    /**
     * 实体类属性的类型
     */
    private Class<?> type;

    /**
     * 实体类属性的注解
     */
    private ExcelColumn column;

}
