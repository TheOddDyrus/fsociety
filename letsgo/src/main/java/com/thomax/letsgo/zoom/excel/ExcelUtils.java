package com.thomax.letsgo.zoom.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Excel工具（支持的实体类数据类型：8个基础类型与包装类、java.util.Date、java.math.BigDecimal）
 *
 * @see ExcelColumn 注解说明：
 * 导出：
 * @see ExcelColumn#format() 实体类中的日期字段必须指定此属性
 * @see ExcelColumn#exportDecimalLength() 自定义保留位数必须指定此属性
 */
public class ExcelUtils {

    private static final File FILE = new File("C:\\Users\\Administrator\\Desktop\\123.xlsx");

    public static void main(String[] args) throws FileNotFoundException {
        ExcelExample excelExample = new ExcelExample();
        excelExample.setByteCell(Byte.MAX_VALUE);
        excelExample.setIntCell(Integer.MAX_VALUE);
        excelExample.setLongCell(Long.MAX_VALUE);
        excelExample.setDoubleCell(12345678901234.1234567d);
        excelExample.setBigDecimalCell(new BigDecimal("1234567890987654321.1234567890987654321"));
        excelExample.setDateCell(DateUtil.parseDate("2022-11-11"));
        excelExample.setDatetimeCell(DateUtil.parseDateTime("2022-11-11 13:13:13"));
        excelExample.setStringCell("豫章故郡，洪都新府。星分翼轸，地接衡庐。襟三江而带五湖，控蛮荆而引瓯越");

        /*FileOutputStream fos = new FileOutputStream(FILE);
        ExcelUtils.writeExcel(fos, Arrays.asList(excelExample, excelExample));*/

        FileInputStream fis = new FileInputStream(FILE);
        List<ExcelExample> list = null;
        try {
            list = ExcelUtils.readExcel(fis, ExcelExample.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ExcelWriter writer = ExcelUtil.getWriter(true).disableDefaultStyle();

        //创建自定义单元格格式
        CellStyle strStyle = createCellStyle(writer, "TEXT"); //TEXT或@都是指定文本类型

        //设置列
        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列名
            writer.writeCellValue(i, 0, config.getColumn().name());
            //设置列名的单元格格式格式
            writer.setStyle(strStyle, i, 0);
            //设置列宽度
            writer.setColumnWidth(i, config.getColumn().width());
        }

        //设置列的数据的单元格格式和数据
        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            if (ExcelFormat.NONE.equals(config.getColumn().format())) {
                int length = config.getColumn().exportDecimalLength();
                if (length > 0) {
                    String pre = "#.";
                    String format2 = StrUtil.fillAfter(pre, '#', length + pre.length());
                    writeCell4Decimal(writer, list, config, i, format2);
                } else {
                    writeCell(writer, list, config, i);
                }
            } else if (ExcelFormat.DATE.equals(config.getColumn().format())) {
                writeCell4Date(writer, list, config, i);
            } else if (ExcelFormat.DATETIME.equals(config.getColumn().format())) {
                writeCell4DateTime(writer, list, config, i);
            }
            //设置列的数据的单元格格式
            setCellStyle(writer, strStyle, i, list.size());
        }

        writer.flush(outputStream);
        writer.close();
    }

    private static void writeCell(ExcelWriter writer, List<?> list, ExcelConfig config, int index) {
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            Map<String, Object> map = BeanUtil.beanToMap(obj);

            writer.writeCellValue(index, i + 1, StrUtil.toStringOrNull(map.get(config.getProp())));
        }
    }

    private static void writeCell4Decimal(ExcelWriter writer, List<?> list, ExcelConfig config, int index, String format) {
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            Map<String, Object> map = BeanUtil.beanToMap(obj);
            writer.writeCellValue(index, i + 1, NumberUtil.decimalFormat(format, map.get(config.getProp())));
        }
    }

    private static void writeCell4Date(ExcelWriter writer, List<?> list, ExcelConfig config, int index) {
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            Map<String, Object> map = BeanUtil.beanToMap(obj);
            writer.writeCellValue(index, i + 1, DateUtil.format((Date) map.get(config.getProp()), "yyyy-MM-dd"));
        }
    }

    private static void writeCell4DateTime(ExcelWriter writer, List<?> list, ExcelConfig config, int index) {
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            Map<String, Object> map = BeanUtil.beanToMap(obj);
            writer.writeCellValue(index, i + 1, DateUtil.format((Date) map.get(config.getProp()), "yyyy-MM-dd HH:mm:ss"));
        }
    }

    private static CellStyle createCellStyle(ExcelWriter writer, String format) {
        CellStyle cellStyle = writer.createCellStyle();
        cellStyle.setDataFormat(writer.getWorkbook().createDataFormat().getFormat(format));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        Font font = writer.createFont();
        font.setFontName("宋体");
        cellStyle.setFont(font);

        return cellStyle;
    }

    private static void setCellStyle(ExcelWriter writer, CellStyle dateTimeStyle, int index, int total) {
        for (int i = 0; i < total; i++) {
            writer.setStyle(dateTimeStyle, index, i + 1);
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
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> type) throws Exception {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<ExcelConfig> excelConfigList = getExcelConfig(type);
        excelConfigList.forEach((o) -> reader.addHeaderAlias(o.getColumn().name(), o.getProp()));

        List<Map<String, Object>> dataList = reader.readAll();

        List<T> list = new ArrayList<>(dataList.size());

        //检查数据
        for (Map<String, Object> map : dataList) {
            for (ExcelConfig config : excelConfigList) {

                Object obj = map.get(config.getProp());
                //检查是否为空
                if (!config.getColumn().importEmpty() && obj == null) {
                    throw new Exception("不能为空或格式不正确");
                }
                //检查是否匹配正则表达式
                if (obj != null) {
                    if (ExcelFormat.NONE.equals(config.getColumn().format())) {
                        String format = config.getColumn().importFormat();
                        if (StrUtil.isNotEmpty(format)) {
                            if (!ReUtil.isMatch(format, String.valueOf(obj))) {
                                throw new Exception("格式不正确");
                            }
                        }
                    }
                }

                //BeanUtil.mapToBean(map, type, );
            }
        }


        return list;
    }

}
