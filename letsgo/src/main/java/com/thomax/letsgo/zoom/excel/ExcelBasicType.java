package com.thomax.letsgo.zoom.excel;

import cn.hutool.core.collection.CollUtil;

import java.math.BigDecimal;
import java.util.Set;

public enum ExcelBasicType {

    BYTE(byte.class, Byte.class),
    SHORT(short.class, Short.class),
    INTEGER(int.class, Integer.class),
    LONG(long.class, Long.class),
    FLOAT(float.class, Float.class),
    DOUBLE(double.class, Double.class),
    BOOLEAN(boolean.class, Boolean.class),
    CHARACTER(char.class, Character.class),
    BIGDECIMAL(null, BigDecimal.class);

    private final Class<?> baseClass;

    private final Class<?> wrapperClass;

    ExcelBasicType(Class<?> baseClass, Class<?> wrapperClass) {
        this.baseClass = baseClass;
        this.wrapperClass = wrapperClass;
    }

    public Class<?> getBaseClass() {
        return baseClass;
    }

    public Class<?> getWrapperClass() {
        return wrapperClass;
    }

    private static final Set<ExcelBasicType> NUMBER_SET = CollUtil.newHashSet(BYTE, SHORT, INTEGER, LONG);

    private static final Set<ExcelBasicType> DECIMAL_SET = CollUtil.newHashSet(FLOAT, DOUBLE, BIGDECIMAL);

    private static final Set<ExcelBasicType> OTHER_SET = CollUtil.newHashSet(BOOLEAN, CHARACTER);

    public static ExcelFormat getExcelFormat(Class<?> type) {
        for (ExcelBasicType excelBasicType : NUMBER_SET) {
            if (excelBasicType.getBaseClass() == type || excelBasicType.getWrapperClass() == type) {
                return ExcelFormat.NUMBER;
            }
        }

        for (ExcelBasicType excelBasicType : DECIMAL_SET) {
            if (excelBasicType.getBaseClass() == type || excelBasicType.getWrapperClass() == type) {
                return ExcelFormat.DECIMAL;
            }
        }

        return ExcelFormat.NONE;
    }

}
