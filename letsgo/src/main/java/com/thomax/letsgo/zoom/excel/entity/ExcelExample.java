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
    @ImportColumn(name = "数值(byte)", templateIndex = 1, templateExample = "123")
    private Byte byteCell;

    @ExportColumn(name = "数值(int)", index = 2)
    @ImportColumn(name = "数值(int)", templateIndex = 2, templateExample = "123456")
    private int intCell;

    @ExportColumn(name = "数值(long)", index = 3)
    @ImportColumn(name = "数值(long)", templateIndex = 3, templateExample = "123456789")
    private Long longCell;

    @ExportColumn(name = "小数(double)", index = 4, decimalLength = 3)
    @ImportColumn(name = "小数(double)", templateIndex = 4, templateExample = "123456.789")
    private double doubleCell;

    @ExportColumn(name = "小数(big decimal)", index = 6, decimalLength = 4, width = 25)
    @ImportColumn(name = "小数(big decimal)", templateIndex = 5, templateWidth = 25, templateExample = "123.456789")
    private BigDecimal bigDecimalCell;

    @ExportColumn(name = "日期", index = 7, format = ExcelFormat.DATE)
    @ImportColumn(name = "日期", templateIndex = 6, templateExample = "2022-11-11")
    private Date dateCell;

    @ExportColumn(name = "日期时间", index = 8, format = ExcelFormat.DATETIME)
    @ImportColumn(name = "日期时间", templateIndex = 7, templateExample = "2022-11-11 13:13:13")
    private Date datetimeCell;

    @ExportColumn(name = "字符串", index = 9, width = 40)
    private String stringCell;

    @ExportColumn(name = "超文本链接", format = ExcelFormat.HYPERLINK_FILE, index = 20)
    private String fileCell;

    private static final File FILE = new File("C:\\Users\\Administrator\\Desktop\\123.xlsx");

    private static final File TEMPLATE = new File("C:\\Users\\Administrator\\Desktop\\template.xlsx");

    public static void main(String[] args) throws Exception {
        FileOutputStream fos = new FileOutputStream(FILE);
        ExcelUtils.exportExcel(fos, createData());

        /*FileOutputStream fos2 = new FileOutputStream(FILE);
        ExcelUtils.exportExcel(fos2, createData(), "template.xlsx", 1);*/

        FileInputStream fis = new FileInputStream(FILE);
        List<ExcelExample> list = ExcelUtils.importExcel(fis, ExcelExample.class);
        System.out.println(JSON.toJSONString(list));

        /*FileOutputStream fos3 = new FileOutputStream(TEMPLATE);
        ExcelUtils.downloadTemplate(fos3, ExcelExample.class);*/

       /* FileOutputStream fos4 = new FileOutputStream(TEMPLATE);
        ExcelUtils.downloadTemplate(fos4, "template.xlsx");*/

        System.out.println("操作结束");
    }

    private static List<ExcelExample> createData() {
        ExcelExample excelExample = new ExcelExample();
        excelExample.setByteCell(Byte.MAX_VALUE);
        excelExample.setIntCell(Integer.MAX_VALUE);
        excelExample.setLongCell(Long.MAX_VALUE);
        excelExample.setDoubleCell(12345678901234.1234567d);
        excelExample.setBigDecimalCell(new BigDecimal("1234567890987654321.1234567890987654321"));
        excelExample.setDateCell(DateUtil.parseDate("2022-11-11"));
        excelExample.setDatetimeCell(DateUtil.parseDateTime("2022-11-11 13:13:13"));
        excelExample.setStringCell("群贤毕至，少长咸集。此地有崇山峻岭，茂林修竹，又有清流激湍，映带左右。");
        excelExample.setFileCell("1122.png");

        return Arrays.asList(excelExample, excelExample);
    }

}
