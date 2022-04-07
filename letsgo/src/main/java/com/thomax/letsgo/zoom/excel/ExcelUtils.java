package com.thomax.letsgo.zoom.excel;

import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.Data;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用Hutool操作Excel
 */
public class ExcelUtils {

    private static final File FILE = new File("C:\\Users\\Administrator\\Desktop\\123.xlsx");

    private static final Map<String, String> COLUMN_CELL_MAP;

    static {
        COLUMN_CELL_MAP = new LinkedHashMap<>();
        COLUMN_CELL_MAP.put("name", "姓名");
        COLUMN_CELL_MAP.put("number", "号码");
        COLUMN_CELL_MAP.put("birthday", "生日");
    }

    public static void main(String[] args) {
        writeExcel();
        readExcel();
    }

    private static void writeExcel() {
        ExcelWriter writer = ExcelUtil.getWriter();
        COLUMN_CELL_MAP.forEach((key, value) -> writer.addHeaderAlias(key, value));
        writer.write(createData());
        writer.setDestFile(FILE);
        writer.flush();
    }

    private static void readExcel() {
        ExcelReader reader = ExcelUtil.getReader(FILE);
        COLUMN_CELL_MAP.forEach((key, value) -> reader.addHeaderAlias(value, key));
        List<Person> all = reader.readAll(Person.class);
        System.out.println(JSONUtil.toJsonStr(all));
    }

    private static List<Person> createData() {
        Person person1 = new Person();
        person1.setName("aa");
        person1.setNumber(111);
        person1.setBirthday(new Date());
        Person person2 = new Person();
        person2.setName("bb");
        person2.setNumber(222);
        person2.setBirthday(new Date());
        return Arrays.asList(person1, person2);
    }

    @Data
    private static class Person {
        private String name;
        private Integer number;
        private Date birthday;
    }

}
