package com.thomax.ast.model;

public class Column {

    private String alias;

    private String column;

    private boolean isSelect;

    public Column() { }

    public Column(String alias, String column, boolean isSelect) {
        this.alias = alias;
        this.column = column;
        this.isSelect = isSelect;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
