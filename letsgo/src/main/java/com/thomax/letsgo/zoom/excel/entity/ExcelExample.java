package com.thomax.letsgo.zoom.excel.entity;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.thomax.letsgo.zoom.excel.ExcelUtils;
import com.thomax.letsgo.zoom.excel.annotation.ExportColumn;
import com.thomax.letsgo.zoom.excel.annotation.ImportColumn;
import com.thomax.letsgo.zoom.excel.constant.ExcelFormat;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class ExcelExample {

    @ExportColumn(name = "数值(byte)", index = 1)
    @ImportColumn(name = "数值(byte)")
    private Byte byteCell;

    @ExportColumn(name = "数值(int)", index = 2)
    @ImportColumn(name = "数值(int)")
    private int intCell;

    @ExportColumn(name = "数值(long)", index = 3)
    @ImportColumn(name = "数值(long)")
    private Long longCell;

    @ExportColumn(name = "小数(double)", index = 4, decimalLength = 3)
    @ImportColumn(name = "小数(double)")
    private double doubleCell;

    @ExportColumn(name = "小数(big decimal)", index = 6, decimalLength = 4, width = 25)
    @ImportColumn(name = "小数(big decimal)")
    private BigDecimal bigDecimalCell;

    @ExportColumn(name = "日期", index = 7, format = ExcelFormat.DATE)
    @ImportColumn(name = "日期")
    private Date dateCell;

    @ExportColumn(name = "日期时间", index = 8, format = ExcelFormat.DATETIME)
    @ImportColumn(name = "日期时间")
    private Date datetimeCell;

    @ExportColumn(name = "字符串", index = 9, width = 40)
    private String stringCell;

    private static final File FILE = new File("C:\\Users\\Administrator\\Desktop\\123.xlsx");

    public static void main(String[] args) throws Exception {
        ExcelExample excelExample = new ExcelExample();
        excelExample.setByteCell(Byte.MAX_VALUE);
        excelExample.setIntCell(Integer.MAX_VALUE);
        excelExample.setLongCell(Long.MAX_VALUE);
        excelExample.setDoubleCell(12345678901234.1234567d);
        excelExample.setBigDecimalCell(new BigDecimal("1234567890987654321.1234567890987654321"));
        excelExample.setDateCell(DateUtil.parseDate("2022-11-11"));
        excelExample.setDatetimeCell(DateUtil.parseDateTime("2022-11-11 13:13:13"));
        excelExample.setStringCell("群贤毕至，少长咸集。此地有崇山峻岭，茂林修竹，又有清流激湍，映带左右。");

        FileOutputStream fos = new FileOutputStream(FILE);
        ExcelUtils.writeExcel(fos, Arrays.asList(excelExample, excelExample));

        FileInputStream fis = new FileInputStream(FILE);
        List<ExcelExample> list = ExcelUtils.readExcel(fis, ExcelExample.class);
        System.out.println(JSON.toJSONString(list));
    }

}
