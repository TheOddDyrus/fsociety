package com.thomax.ast.model;

public class Column {

    private String alias;

    private String column;

    public Column() { }

    public Column(String alias, String column) {
        this.alias = alias;
        this.column = column;
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
}
