package com.thomax.ast.model;

import java.util.LinkedList;
import java.util.List;

public class Result {

    private List<Column> columnList;

    private List<List<Object>> rowList;

    public void addColumn(Column column) {
        if (columnList == null) {
            columnList = new LinkedList<>();
        }

        columnList.add(column);
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public void addRow(List<Object> row) {
        if (rowList == null) {
            rowList = new LinkedList<>();
        }

        rowList.add(row);
    }

    public List<List<Object>> getRowList() {
        return rowList;
    }

    public void setRowList(List<List<Object>> rowList) {
        this.rowList = rowList;
    }

}
