package com.thomax.letsgo.zoom.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel工具（支持的数据类型：8个基础类型与包装类、java.util.Date、java.math.BigDecimal）
 *
 * @see ExcelColumn 注解说明：
 * 导出：
 * @see ExcelColumn#format() 实体类中的日期字段必须设置此属性
 * @see ExcelColumn#exportDecimalLength() 自定义保留小数位数必须设置此属性
 * 导入：
 * @see ExcelColumn#importName() 导入时默认使用name()作为列名，自定义列名必须设置此属性
 */
public class ExcelUtils {

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
     |                 导出Excel                 |
     ============================================*/

    /**
     * 写入Excel
     *
     * @param outputStream 输出流
     * @param list 数据集
     */
    public static void writeExcel(OutputStream outputStream, List<?> list) throws Exception {
        if (CollUtil.isEmpty(list)) {
            throw new Exception("无数据");
        }

        List<ExcelConfig> excelConfigList = getExcelConfig(list.get(0).getClass());
        ExcelWriter writer = ExcelUtil.getWriter(true).disableDefaultStyle();

        //创建文本类型的单元格格式
        CellStyle textStyle = createTextCellStyle(writer);

        //设置列
        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列名
            writer.writeCellValue(i, 0, config.getColumn().name());
            //设置列名的单元格格式格式
            writer.setStyle(textStyle, i, 0);
            //设置列宽度
            writer.setColumnWidth(i, config.getColumn().width());
        }

        //设置列的数据的单元格格式和数据
        List<Map<String, Object>> mapList = new ArrayList<>(list.size());
        list.forEach(o -> mapList.add(BeanUtil.beanToMap(o)));
        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            if (ExcelFormat.NONE.equals(config.getColumn().format())) {
                int length = config.getColumn().exportDecimalLength();
                if (length > 0) {
                    String pre = "#.";
                    String format = StrUtil.fillAfter(pre, '#', length + pre.length());
                    writeCell4Decimal(writer, mapList, config, i, format);
                } else {
                    writeCell(writer, mapList, config, i);
                }
            } else if (ExcelFormat.DATE.equals(config.getColumn().format())) {
                writeCell4Date(writer, mapList, config, i);
            } else if (ExcelFormat.DATETIME.equals(config.getColumn().format())) {
                writeCell4DateTime(writer, mapList, config, i);
            }
            //设置列的数据的单元格格式
            setCellStyle(writer, textStyle, i, mapList.size());
        }

        writer.flush(outputStream);
        writer.close();
    }

    private static void writeCell(ExcelWriter writer, List<Map<String, Object>> list, ExcelConfig config, int index) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            writer.writeCellValue(index, i + 1, StrUtil.toStringOrNull(map.get(config.getProp())));
        }
    }

    private static void writeCell4Decimal(ExcelWriter writer, List<Map<String, Object>> list, ExcelConfig config, int index, String format) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            writer.writeCellValue(index, i + 1, NumberUtil.decimalFormat(format, map.get(config.getProp())));
        }
    }

    private static void writeCell4Date(ExcelWriter writer, List<Map<String, Object>> list, ExcelConfig config, int index) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            writer.writeCellValue(index, i + 1, DateUtil.format((Date) map.get(config.getProp()), "yyyy-MM-dd"));
        }
    }

    private static void writeCell4DateTime(ExcelWriter writer, List<Map<String, Object>> list, ExcelConfig config, int index) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            writer.writeCellValue(index, i + 1, DateUtil.format((Date) map.get(config.getProp()), "yyyy-MM-dd HH:mm:ss"));
        }
    }

    private static CellStyle createTextCellStyle(ExcelWriter writer) {
        CellStyle cellStyle = writer.createCellStyle();
        cellStyle.setDataFormat(writer.getWorkbook().createDataFormat().getFormat("TEXT")); //TEXT或@都是指定文本类型
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
     |                 导入Excel                 |
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
        excelConfigList.forEach((o) -> {
            String header = StrUtil.isEmpty(o.getColumn().name()) ? o.getColumn().importName() : o.getColumn().name();
            reader.addHeaderAlias(header, o.getProp());
        });

        List<Map<String, Object>> dataList = reader.readAll();
        List<T> list = new ArrayList<>(dataList.size());
        CopyOptions copyOptions = CopyOptions.create();

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            for (ExcelConfig config : excelConfigList) {
                Object obj = map.get(config.getProp());
                //检查是否为空
                if (!config.getColumn().importEmpty() && obj == null) {
                    throw new Exception("第" + (i + 2) + "行数据不能为空或格式不正确");
                }
                //检查是否匹配正则表达式
                if (obj != null) {
                    if (ExcelFormat.NONE.equals(config.getColumn().format())) {
                        String format = config.getColumn().importFormat();
                        if (StrUtil.isNotEmpty(format)) {
                            if (!ReUtil.isMatch(format, String.valueOf(obj))) {
                                throw new Exception("第" + (i + 2) + "行数据格式不正确");
                            }
                        }
                    }
                }
            }

            T entity = BeanUtil.mapToBean(map, type, true, copyOptions);
            list.add(entity);
        }

        return list;
    }

}
