package com.thomax.letsgo.zoom.excel.constant;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Frame里面使用到的国际化常量
 */
public class I18nConstants {

    private static final Map<String, String> CN_MAP = new HashMap<>();

    private static final Map<String, String> EN_MAP = new HashMap<>();

    private static final Map<String, String> TW_MAP = new HashMap<>();

    //ExcelUtils工具类使用的国际化提示语
    public static final String EXCEL_UTIL_NOTICE1 = "excel.util.notice1";
    public static final String EXCEL_UTIL_NOTICE2 = "excel.util.notice2";
    public static final String EXCEL_UTIL_NOTICE3 = "excel.util.notice3";
    public static final String EXCEL_UTIL_NOTICE4 = "excel.util.notice4";

    static {
        CN_MAP.put(EXCEL_UTIL_NOTICE1, "无数据");
        CN_MAP.put(EXCEL_UTIL_NOTICE2, "第[1]行数据的列【[2]】不能为空");
        CN_MAP.put(EXCEL_UTIL_NOTICE3, "第[1]行数据的列【[2]】格式不正确或超出范围");
        CN_MAP.put(EXCEL_UTIL_NOTICE4, "请稍后重试");

        EN_MAP.put(EXCEL_UTIL_NOTICE1, "No data");
        EN_MAP.put(EXCEL_UTIL_NOTICE2, "Column [[2]] of data in row [1] cannot be empty");
        EN_MAP.put(EXCEL_UTIL_NOTICE3, "Column [[2]] of data in row [1] is in incorrect format or out of range");
        EN_MAP.put(EXCEL_UTIL_NOTICE4, "Please try again later");

        TW_MAP.put(EXCEL_UTIL_NOTICE1, "無數據");
        TW_MAP.put(EXCEL_UTIL_NOTICE2, "第[1]行數據的列【[2]】不能為空");
        TW_MAP.put(EXCEL_UTIL_NOTICE3, "第[1]行數據的列【[2]】格式不正確或超出範圍");
        TW_MAP.put(EXCEL_UTIL_NOTICE4, "請稍後重試");
    }

    public static String getMessage(String key) {
        switch(LocaleContextHolder.getLocale().getLanguage()){
            case "zh_tw":
                return TW_MAP.get(key);
            case "en":
                return EN_MAP.get(key);
            default:
                return CN_MAP.get(key);
        }
    }

}
