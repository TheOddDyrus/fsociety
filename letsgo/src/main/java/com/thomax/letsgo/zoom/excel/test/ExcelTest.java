package com.thomax.letsgo.zoom.excel.test;

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
public class ExcelTest {

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
        ExcelUtils.exportExcel(fos2, Arrays.asList(excelExample, excelExample), "template.xlsx", 1);*/

        FileInputStream fis = new FileInputStream(FILE);
        List<ExcelTest> list = ExcelUtils.importExcel(fis, ExcelTest.class, true);
        System.out.println("读取到的Excel数据1:" + JSON.toJSONString(list));
        FileInputStream fis2 = new FileInputStream(FILE);
        List<ExcelTest> list2 = ExcelUtils.importExcel(fis2, ExcelTest.class, false);
        System.out.println("读取到的Excel数据2:" + JSON.toJSONString(list2));

        FileOutputStream fos3 = new FileOutputStream(TEMPLATE);
        ExcelUtils.downloadTemplate(fos3, ExcelTest.class);

        /*FileOutputStream fos4 = new FileOutputStream(TEMPLATE);
        ExcelUtils.downloadTemplate(fos4, "template.xlsx");*/

        System.out.println("============Excel测试结束============");
    }

    private static List<ExcelTest> createData() {
        ExcelTest excelTest = new ExcelTest();
        excelTest.setByteCell(Byte.MAX_VALUE);
        excelTest.setIntCell(Integer.MAX_VALUE);
        excelTest.setLongCell(Long.MAX_VALUE);
        excelTest.setDoubleCell(12345678901234.1234567d);
        excelTest.setBigDecimalCell(new BigDecimal("1234567890987654321.1234567890987654321"));
        excelTest.setDateCell(DateUtil.parseDate("2022-11-11"));
        excelTest.setDatetimeCell(DateUtil.parseDateTime("2022-11-11 13:13:13"));
        excelTest.setStringCell("群贤毕至，少长咸集。此地有崇山峻岭，茂林修竹，又有清流激湍，映带左右。");
        excelTest.setFileCell("1122.png");

        return Arrays.asList(excelTest, excelTest);
    }

}
