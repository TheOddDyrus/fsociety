package com.thomax.ast;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.util.JdbcConstants;
import com.thomax.ast.model.Column;
import com.thomax.ast.model.ConditionType;
import com.thomax.ast.model.OperatorType;
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
                "FROM db_device.tb_product t1, FEWESFS09WERFWEF t2, db_device.thomax " +
                "WHERE 123 != t1.product_id and t1.product_id = 'hello' and t1.id = t2.tid or t2.cid = db_device.thomax.mid OR db_device.thomax.mid not in (1, 2) AND 1 = 1";

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

        //属性上报的Topic
        String propertyTopic = "/sys/0000000100020101/thing/event/property/post";
        String propertyTopicAlias = "PROPERTY_TOPIC";
        //事件上报的Topic
        String eventTopic = "/sys/0000000100020101/thing/event/${tsl.event.identifer}/post";
        String eventTopicAlias = "EVENT_TOPIC";

        try {
            Map<String, Object> result = parseSQL(sql, topicData);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("解析SQL过程发生异常：" + e.getMessage());
        }
    }

    /**
     * 解析SQL，进行多数据源关联查询
     *
     * 支持的关键字：SELECT FROM WHERE LEFT RIGHT INNER JOIN ON NOT IN AND OR
     * 支持的符号： ( ) , = != ''
     * 注意事项：目前括号只支持IN ()的语法，不支持复合条件语句比如：((x = y) or (a = b))
     *
     * @param sql
     * @param topicData 主表数据源
     */
    public static Map<String, Object> parseSQL(String sql, HashMap<String, Object> topicData) throws Exception {
        //Parse
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL); //这步解析大概耗时500ms（如果把tableRela转JSON，解析JSON时间差不多）
        SQLSelectStatement statement = (SQLSelectStatement) statementList.get(0);
        SQLSelect sqlSelect = (SQLSelect) statement.getChildren().get(0);
        MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) sqlSelect.getQuery();

        //Business AST
        TableRela tableRela = new TableRela();

        //Step1 - TABLE
        parseTable(query.getFrom(), tableRela);

        //Step2 - COLUMN
        List<SQLSelectItem> selectList = query.getSelectList();
        for (SQLSelectItem sqlSelectItem : selectList) {
            SQLPropertyExpr column = (SQLPropertyExpr) sqlSelectItem.getExpr();
            String alias = ((SQLIdentifierExpr) column.getOwner()).getName();
            String name = column.getName();
            addColumn(new Column(alias, name), tableRela);
        }

        //Step3 - CONDITION
        parseCondition(query.getWhere(), tableRela, null);

        return execAST(tableRela, topicData);
    }

    /**
     * 遍历业务AST来操作多数据源
     *
     * @param tableRela 表的关系
     * @param topicData 主表数据源
     * @return
     */
    private static Map<String, Object> execAST(TableRela tableRela, Map<String, Object> topicData) {
        if (tableRela.getParent() != null) {
            execAST(tableRela.getParent(), topicData);
        }

        //exec right
        topicData = new LinkedHashMap<>();
        topicData.put("topicData", topicData);

        return topicData;
    }

    /**
     * 解析表结构
     *
     * @param tableSource 表的数据源
     * @param tableRela 表的关系
     * @throws Exception
     */
    private static void parseTable(SQLTableSource tableSource, TableRela tableRela) throws Exception {
        SQLJoinTableSource tables = (SQLJoinTableSource) tableSource;
        SQLTableSource left = tables.getLeft();
        if (left instanceof SQLJoinTableSource) {
            TableRela parent = new TableRela();
            tableRela.setParent(parent);
            parseTable(left, parent);
        } else {
            //Primary table - Topic
            addTable(left, tableRela, true);
        }

        //Right table
        SQLTableSource right = tables.getRight();
        addTable(right, tableRela, false);

        //Table relationship
        switch (tables.getJoinType()) {
            case COMMA: //,
                tableRela.setRela(RelaType.NONE);
                break;
            case JOIN: //LEFT JOIN
                tableRela.setRela(RelaType.LEFT_JOIN);
                parseCondition(tables.getCondition(),  tableRela, null);
                break;
            case RIGHT_OUTER_JOIN: //RIGHT JOIN
                tableRela.setRela(RelaType.RIGHT_JOIN);
                parseCondition(tables.getCondition(), tableRela, null);
                break;
            case INNER_JOIN: //INNER JOIN
                tableRela.setRela(RelaType.INNER_JOIN);
                parseCondition(tables.getCondition(), tableRela, null);
                break;
        }
    }

    /**
     * 新增表结构
     *
     * @param tableSource 表的数据源
     * @param tableRela 表的关系
     * @param isLeft 左侧还是右侧的表
     */
    private static void addTable(SQLTableSource tableSource, TableRela tableRela, boolean isLeft) {
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

    /**
     * 解析条件
     *
     * @param expr 条件的数据源
     * @param tableRela 表的关系
     * @param conditionType 条件类型
     * @throws Exception
     */
    private static void parseCondition(SQLExpr expr, TableRela tableRela, ConditionType conditionType) throws Exception {
        TableCondition tableCondition;
        if (expr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr where = (SQLBinaryOpExpr) expr;
            SQLExpr left = where.getLeft();
            SQLExpr right = where.getRight();

            if (left instanceof SQLBinaryOpExpr) {
                parseCondition(left, tableRela, conditionType);
            } else {
                tableCondition = new TableCondition();
                if (conditionType == null) {
                    tableCondition.setCondition(ConditionType.BOOLEAN_AND); //默认值
                } else {
                    tableCondition.setCondition(conditionType);
                }

                //只有2个同时为字段的才进行解析，其余的直接作为查询条件
                if (left instanceof SQLPropertyExpr && right instanceof SQLPropertyExpr) {
                    tableCondition.setLeft(parseColumn(left, tableRela));
                    tableCondition.setRight(parseColumn(right, tableRela));
                    switch (where.getOperator()) {
                        case Equality:
                            tableCondition.setOperator(OperatorType.EQUALITY);
                            break;
                        case NotEqual:
                            tableCondition.setOperator(OperatorType.NOT_EQUAL);
                            break;
                    }
                } else {
                    //使用tableCondition.left存放查询条件内的唯一字段，如果不存在唯一字段那只需要表达式的字符串
                    if (left instanceof SQLPropertyExpr) {
                        tableCondition.setLeft(parseColumn(left, tableRela));
                    } else if (right instanceof SQLPropertyExpr) {
                        tableCondition.setLeft(parseColumn(right, tableRela));
                    }
                    tableCondition.setExpr(where.toString());
                }

                addCondition(tableRela, tableCondition, false);
            }

            if (right instanceof SQLBinaryOpExpr) {
                ConditionType rightConditionType = null;
                switch (where.getOperator()) {
                    case BooleanAnd: //AND
                        rightConditionType = ConditionType.BOOLEAN_AND;
                        break;
                    case BooleanOr: //OR
                        rightConditionType = ConditionType.BOOLEAN_OR;
                        break;
                }
                parseCondition(right, tableRela, rightConditionType);
            }
        } else if (expr instanceof SQLInListExpr) {
            SQLInListExpr where = (SQLInListExpr) expr;
            tableCondition = new TableCondition();
            Column column = parseColumn(where, tableRela);
            //使用tableCondition.left存放查询条件内的唯一字段
            tableCondition.setLeft(column);

            StringBuilder prefix = new StringBuilder();
            prefix.append(column.getAlias()).append(".").append(column.getColumn()).append(where.isNot() ? " NOT IN (" : " IN (");

            List<SQLExpr> targetList = where.getTargetList();
            boolean isNumber = targetList.get(0) instanceof SQLIntegerExpr;
            for (SQLExpr sqlExpr : targetList) {
                if (isNumber) {
                    prefix.append(sqlExpr.toString()).append(",");
                } else {
                    prefix.append("'").append(sqlExpr.toString()).append("',");
                }
            }
            String exprStr = prefix.replace(prefix.length() - 1, prefix.length(), ")").toString();
            tableCondition.setExpr(exprStr);

            addCondition(tableRela, tableCondition, false);
        } else {
            throw new Exception("出现不支持的语法");
        }
    }

    /**
     * 将条件新增到表的关系中
     *
     * SQL为SELECT ... FROM topic t1, xx t2, yy t3时，tableRela结构如下
     *         t1
     *         conditionList1
     *      t2
     *      conditionList2
     *   t3
     *   conditionList3
     * 当有关系为t2.id = t3.id的tableCondition新增到tableRela时，tableCondition会存放在最外面的t3处
     * （这样在一次IO查询t2时，把t2.id都查出来，再回溯这些t2.id到conditionList3中关联查询）
     *
     * @param tableRela 表的关系
     * @param tableCondition 条件
     */
    private static void addCondition(TableRela tableRela, TableCondition tableCondition, boolean isLastCheck) {
        Column leftColumn = tableCondition.getLeft();
        Column rightColumn = tableCondition.getRight();
        TableRela parent = tableRela.getParent();
        Table rightTable = tableRela.getRight();

        if (parent == null) { //不用使用leftTable来做判断，一直递归到parent == null可直接插入条件
            tableRela.addCondition(tableCondition);
        } else {
            String checkAlias = getTableAlias(rightTable);

            if (checkAlias.equals(leftColumn.getAlias())) {
                if (isLastCheck) {
                    tableRela.addCondition(tableCondition);
                } else {
                    addCondition(parent, tableCondition, true);
                }
            } else if (checkAlias.equals(rightColumn.getAlias())) {
                if (isLastCheck) {
                    tableRela.addCondition(tableCondition);
                } else {
                    addCondition(parent, tableCondition, true);
                }
            } else {
                addCondition(parent, tableCondition, false);
            }
        }
    }

    /**
     * 解析字段
     *
     * @param expr 字段的数据源
     * @param tableRela 表的关系
     * @return
     */
    private static Column parseColumn(SQLExpr expr, TableRela tableRela) {
        SQLPropertyExpr propertyExpr = (SQLPropertyExpr) expr;
        SQLExpr owner = propertyExpr.getOwner();

        String leftAlias = null;
        if (owner instanceof SQLPropertyExpr) {
            SQLIdentifierExpr subOwner = (SQLIdentifierExpr) ((SQLPropertyExpr) owner).getOwner();
            String dbName = subOwner.getName();
            leftAlias = ((SQLPropertyExpr) owner).getName();
            if (dbName != null) {
                leftAlias = dbName + "." + leftAlias;
            }
        } else if (owner instanceof SQLIdentifierExpr) {
            leftAlias = ((SQLIdentifierExpr) owner).getName();
        }

        String leftColumn = propertyExpr.getName();
        Column newColumn = new Column(leftAlias, leftColumn);

        addColumn(newColumn, tableRela);

        return newColumn;
    }

    /**
     * 新增字段
     *
     * @param column 新的字段
     * @param tableRela 表的关系
     */
    private static void addColumn(Column column, TableRela tableRela) {
        String checkAlias = getTableAlias(tableRela.getRight());
        if (checkAlias.equals(column.getAlias())) {
            tableRela.addColumn(column);
        } else {
            addColumn(column, tableRela.getParent());
        }
    }

    /**
     * 获得表的别名
     *
     * @param table 表的对象
     * @return
     */
    private static String getTableAlias(Table table) {
        return table.getAlias() != null ? table.getAlias() : table.getDbName() + "." + table.getTableName();
    }

}
