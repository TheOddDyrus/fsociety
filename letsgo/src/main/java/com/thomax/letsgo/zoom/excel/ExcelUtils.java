package com.thomax.letsgo.zoom.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
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
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Excel工具（支持的数据类型：8个基础类型与包装类、java.util.Date、java.math.BigDecimal）
 *
 * 导出注解特别说明：
 * @see ExportColumn#name() 使用自定义模板导出时可以不用设置此属性，会根据index()序号导出
 * @see ExportColumn#i18nKey() 定义I18nUtil.getMessage()所使用的Key，Key存在时name()属性会失效
 * @see ExportColumn#format() 实体类中的日期字段必须设置此属性
 * @see ExportColumn#decimalLength() 自定义保留小数位数必须设置此属性
 *
 * 导入注解特别说明：
 * @see ImportColumn#i18nKey() 国际化的Key，定义以后会被I18nUtil.getMessage()所使用并获得对于国际化内容，Key设置后name()属性会失效
 * @see ImportColumn#format() 正则表达式匹配数据，可以使用com.sf.air.service.frame.constant.NormalRegx
 *
 * 下载导入的Excel模板，可以使用以下配置自动生成模板：
 * @see ImportColumn#templateIndex() 导出模板的顺序
 * @see ImportColumn#templateWidth() 导出模板的单元格宽度
 * @see ImportColumn#templateExample() 导出模板的第一条默认数据
 *
 * 演示案例：单元测试目录下的com.sf.air.service.frame.ExcelTest#test()
 */
public class ExcelUtils {

     /*--------------------------------------------
     |                 导出Excel                 |
     ============================================*/

    /**
     * 导出Excel
     *
     * @param outputStream 输出流
     * @param list 数据集
     */
    public static void exportExcel(OutputStream outputStream, List<?> list) {
        if (CollUtil.isEmpty(list)) {
            throw new RuntimeException("无数据");
        }

        ExcelWriter writer = ExcelUtil.getWriter(true).disableDefaultStyle();
        setCellStyleAndValue(writer, list, 1, false);

        flushAndClose(writer, outputStream);
    }

    /**
     * 导出Excel
     *
     * @param outputStream 输出流
     * @param list 数据集
     * @param path 模板相对于Resource目录的路径
     */
    public static void exportExcel(OutputStream outputStream, List<?> list, String path) {
        exportExcel(outputStream, list, path, 1);
    }

    /**
     * 导出Excel
     *
     * @param outputStream 输出流
     * @param list 数据集
     * @param path 模板相对于Resource目录的路径
     * @param startIndex 从下标N开始写入数据（Excel内第一行的下标为0）
     */
    public static void exportExcel(OutputStream outputStream, List<?> list, String path, int startIndex) {
        if (CollUtil.isEmpty(list)) {
            throw new RuntimeException("无数据");
        }

        ExcelWriter writer = ExcelUtil.getWriter(path).disableDefaultStyle();
        setCellStyleAndValue(writer, list, startIndex, true);

        flushAndClose(writer, outputStream);
    }

    //设置单元格格式和数据
    private static void setCellStyleAndValue(ExcelWriter writer, List<?> list, int startIndex, boolean useTemplate) {
        //读取注解的配置
        List<ExcelConfig> excelConfigList = getExportConfig(list.get(0).getClass());

        //创建文本类型的单元格格式
        CellStyle headerStyle = createCellStyle(writer, true);
        CellStyle normalStyle = createCellStyle(writer, false);
        //创建超文本类型的创建器
        CreationHelper creationHelper = writer.getWorkbook().getCreationHelper();

        //创建日期时间格式器
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (!useTemplate) {
            setHeader(writer, excelConfigList, headerStyle, normalStyle, true);
        }

        List<Map<String, Object>> mapList = new ArrayList<>(list.size());
        list.forEach(o -> mapList.add(BeanUtil.beanToMap(o)));
        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列的数据
            switch (config.getExportColumn().format()) {
                case NONE:
                    int length = config.getExportColumn().decimalLength();
                    if (length > 0) {
                        String pre = "#.";
                        String format = StrUtil.fillAfter(pre, '#', length + pre.length());
                        setCellValue(writer, mapList, config, i, startIndex, o -> NumberUtil.decimalFormat(format, o));
                    } else {
                        setCellValue(writer, mapList, config, i, startIndex, StrUtil::toStringOrNull);
                    }
                    break;
                case DATE:
                    setCellValue(writer, mapList, config, i, startIndex, dateFormat::format);
                    break;
                case DATETIME:
                    setCellValue(writer, mapList, config, i, startIndex,  dateTimeFormat::format);
                    break;
                case HYPERLINK_FILE:
                    setCellValue4Hyperlink(writer, mapList, config, i, startIndex, o -> {
                        String value = StrUtil.toStringOrNull(o);
                        Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.FILE);
                        hyperlink.setAddress("./" + value); //设置文件与Excel在同一文件夹下

                        return new Object[] {value, hyperlink};
                    });
                    break;
                case HYPERLINK_URL:
                    setCellValue4Hyperlink(writer, mapList, config, i, startIndex, o -> {
                        String value = StrUtil.toStringOrNull(o);
                        Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.URL);
                        hyperlink.setAddress(value);

                        return new Object[] {value, hyperlink};
                    });
                    break;
            }
            //设置列的数据的单元格格式
            setCellStyle(writer, normalStyle, i, mapList.size());
        }
    }

    //创建文本单元格格式
    private static CellStyle createCellStyle(ExcelWriter writer, boolean isHeader) {
        CellStyle cellStyle = writer.createCellStyle();
        cellStyle.setDataFormat(writer.getWorkbook().createDataFormat().getFormat("TEXT")); //TEXT或@都是指定文本类型
        if (isHeader) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        } else {
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
        }

        Font font = writer.createFont();
        font.setFontName("宋体");
        cellStyle.setFont(font);

        return cellStyle;
    }

    //设置单元格格式
    private static void setCellStyle(ExcelWriter writer, CellStyle cellStyle, int index, int total) {
        for (int i = 0; i < total; i++) {
            writer.setStyle(cellStyle, index, i + 1);
        }
    }

    //设置单元格数据
    private static void setCellValue(ExcelWriter writer, List<Map<String, Object>> list, ExcelConfig config,
                                     int index, int startRow, Function<Object, String> function) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            writer.writeCellValue(index, i + startRow, function.apply(map.get(config.getProp())));
        }
    }

    //设置单元格数据（本地文件的超文本链接）
    private static void setCellValue4Hyperlink(ExcelWriter writer, List<Map<String, Object>> list, ExcelConfig config,
                                     int index, int startRow, Function<Object, Object[]> function) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            Object[] valueArr = function.apply(map.get(config.getProp()));

            Cell cell = writer.getOrCreateCell(index, i + startRow);
            cell.setCellValue((String) valueArr[0]);
            cell.setHyperlink((Hyperlink) valueArr[1]);
        }
    }

    //设置列
    private static void setHeader(ExcelWriter writer, List<ExcelConfig> excelConfigList,
                                  CellStyle headerStyle, CellStyle normalStyle, boolean isExport) {
        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列宽度
            int width = isExport ? config.getExportColumn().width() : config.getImportColumn().templateWidth();
            writer.setColumnWidth(i, width);
            //设置整列的单元格格式
            writer.setColumnStyle(i, normalStyle);
            //设置列名
            writer.writeCellValue(i, 0, getHeaderName(config, isExport));
            //设置列名的单元格格式
            writer.setStyle(headerStyle, i, 0);
        }
    }

    //获得列名（支持国际化）
    private static String getHeaderName(ExcelConfig config, boolean isExport) {
        String header;
        String i18nKey;

        if (isExport) {
            i18nKey = config.getExportColumn().i18nKey();
        } else {
            i18nKey = config.getImportColumn().i18nKey();
        }

        if (StrUtil.isNotBlank(i18nKey)) {
            header = i18nKey; //I18nUtil.getMessage(i18nKey);
        } else {
            if (isExport) {
                header = config.getExportColumn().name();
            } else {
                header = config.getImportColumn().name();
            }
        }

        return header;
    }

    //刷数据、关闭writer
    private static void flushAndClose(ExcelWriter writer, OutputStream outputStream) {
        writer.flush(outputStream);
        writer.setDestFile(null);
        writer.close();
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
    public static <T> List<T> importExcel(InputStream inputStream, Class<T> type) {
        return importExcel(inputStream, type, 1);
    }

    /**
     * 导入Excel
     *
     * @param inputStream 输入流
     * @param type 类型
     * @param startIndex 从下标N开始读取数据（Excel内第一行的下标为0）
     */
    public static <T> List<T> importExcel(InputStream inputStream, Class<T> type, int startIndex) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<ExcelConfig> excelConfigList = getImportConfig(type);
        excelConfigList.forEach((o) -> reader.addHeaderAlias(getHeaderName(o, false), o.getProp()));

        //startIndex的上一行为列名所在的行
        List<Map<String, Object>> dataList = reader.read(startIndex - 1, startIndex, 60000);

        List<T> list = new ArrayList<>(dataList.size());
        CopyOptions copyOptions = CopyOptions.create();

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            for (ExcelConfig config : excelConfigList) {
                Object obj = map.get(config.getProp());
                String value;
                if (obj instanceof String) {
                    value = (String) obj;
                } else {
                    value = StrUtil.toStringOrNull(obj);
                }
                //检查是否为空
                if (!config.getImportColumn().enableEmpty() && StrUtil.isBlank(value)) {
                    throw new RuntimeException(StrUtil.format("第{}行数据的列【{}】不能为空",
                            i + startIndex + 1,
                            getHeaderName(config, false)));
                }
                //检查是否匹配正则表达式
                if (StrUtil.isNotBlank(value)) {
                    String format = config.getImportColumn().format();
                    if (StrUtil.isNotBlank(format)) {
                        if (!ReUtil.isMatch(format, value)) {
                            throw new RuntimeException(StrUtil.format("第{}行数据的列【{}】格式不正确或超出范围",
                                    i + startIndex + 1,
                                    getHeaderName(config, false)));
                        }
                    }
                }
            }

            T entity = BeanUtil.mapToBean(map, type, false, copyOptions);
            list.add(entity);
        }

        reader.close();

        return list;
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

    /*--------------------------------------------
     |                 Excel模板                 |
     ============================================*/

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
        CellStyle headerStyle = createCellStyle(writer, true);
        CellStyle normalStyle = createCellStyle(writer, false);

        for (int i = 0; i < excelConfigList.size(); i++) {
            setHeader(writer, excelConfigList, headerStyle, normalStyle, false);
        }

        for (int i = 0; i < excelConfigList.size(); i++) {
            ExcelConfig config = excelConfigList.get(i);
            //设置列的数据
            writer.writeCellValue(i, 1, config.getImportColumn().templateExample());
            //设置列的数据的单元格格式
            writer.setStyle(normalStyle, i, 1);
        }

        flushAndClose(writer, outputStream);
    }

    /**
     * 下载导入模板
     *
     * @param outputStream 输出流
     * @param path 模板相对于Resource目录的路径
     */
    public static void downloadTemplate(OutputStream outputStream, String path) {
        ExcelWriter writer = ExcelUtil.getWriter(path).disableDefaultStyle();

        flushAndClose(writer, outputStream);
    }

}
