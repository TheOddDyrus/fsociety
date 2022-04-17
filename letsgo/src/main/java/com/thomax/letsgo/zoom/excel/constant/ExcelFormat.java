package com.thomax.letsgo.zoom.excel.constant;

public enum ExcelFormat {

    /**
     * 不进行格式化
     */
    NONE,

    /**
     * 日期类型，格式化样式: yyyy-MM-dd
     */
    DATE,

    /**
     * 日期时间类型，格式化样式: yyyy-MM-dd HH:mm:ss
     */
    DATETIME,

    /**
     * 超文本链接（文件绝对路径）
     */
    HYPERLINK_FILE,

    /**
     * 超文本链接（URL）
     */
    HYPERLINK_URL,

}
