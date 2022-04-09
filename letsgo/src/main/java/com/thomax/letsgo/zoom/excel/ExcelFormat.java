package com.thomax.letsgo.zoom.excel;

public enum ExcelFormat {

    /**
     * 不进行格式化
     */
    NONE,

    /**
     * 数值类型
     */
    NUMBER,

    /**
     * 浮点类型
     */
    DECIMAL,

    /**
     * 日期类型，格式化样式: yyyy-MM-dd
     */
    DATE,

    /**
     * 日期时间类型，格式化样式: yyyy-MM-dd HH:mm:ss
     */
    DATETIME,

}
