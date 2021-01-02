package com.thomax.ast;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource.JoinType;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.util.JdbcConstants;
import com.thomax.ast.model.Column;
import com.thomax.ast.model.RelaType;
import com.thomax.ast.model.Table;
import com.thomax.ast.model.TableCondition;
import com.thomax.ast.model.TableRela;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 多数据源操作
 */
public class MultiDataSource {

    public static void main(String[] args) {
        String sql = "SELECT t1.hix, t3.pix " +
                "FROM db_device.tb_product t1, FEWESFS09WERFWEF t2, thomax t3 " +
                "WHERE 123 != t1.product_id and t1.product_id = 'hello' and t1.id = t2.tid or t2.cid = t3.mid OR t3.mid not in (1, 2)";

        /*String sql = "SELECT t1.hix, t3.pix " +
                "FROM db_device.tb_product t1" +
                "LEFT JOIN FEWESFS09WERFWEF t2 ON (t1.product_id = 123 and t1.id = t2.tid) or (t1.product_id = 456 and t1.id != t2.tid)" +
                "RIGHT JOIN thomax t3 ON t2.cid = t3.mid " +
                "WHERE t1.product_id in (123, 456)";*/

        HashMap<String, Object> topicData = new HashMap<>();
        topicData.put("dataTimeStamp", "generalMessage.getData().get(dataTimeStamp)");
        topicData.put("tslType", "StringUtils.isEmpty(tslType) ? (publishDataMap.size() > 0 ? properties : events) : tslType");
        topicData.put("macAddress", "did");
        topicData.put("deviceId", "deviceId");
        topicData.put("productId", "productId");
        topicData.put("command", "generalMessage.getCommand()");
        HashMap<String, Object> propertyData = new HashMap<>();
        topicData.put("data", propertyData);

        Map<String, Object> singleRow = new LinkedHashMap<>();
        singleRow.put("topicData", topicData);

        String topicTable = "X1";

        parseSQL(sql, topicData);
    }

    /**
     * 遍历Druid AST来操作多数据源。
     *
     * 支持的关键字：SELECT FROM WHERE LEFT RIGHT INNER JOIN ON NOT IN AND OR
     * 支持的符号： ( ) , = != ''
     * 注意事项：目前括号只支持IN ()的语法，不支持复合条件语句比如：((x = y) or (a = b))
     *
     * @param sql
     * @param topicData
     */
    public static void parseSQL(String sql, HashMap<String, Object> topicData) {
        //parse
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL); //解析大概耗时500ms（如果把tableRela转JSON，解析JSON时间差不多）
        SQLSelectStatement statement = (SQLSelectStatement) statementList.get(0);
        SQLSelect sqlSelect = (SQLSelect) statement.getChildren().get(0);
        MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) sqlSelect.getQuery();

        //COLUMN
        List<Column> columns = new ArrayList<>();
        List<SQLSelectItem> selectList = query.getSelectList();
        for (SQLSelectItem sqlSelectItem : selectList) {
            SQLPropertyExpr column = (SQLPropertyExpr) sqlSelectItem.getExpr();
            String alias = ((SQLIdentifierExpr) column.getOwner()).getName();
            String name = column.getName();
            columns.add(new Column(alias, name));
        }

        //TABLE
        TableRela tableRela = new TableRela();
        SQLJoinTableSource tableSource = (SQLJoinTableSource) query.getFrom();
        parseTable(tableSource, tableRela, columns);

        //CONDITION
        SQLBinaryOpExpr where = (SQLBinaryOpExpr) query.getWhere();
        parseCondition(where, tableRela, columns);
    }

    private static void parseTable(SQLJoinTableSource tableSource, TableRela tableRela, List<Column> columns) {
        SQLTableSource left = tableSource.getLeft();
        if (left instanceof SQLJoinTableSource) {
            TableRela parent = new TableRela();
            tableRela.setParent(parent);
            parseTable((SQLJoinTableSource) left, parent, columns);
        } else {
            //Primary table - Topic
            addTable(tableRela, left, true);
        }

        //Table relationship
        JoinType joinType = tableSource.getJoinType();
        SQLBinaryOpExpr condition;
        switch (joinType) {
            case COMMA: //,
                tableRela.setRela(RelaType.NONE);
                break;
            case JOIN: //LEFT JOIN
                tableRela.setRela(RelaType.LEFT_JOIN);
                condition = (SQLBinaryOpExpr) tableSource.getCondition();
                parseCondition(condition, tableRela, columns);
                break;
            case RIGHT_OUTER_JOIN: //RIGHT JOIN
                tableRela.setRela(RelaType.RIGHT_JOIN);
                condition = (SQLBinaryOpExpr) tableSource.getCondition();
                parseCondition(condition, tableRela, columns);
                break;
            case INNER_JOIN: //INNER JOIN
                tableRela.setRela(RelaType.INNER_JOIN);
                condition = (SQLBinaryOpExpr) tableSource.getCondition();
                parseCondition(condition, tableRela, columns);
                break;
        }

        //Right table
        SQLTableSource right = tableSource.getRight();
        addTable(tableRela, right, false);
    }

    private static void addTable(TableRela tableRela, SQLTableSource tableSource, boolean isLeft) {
        SQLPropertyExpr expr = (SQLPropertyExpr) ((SQLExprTableSource) tableSource).getExpr();
        SQLIdentifierExpr owner =  (SQLIdentifierExpr) expr.getOwner();
        String dbName = owner == null ? null : owner.getName();
        String tableName = expr.getName();
        String alias = tableSource.getAlias();
        Table table = new Table(dbName, tableName, alias);

        if (isLeft) {
            tableRela.setLeft(table);
        } else {
            tableRela.setRight(table);
        }
    }

    private static void parseCondition(SQLBinaryOpExpr where, TableRela tableRela, List<Column> columns) {
        SQLExpr left = where.getLeft();
        SQLExpr right = where.getRight();
        if (left instanceof SQLBinaryOpExpr) {
            parseCondition((SQLBinaryOpExpr) left, tableRela, columns);
        } else {
            if (left instanceof SQLPropertyExpr && right instanceof SQLPropertyExpr) { //只有2个同时为字段的才进行解析，其余的直接作为查询条件
                addColumn(left, columns);
                addColumn(right, columns);
            } else {
                addColumn(left, columns);

                TableCondition tableCondition = new TableCondition();
                //tableCondition
                tableRela.addCondition(tableCondition);
            }
        }

        SQLBinaryOperator operator = where.getOperator();
        switch (operator) {
            case BooleanAnd: //AND
                break;
            case BooleanOr: //OR
                break;
        }
    }

    private static void addColumn(SQLExpr expr, List<Column> columns) {
        SQLPropertyExpr propertyExpr = (SQLPropertyExpr) expr;
        String leftAlias = ((SQLIdentifierExpr) propertyExpr.getOwner()).getName();
        String leftColumn = propertyExpr.getName();
        selectColumn(leftAlias, leftColumn, columns);
    }

    private static void selectColumn(String newAlias, String newColumn, List<Column> columns) {
        for (Column column : columns) {
            boolean isSame = false;
            if (column.getColumn().equals(newColumn)) {
                isSame = true;
            }

            if (isSame && (column.getAlias() == null || column.getAlias().equals(newAlias))) {
                return;
            }
        }

        columns.add(new Column(newAlias, newColumn));
    }

}
