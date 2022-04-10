package com.thomax.letsgo.zoom.excel.entity;

import com.thomax.letsgo.zoom.excel.annotation.ExportColumn;
import com.thomax.letsgo.zoom.excel.annotation.ImportColumn;
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
     * 实体类属性的导出注解
     */
    private ExportColumn exportColumn;

    /**
     * 实体类属性的导入注解
     */
    private ImportColumn importColumn;

}
