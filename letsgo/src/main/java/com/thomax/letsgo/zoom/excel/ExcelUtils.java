package com.thomax.letsgo.zoom.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Excel工具（支持的实体类数据类型：8个基础类型与包装类、java.util.Date、java.math.BigDecimal）
 *
 * @see ExcelColumn 注解说明：
 * @see ExcelColumn#exportFormat() 实体类中的日期字段必须指定此属性
 * @see ExcelColumn#exportDecimalLength() 浮点型默认保留2位小数，自定义时请指定此属性
 */
public class ExcelUtils {

    private static final File FILE = new File("C:\\Users\\Administrator\\Desktop\\123.xlsx");

    public static void main(String[] args) throws FileNotFoundException {
        ExcelExample excelExample = new ExcelExample();
        excelExample.setByteCell(Byte.MAX_VALUE);
        excelExample.setIntCell(Integer.MAX_VALUE);
        excelExample.setLongCell(Long.MAX_VALUE);
        excelExample.setDoubleCell1(123.123456d);
        excelExample.setDoubleCell2(Double.MAX_VALUE);
        excelExample.setBigDecimalCell(BigDecimal.valueOf(Double.MAX_VALUE));
        excelExample.setDateCell(DateUtil.parseDate("2022-11-11"));
        excelExample.setDatetimeCell(DateUtil.parseDateTime("2022-11-11 13:13:13"));
        excelExample.setStringCell("豫章故郡，洪都新府。星分翼轸，地接衡庐。襟三江而带五湖，控蛮荆而引瓯越");

        FileOutputStream fos = new FileOutputStream(FILE);
        ExcelUtils.writeExcel(fos, Arrays.asList(excelExample, excelExample));

        FileInputStream fis = new FileInputStream(FILE);
        List<ExcelExample> list = ExcelUtils.readExcel(fis, ExcelExample.class);
        System.out.println(JSON.toJSONString(list));
    }

    /**
     * 从实体类的注解中读取配置
     *
     * @param type 类型
     */
    private static <T> List<ExcelConfig> getExcelConfig(Class<T> type) {
        List<ExcelConfig> list = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(type);
        for (Field field : fields) {
            ExcelConfig excelConfig = new ExcelConfig();
            excelConfig.setProp(field.getName());
            excelConfig.setType(field.getType());
            excelConfig.setColumn(field.getAnnotation(ExcelColumn.class));
            list.add(excelConfig);
        }

        return CollUtil.sort(list, Comparator.comparingInt(o -> o.getColumn().index()));
    }

     /*--------------------------------------------
     |                 写入Excel                 |
     ============================================*/

    /**
     * 写入Excel
     *
     * @param outputStream 输出流
     * @param list 数据集
     */
    public static void writeExcel(OutputStream outputStream, List<?> list) {
        List<ExcelConfig> excelConfigList = getExcelConfig(list.get(0).getClass());
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //设置全局样式
        Font font = writer.createFont();
        font.setFontName("宋体");
        writer.getStyleSet().setFont(font, false);

        //设置列名与宽度
        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列名
            writer.addHeaderAlias(config.getProp(), config.getColumn().name());
            //设置宽度
            writer.setColumnWidth(i, config.getColumn().width());
        }

        //写入数据
        writer.write(list);

        //创建自定义单元格格式
        CellStyle strStyle = createCellStyle(writer, "TEXT"); //TEXT或@都是指定文本类型
        CellStyle numberStyle = createCellStyle(writer, "0");
        CellStyle dateStyle = createCellStyle(writer, "yyyy-MM-dd");
        CellStyle dateTimeStyle = createCellStyle(writer, "yyyy-MM-dd HH:mm:ss");
        //设置列的数据的单元格格式
        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            if (ExcelFormat.NONE.equals(config.getColumn().exportFormat())) {
                if (Double.class.isAssignableFrom(config.getType()) ||
                        Float.class.isAssignableFrom(config.getType()) ||
                        BigDecimal.class.isAssignableFrom(config.getType())) {
                    int length = config.getColumn().exportDecimalLength();
                    String pre = "0.";
                    String format = StrUtil.fillAfter(pre, '0', length + pre.length());
                    CellStyle decimalStyle = createCellStyle(writer, format);
                    setCellStyle(writer, decimalStyle, i, list.size());
                } else if (Byte.class.isAssignableFrom(config.getType())) {
                    setCellStyle(writer, numberStyle, i, list.size());
                } else {
                    setCellStyle(writer, strStyle, i, list.size());
                }
            } else if (ExcelFormat.DATE.equals(config.getColumn().exportFormat())) {
                setCellStyle(writer, dateStyle, i, list.size());
            } else if (ExcelFormat.DATETIME.equals(config.getColumn().exportFormat())) {
                setCellStyle(writer, dateTimeStyle, i, list.size());
            } else {
                setCellStyle(writer, strStyle, i, list.size());
            }
        }

        writer.flush(outputStream);
        writer.close();
    }

    private static CellStyle createCellStyle(ExcelWriter writer, String format) {
        CellStyle cellStyle = writer.createCellStyle();
        cellStyle.setDataFormat(writer.getWorkbook().createDataFormat().getFormat(format));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        return cellStyle;
    }

    private static void setCellStyle(ExcelWriter writer, CellStyle dateTimeStyle, int index, int total) {
        for (int i = 1; i < total + 1; i++) {
            writer.setStyle(dateTimeStyle, index, i);
        }
    }

    /*--------------------------------------------
     |                 读取Excel                 |
     ============================================*/

    /**
     * 导入Excel
     *
     * @param inputStream 输入流
     * @param type 类型
     */
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> type) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<ExcelConfig> excelConfigList = getExcelConfig(type);
        excelConfigList.forEach((o) -> reader.addHeaderAlias(o.getColumn().name(), o.getProp()));

        return reader.readAll(type);
    }

}
