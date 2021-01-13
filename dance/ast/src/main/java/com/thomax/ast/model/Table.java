package com.thomax.ast.model;

import com.thomax.ast.type.DbType;

import java.util.Properties;

public class Table {

    private String dbName;

    private String tableName;

    private String alias;

    private DbType dbType;

    private Properties baseInfo;

    public Table() { }

    public Table(String dbName, String tableName, String alias) {
        this.dbName = dbName;
        this.tableName = tableName;
        this.alias = alias;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public Properties getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(Properties baseInfo) {
        this.baseInfo = baseInfo;
    }

}
