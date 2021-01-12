package com.thomax.ast.model;

import java.util.ArrayList;
import java.util.List;

public class TableRela {

    private TableRela parent;

    private Table left;

    private Table right;

    private RelaType rela;

    private List<Column> columnList;

    private List<TableCondition> conditionList;

    public TableRela getParent() {
        return parent;
    }

    public void setParent(TableRela parent) {
        this.parent = parent;
    }

    public Table getLeft() {
        return left;
    }

    public void setLeft(Table left) {
        this.left = left;
    }

    public Table getRight() {
        return right;
    }

    public void setRight(Table right) {
        this.right = right;
    }

    public RelaType getRela() {
        return rela;
    }

    public void setRela(RelaType rela) {
        this.rela = rela;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public void addColumn(Column newColumn) {
        if (columnList == null) {
            columnList = new ArrayList<>();
        } else {
            for (Column column : columnList) {
                if (column.getAlias()!= null &&
                        column.getAlias().equals(newColumn.getAlias()) &&
                        column.getColumn().equals(newColumn.getColumn())) {
                    return;
                }
            }
        }

        columnList.add(newColumn);
    }

    public List<TableCondition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<TableCondition> conditionList) {
        this.conditionList = conditionList;
    }

    public void addCondition(TableCondition tableCondition) {
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        conditionList.add(tableCondition);
    }

}
