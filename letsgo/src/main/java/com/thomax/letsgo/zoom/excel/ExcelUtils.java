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
import com.thomax.letsgo.zoom.excel.annotation.ExportColumn;
import com.thomax.letsgo.zoom.excel.annotation.ImportColumn;
import com.thomax.letsgo.zoom.excel.entity.ExcelConfig;
import com.thomax.letsgo.zoom.excel.constant.ExcelFormat;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel工具（支持的数据类型：8个基础类型与包装类、java.util.Date、java.math.BigDecimal）
 *
 * 导出注解特别说明：
 * @see ExportColumn#format() 实体类中的日期字段必须设置此属性
 * @see ExportColumn#decimalLength() 自定义保留小数位数必须设置此属性
 * 导入注解特别说明：
 * @see ImportColumn#templateIndex() 导出模板的顺序
 * @see ImportColumn#templateWidth() 导出模板的单元格宽度
 * @see ImportColumn#templateExample() 导出模板的第一条默认数据
 */
public class ExcelUtils {

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

        List<ExcelConfig> excelConfigList = getExportConfig(list.get(0).getClass());
        ExcelWriter writer = ExcelUtil.getWriter(true).disableDefaultStyle();

        //创建文本类型的单元格格式
        CellStyle textStyle = createTextCellStyle(writer);

        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列宽度
            writer.setColumnWidth(i, config.getExportColumn().width());
            //设置整列的单元格格式
            writer.setColumnStyle(i, textStyle);
            //设置列名
            writer.writeCellValue(i, 0, config.getExportColumn().name());
            //设置列名的单元格格式
            writer.setStyle(textStyle, i, 0);
        }

        List<Map<String, Object>> mapList = new ArrayList<>(list.size());
        list.forEach(o -> mapList.add(BeanUtil.beanToMap(o)));
        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列的数据
            if (ExcelFormat.NONE.equals(config.getExportColumn().format())) {
                int length = config.getExportColumn().decimalLength();
                if (length > 0) {
                    String pre = "#.";
                    String format = StrUtil.fillAfter(pre, '#', length + pre.length());
                    writeCell4Decimal(writer, mapList, config, i, format);
                } else {
                    writeCell(writer, mapList, config, i);
                }
            } else if (ExcelFormat.DATE.equals(config.getExportColumn().format())) {
                writeCell4Date(writer, mapList, config, i);
            } else if (ExcelFormat.DATETIME.equals(config.getExportColumn().format())) {
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

    private static void setCellStyle(ExcelWriter writer, CellStyle cellStyle, int index, int total) {
        for (int i = 0; i < total; i++) {
            writer.setStyle(cellStyle, index, i + 1);
        }
    }

    //从实体类的注解中读取导出配置
    private static <T> List<ExcelConfig> getExportConfig(Class<T> type) {
        List<ExcelConfig> list = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(type);
        for (Field field : fields) {
            ExportColumn exportColumn = field.getAnnotation(ExportColumn.class);
            if (exportColumn == null) {
                continue;
            }
            ExcelConfig excelConfig = new ExcelConfig();
            excelConfig.setProp(field.getName());
            excelConfig.setType(field.getType());
            excelConfig.setExportColumn(exportColumn);
            list.add(excelConfig);
        }

        return CollUtil.sort(list, Comparator.comparingInt(o -> o.getExportColumn().index()));
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
        List<ExcelConfig> excelConfigList = getImportConfig(type);
        excelConfigList.forEach((o) -> reader.addHeaderAlias(o.getImportColumn().name(), o.getProp()));

        List<Map<String, Object>> dataList = reader.readAll();
        List<T> list = new ArrayList<>(dataList.size());
        CopyOptions copyOptions = CopyOptions.create();

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            for (ExcelConfig config : excelConfigList) {
                Object obj = map.get(config.getProp());
                //检查是否为空
                if (!config.getImportColumn().enableEmpty() && obj == null) {
                    throw new Exception("第" + (i + 2) + "行数据不能为空或格式不正确");
                }
                //检查是否匹配正则表达式
                if (obj != null) {
                    String format = config.getImportColumn().format();
                    if (StrUtil.isNotEmpty(format)) {
                        if (!ReUtil.isMatch(format, String.valueOf(obj))) {
                            throw new Exception("第" + (i + 2) + "行数据格式不正确");
                        }
                    }
                }
            }

            T entity = BeanUtil.mapToBean(map, type, true, copyOptions);
            list.add(entity);
        }

        return list;
    }

    /**
     * 下载导入模板
     *
     * @param outputStream 输出流
     * @param type 类型
     */
    public static <T> void downloadTemplate(OutputStream outputStream, Class<T> type) {
        List<ExcelConfig> excelConfigList = getImportConfig(type);
        ExcelWriter writer = ExcelUtil.getWriter(true).disableDefaultStyle();

        //创建文本类型的单元格格式
        CellStyle textStyle = createTextCellStyle(writer);

        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列宽度
            writer.setColumnWidth(i, config.getImportColumn().templateWidth());
            //设置整列的单元格格式
            writer.setColumnStyle(i, textStyle);
            //设置列名
            writer.writeCellValue(i, 0, config.getImportColumn().name());
            //设置列名的单元格格式
            writer.setStyle(textStyle, i, 0);
        }

        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列的数据
            writer.writeCellValue(i, 1, config.getImportColumn().templateExample());
            //设置列的数据的单元格格式
            writer.setStyle(textStyle, i, 1);
        }

        writer.flush(outputStream);
        writer.close();
    }

    /**
     * 下载导入模板
     *
     * @param outputStream 输出流
     * @param path 根路径
     */
    public static void downloadTemplate(OutputStream outputStream, String path) {
        ExcelWriter writer = ExcelUtil.getWriter(path);

        writer.flush(outputStream);
        writer.close();
    }

    //从实体类的注解中读取导入配置
    private static <T> List<ExcelConfig> getImportConfig(Class<T> type) {
        List<ExcelConfig> list = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(type);
        for (Field field : fields) {
            ImportColumn importColumn = field.getAnnotation(ImportColumn.class);
            if (importColumn == null) {
                continue;
            }
            ExcelConfig excelConfig = new ExcelConfig();
            excelConfig.setProp(field.getName());
            excelConfig.setType(field.getType());
            excelConfig.setImportColumn(importColumn);
            list.add(excelConfig);
        }

        return list;
    }

}
