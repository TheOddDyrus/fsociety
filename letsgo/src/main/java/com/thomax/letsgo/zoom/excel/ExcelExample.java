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

    @ExcelColumn(name = "小数1(double)", index = 4, exportDecimalLength = 4)
    private double doubleCell1;

    @ExcelColumn(name = "小数2(double)", index = 5, width = 25)
    private double doubleCell2;

    @ExcelColumn(name = "小数3(big decimal)", index = 6, width = 25)
    private BigDecimal bigDecimalCell;

    @ExcelColumn(name = "日期", index = 7, exportFormat = ExcelFormat.DATE)
    private Date dateCell;

    @ExcelColumn(name = "日期时间", index = 8, exportFormat = ExcelFormat.DATETIME)
    private Date datetimeCell;

    @ExcelColumn(name = "字符串", index = 9, width = 40)
    private String stringCell;

}
