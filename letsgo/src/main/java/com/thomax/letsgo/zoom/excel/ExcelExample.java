package com.thomax.letsgo.zoom.excel;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExcelExample {

    @ExcelColumn(name = "数值(byte)", index = 1)
    private byte byteCell;

    @ExcelColumn(name = "数值(int)", index = 2)
    private int intCell;

    @ExcelColumn(name = "数值(long)", index = 3)
    private long longCell;

    @ExcelColumn(name = "小数(double)", index = 4, exportDecimalLength = 3)
    private double doubleCell;

    @ExcelColumn(name = "小数(big decimal)", index = 6, exportDecimalLength = 4, width = 25)
    private BigDecimal bigDecimalCell;

    @ExcelColumn(name = "日期", index = 7, format = ExcelFormat.DATE)
    private Date dateCell;

    @ExcelColumn(name = "日期时间", index = 8, format = ExcelFormat.DATETIME)
    private Date datetimeCell;

    @ExcelColumn(name = "字符串", index = 9, width = 40)
    private String stringCell;

}
