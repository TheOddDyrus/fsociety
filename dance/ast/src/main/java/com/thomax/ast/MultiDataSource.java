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
import com.thomax.ast.model.RelaType;
import com.thomax.ast.model.Table;
import com.thomax.ast.model.TableRela;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 多数据源操作
 */
public class MultiDataSource {

    /**
     * 遍历Druid AST来操作多数据源。
     * 支持的关键字：SELECT FROM WHERE LEFT RIGHT JOIN ON IN AND OR
     *
     * @param args
     */
    public static void main(String[] args) {
        String sql = "SELECT t1.hix, t3.pix " +
                "FROM db_device.tb_product t1, FEWESFS09WERFWEF t2, thomax t3 " +
                "WHERE ((t1.product_id = 123 and t1.id = t2.tid) or (t1.product_id = 456 and t1.id = t3.tid)) and (t2.cid = t3.mid OR t3.mid in (1, 2))";
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

        //signle datasource
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        SQLSelectStatement statement = (SQLSelectStatement) statementList.get(0);
        SQLSelect sqlSelect = (SQLSelect) statement.getChildren().get(0);
        MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) sqlSelect.getQuery();

        //TABLE
        TableRela tableRela = new TableRela();
        SQLJoinTableSource tableSource = (SQLJoinTableSource) query.getFrom();
        parseTable(tableSource, tableRela);

        //CONDITION
        SQLBinaryOpExpr where = (SQLBinaryOpExpr) query.getWhere();
        parseCondition(where, tableRela);

        //COLUMN
        List<SQLSelectItem> selectList = query.getSelectList();
        for (SQLSelectItem sqlSelectItem : selectList) {
            SQLExpr expr1 = sqlSelectItem.getExpr();
        }
    }

    private static void parseTable(SQLJoinTableSource tableSource, TableRela tableRela) {
        SQLTableSource left = tableSource.getLeft();
        if (left instanceof SQLJoinTableSource) {
            TableRela parent = new TableRela();
            tableRela.setParent(parent);
            parseTable((SQLJoinTableSource) left, parent);
        } else {
            SQLPropertyExpr expr = (SQLPropertyExpr)((SQLExprTableSource) left).getExpr();
            String dbName = ((SQLIdentifierExpr) expr.getOwner()).getName();
            String tableName = expr.getName();
            String alias = left.getAlias();
            Table leftTable = new Table(dbName, tableName, alias, tableName.equals(tableName));
            tableRela.setLeft(leftTable);
        }

        JoinType joinType = tableSource.getJoinType();
        switch (joinType) {
            case COMMA: //,
                tableRela.setRela(RelaType.NONE);
                break;
            case JOIN: //LEFT JOIN
                tableRela.setRela(RelaType.LEFT_JOIN);
                SQLBinaryOpExpr condition = (SQLBinaryOpExpr) tableSource.getCondition();
                parseCondition(condition, tableRela);
                break;
            case RIGHT_OUTER_JOIN: //RIGHT JOIN
                tableRela.setRela(RelaType.RIGHT_JOIN);
                break;
            case INNER_JOIN: //INNER JOIN
                tableRela.setRela(RelaType.INNER_JOIN);
                break;
        }

        SQLExprTableSource right = (SQLExprTableSource) tableSource.getRight();
        SQLPropertyExpr expr = (SQLPropertyExpr) right.getExpr();
        String dbName = ((SQLIdentifierExpr) expr.getOwner()).getName();
        String tableName = expr.getName();
        String alias = left.getAlias();
        Table rightTable = new Table(dbName, tableName, alias, tableName.equals(tableName));
        tableRela.setRight(rightTable);
    }


    private static void parseCondition(SQLBinaryOpExpr where, TableRela tableRela) {
        SQLExpr left = where.getLeft();
        if (left instanceof SQLBinaryOpExpr) {
            parseCondition((SQLBinaryOpExpr) left, tableRela);
        } else if (left instanceof SQLInListExpr) {

        } else if (left instanceof SQLPropertyExpr) {
            SQLPropertyExpr leftProperty = (SQLPropertyExpr) left;
            String leftAlias = ((SQLIdentifierExpr) leftProperty.getOwner()).getName();
            String leftColumn = leftProperty.getName();
        }


        SQLExpr right = where.getRight();
        if (right instanceof SQLBinaryOpExpr) {
            parseCondition((SQLBinaryOpExpr) right, tableRela);
        } else if (right instanceof SQLInListExpr) {

        } else if (right instanceof SQLPropertyExpr) {
            SQLPropertyExpr rightProperty = (SQLPropertyExpr) left;
            String rightAlias = ((SQLIdentifierExpr) rightProperty.getOwner()).getName();
            String rightColumn = rightProperty.getName();
        }

        SQLBinaryOperator operator = where.getOperator();
        switch (operator) {
            case BooleanAnd: //AND
                break;
            case BooleanOr: //OR
                break;
        }
    }

}
