package com.thomax.ast.model;

import java.util.List;

public class Result {

    private List<String> columnList;

    private List<List<Object>> dataList;

    public List<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }

    public List<List<Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<Object>> dataList) {
        this.dataList = dataList;
    }
}
