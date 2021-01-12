package com.thomax.ast;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
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
import com.alibaba.fastjson.JSON;
import com.thomax.ast.exception.NoDataException;
import com.thomax.ast.model.Column;
import com.thomax.ast.model.ConditionType;
import com.thomax.ast.model.DbType;
import com.thomax.ast.model.OperatorType;
import com.thomax.ast.model.RelaType;
import com.thomax.ast.model.Result;
import com.thomax.ast.model.Table;
import com.thomax.ast.model.TableCondition;
import com.thomax.ast.model.TableRela;
import com.thomax.ast.util.MySQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 多数据源操作
 */
public class MultiDataSource {

    private static final String PROPERTY_TOPIC = "PROPERTY_TOPIC";

    private static final String EVENT_TOPIC = "EVENT_TOPIC";

    private static final Pattern BRACKET_PATTERN = Pattern.compile("(?<=\\()[^\\)]+");

    private static final Set<String> TOPIC_COLUMNS = new HashSet<>();

    static {
        TOPIC_COLUMNS.add("dataTimeStamp");
        TOPIC_COLUMNS.add("tslType");
        TOPIC_COLUMNS.add("macAddress");
        TOPIC_COLUMNS.add("deviceId");
        TOPIC_COLUMNS.add("productId");
        TOPIC_COLUMNS.add("command");
    }

    public static void main(String[] args) {
        String sql1 = "SELECT t1.hix, 中国.pix " +
                "FROM  PROPERTY_TOPIC t2, c.tb_product t1, db_device.thomax, 中国 " +
                "WHERE t1.product_id = 12 and t1.product_id = 'hello' and t1.id = t2.tid or t2.cid = db_device.thomax.mid " +
                "OR db_device.thomax.mid not in ('1', '2') AND 中国.aaa = 123 and 中国.id = t1.id";

        String sql2 = "SELECT t1.hix, t3.pix " +
                "FROM PROPERTY_TOPIC t1, david dd " +
                "LEFT JOIN db_device.tb_product t2 ON t1.product_id = 123 and t1.id = t2.tid or t1.product_id = 456 and t1.id = t2.tid " +
                "LEFT JOIN thomax t3 ON t2.cid = t3.mid " +
                "INNER JOIN thomax2 t4 ON t3.cid = t4.mid " +
                "WHERE t1.product_id in (123, 456) AND dd.id = t1.id";

        String sql3 = "SELECT t1.command, t2.device_id, t3.info_value \n" +
                "FROM PROPERTY_TOPIC t1, tb_device_mac t2, tb_device_info t3 \n" +
                "WHERE t1.deviceId = t2.device_id AND t2.mac_address = t3.mac";

        HashMap<String, Object> topicData = new HashMap<>();
        topicData.put("dataTimeStamp", System.currentTimeMillis()); //generalMessage.getData().get(dataTimeStamp)
        topicData.put("tslType", "properties"); //StringUtils.isEmpty(tslType) ? (publishDataMap.size() > 0 ? properties : events) : tslType
        topicData.put("macAddress", "ABCDEFGHI");
        topicData.put("deviceId", 100310);
        topicData.put("productId", 1234);
        topicData.put("command", "command-test"); //generalMessage.getCommand()
        HashMap<String, Object> propertyData = new HashMap<>();
        propertyData.put("SWITCH_STATUS", 1);
        topicData.put("data", propertyData);

        try {
            Result result = execSQL(sql3, topicData);
        } catch (NoDataException nde) {
            System.out.println("没有数据查询出来");
        } catch (Exception e) {
            System.out.println("解析SQL过程发生异常：" + e.getMessage());
        }
    }

    /**
     * 解析SQL并进行多数据源关联查询
     *
     * 支持的关键字：SELECT FROM WHERE LEFT INNER JOIN ON NOT IN AND OR
     * 支持的符号： ( ) , = != ''
     * 注意事项：目前括号只支持IN ()的语法，不支持复合条件语句比如：((x = y) or (a = b))
     *
     * @param sql
     * @param topicData 主表数据源
     */
    public static Result execSQL(String sql, Map<String, Object> topicData) throws Exception {
        System.out.println(">>设备上报的topicData：" + JSON.toJSONString(topicData));
        System.out.println(">>待执行的SQL：" + sql);
        long start = System.currentTimeMillis();
        //VERIFY BRACKET
        Matcher matcher = BRACKET_PATTERN.matcher(sql);
        while (matcher.find()) {
            String condition = matcher.group();
            if (!condition.contains(",")) {
                throw new Exception("目前括号只支持IN ()的语法，不支持复合条件语句比如：((x = y) or (a = b))");
            }
        }
        System.out.println(">>正则表达式语法校验耗时：" + (System.currentTimeMillis() - start) +  "毫秒");

        start = System.currentTimeMillis();
        //get Druid AST
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL); //这步解析大概耗时500ms（如果把tableRela转JSON，解析JSON时间差不多）
        SQLSelectStatement statement = (SQLSelectStatement) statementList.get(0);
        SQLSelect sqlSelect = (SQLSelect) statement.getChildren().get(0);
        MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) sqlSelect.getQuery();
        System.out.println(">>Druid解析SQL生成AST耗时：" + (System.currentTimeMillis() - start) +  "毫秒");

        start = System.currentTimeMillis();
        //new Business AST
        TableRela tableRela = new TableRela();

        //Step1 - TABLE
        parseTable(query.getFrom(), tableRela);

        //Step2 - COLUMN
        List<Column> selectColumnList = new ArrayList<>();
        List<SQLSelectItem> selectList = query.getSelectList();
        for (SQLSelectItem sqlSelectItem : selectList) {
            SQLPropertyExpr column = (SQLPropertyExpr) sqlSelectItem.getExpr();
            String alias = ((SQLIdentifierExpr) column.getOwner()).getName();
            String name = column.getName();
            Column newColumn = new Column(alias, name);
            selectColumnList.add(newColumn);

            addColumn(newColumn, tableRela);
        }

        //Step3 - CONDITION
        parseCondition(query.getWhere(), tableRela, null);

        //Step4 - VERIFY
        String invalidTable = checkInvalidTable(tableRela, new HashSet<>());
        if (invalidTable != null && invalidTable.length() > 0) {
            throw new Exception("表: " + invalidTable + " 和其他表之间缺少关联关系");
        }
        System.out.println(">>生成数据流转AST耗时：" + (System.currentTimeMillis() - start) +  "毫秒");

        String s = JSON.toJSONString(tableRela);
        System.out.println(">>生成数据流转AST转JSON：" + s);
        start = System.currentTimeMillis();
        TableRela newObject = (TableRela) JSON.parseObject(s, TableRela.class);
        System.out.println(">>数据流转AST的JSON转回对象耗时：" + (System.currentTimeMillis() - start) +  "毫秒");

        start = System.currentTimeMillis();
        //Step5 - EXECUTE
        Result result = new Result();
        execAST(tableRela, topicData, result);

        //Step6 - FILTER
        List<Column> columnList = result.getColumnList();
        List<Integer> deleteIndexList = new ArrayList<>();
        int deleteIndex = 0;
        Iterator<Column> columnIterator = columnList.iterator();
        while (columnIterator.hasNext()) {
            Column column = columnIterator.next();
            boolean isSelect = false;
            for (Column selectColumn : selectColumnList) {
                if (selectColumn.getAlias().equals(column.getAlias()) && selectColumn.getColumn().equals(column.getColumn())) {
                    isSelect = true;
                    break;
                }
            }
            if (!isSelect) {
                columnIterator.remove();
                deleteIndexList.add(deleteIndex);
            }
            deleteIndex++;
        }
        deleteIndexList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 < o2) {
                    return 1;
                } else if (o1 > o2) {
                    return -1;
                }
                return 0;
            }
        });
        for (List<Object> row : result.getRowList()) {
            for (Integer index : deleteIndexList) {
                int cursor = 0;
                Iterator<Object> rowIterator = row.iterator();
                while (rowIterator.hasNext()) {
                    rowIterator.next();
                    if (cursor == index) {
                        rowIterator.remove();
                        break;
                    }
                    cursor++;
                }
            }
        }
        System.out.println(">>使用AST执行多数据源操作耗时：" + (System.currentTimeMillis() - start) +  "毫秒");

        System.out.println(">>查询结果：" + JSON.toJSONString(result));

        return result;
    }

    /**
     * 检测语法是否无效：
     * ①2张表之间需要有id相等的关联条件（t1.id = t2.id）
     * ②2张表之间的字段不能使用不等号进行匹配（t1.id != t2.id）
     *
     * @param tableRela 表的关系
     * @param leftTableAliasList 回溯所需的左表集合
     * @return
     */
    private static String checkInvalidTable(TableRela tableRela, Set<String> leftTableAliasList) throws Exception {
        if (tableRela.getParent() != null) {
            String invalidTable = checkInvalidTable(tableRela.getParent(), leftTableAliasList);
            if (invalidTable != null) {
                return invalidTable;
            }
        } else {
            String tableName = tableRela.getLeft().getTableName();
            if (!PROPERTY_TOPIC.equals(tableName) && !EVENT_TOPIC.equals(tableName)) {
                throw new Exception("SQL语法中FROM的第一张表（主表）一定要为PROPERTY_TOPIC或EVENT_TOPIC");
            }

            leftTableAliasList.add(getTableAlias(tableRela.getLeft()));
        }

        String rightTableAlias = getTableAlias(tableRela.getRight());
        if (rightTableAlias == null) { //单表
            return null;
        }

        List<TableCondition> conditionList = tableRela.getConditionList();
        for (TableCondition tableCondition : conditionList) {
            Column leftColumn = tableCondition.getLeft();
            Column rightColumn = tableCondition.getRight();
            if (tableCondition.getOperator().equals(OperatorType.EQUAL)) {
                for (String leftTableAlias : leftTableAliasList) {
                    if (leftColumn != null && rightColumn != null) {
                        if ((leftTableAlias.equals(leftColumn.getAlias()) && rightTableAlias.equals(rightColumn.getAlias())) ||
                                (leftTableAlias.equals(rightColumn.getAlias()) && rightTableAlias.equals(leftColumn.getAlias()))) {
                            leftTableAliasList.add(rightTableAlias);
                            return null;
                        }
                    }
                }
            } else if (tableCondition.getOperator().equals(OperatorType.NOT_EQUAL)) {
                if (tableCondition.getLeft() != null && tableCondition.getRight() != null) {
                    throw new Exception("2个表之间的字段不能使用!=进行关联");
                }
            }
        }

        if (tableRela.getRight().getDbName() != null) {
            return tableRela.getRight().getDbName() + "." + tableRela.getRight().getTableName();
        } else {
            return tableRela.getRight().getTableName();
        }
    }

    /**
     * 遍历业务AST来操作多数据源
     *
     * @param tableRela 表的关系
     * @param topicData 主表数据源
     * @param result 结果集
     * @return
     */
    private static void execAST(TableRela tableRela, Map<String, Object> topicData, Result result) throws Exception {
        List<Column> rightColumnList;
        if (tableRela.getParent() != null) {
            execAST(tableRela.getParent(), topicData, result);
            rightColumnList = tableRela.getColumnList();
        } else {
            //get topic result
            Table left = tableRela.getLeft();
            List<Column> topicColumnList = new LinkedList<>();
            rightColumnList = new LinkedList<>();
            for (Column column : tableRela.getColumnList()) {
                if (column.getAlias().equals(getTableAlias(left))) {
                    topicColumnList.add(column);
                } else {
                    rightColumnList.add(column);
                }
            }
            List<Object> firstRow = new LinkedList<>();
            for (Column column : topicColumnList) {
                result.addColumn(column);
                if (topicData.containsKey(column.getColumn())) {
                    firstRow.add(topicData.get(column.getColumn()));
                } else {
                    Map<String, Object> data = (Map<String, Object>) topicData.get("data");
                    firstRow.add(data.get(column.getColumn()));
                }
            }
            result.addRow(firstRow);
        }

        //condition
        List<TableCondition> conditionList = tableRela.getConditionList();

        //right table
        Table right = tableRela.getRight();
        switch (right.getDbType()) {
            case MYSQL:
                StringBuilder selectBuilder = new StringBuilder("select ");

                for (Column column : rightColumnList) {
                    selectBuilder.append(column.getColumn()).append(",");
                }
                selectBuilder.deleteCharAt(selectBuilder.length() - 1);
                selectBuilder.append(" from ");
                selectBuilder.append(right.getTableName());
                selectBuilder.append(" where ");

                boolean isFristAnd = true;
                boolean isExistPreviousExpr = false;
                List<TableCondition> twoTableCondition = new ArrayList<>(); //t1.id = t2.id需要等计算完单字段表达式后再计算
                //第一步计算：单字段表达式
                for (TableCondition tableCondition : conditionList) {
                    if (tableCondition.getCollection() != null) {
                        if (tableCondition.getLeft().getAlias().equals(getTableAlias(right))) { //right table condition
                            if (isFristAnd) {
                                isFristAnd = false;
                            } else {
                                ConditionType condition = tableCondition.getCondition();
                                switch (condition) {
                                    case BOOLEAN_AND:
                                        selectBuilder.append(" and ");
                                        break;
                                    case BOOLEAN_OR:
                                        selectBuilder.append(" or ");
                                        break;
                                }
                            }

                            selectBuilder.append(tableCondition.getLeft().getColumn())
                                    .append(" ");
                            switch (tableCondition.getOperator()) {
                                case EQUAL:
                                    selectBuilder.append(tableCondition.getLeft().getColumn())
                                            .append("=")
                                            .append(tableCondition.getCollection().get(0));
                                    break;
                                case NOT_EQUAL:
                                    selectBuilder.append(tableCondition.getLeft().getColumn())
                                            .append("!=")
                                            .append(tableCondition.getCollection().get(0));
                                    break;
                                case IN:
                                    selectBuilder.append("in (");
                                    List<Object> collection = tableCondition.getCollection();
                                    boolean isNumber = collection.get(0) instanceof Number;
                                    for (Object o : collection) {
                                        if (isNumber) {
                                            selectBuilder.append(o.toString()).append(",");
                                        }
                                    }
                                    selectBuilder.deleteCharAt(selectBuilder.length() - 1);
                                    selectBuilder.append(")");
                                    break;
                                case NOT_IN:
                                    selectBuilder.append("not in (");
                                    collection = tableCondition.getCollection();
                                    isNumber = collection.get(0) instanceof Number;
                                    for (Object o : collection) {
                                        if (isNumber) {
                                            selectBuilder.append(o.toString()).append(",");
                                        }
                                    }
                                    selectBuilder.deleteCharAt(selectBuilder.length() - 1);
                                    selectBuilder.append(")");
                                    break;
                            }

                            isExistPreviousExpr = true;
                        } else { //result condition
                            int columnIndex = getColumnIndex(tableCondition.getLeft(), result);

                            Iterator<List<Object>> rowIterator = result.getRowList().iterator();
                            while (rowIterator.hasNext()) {
                                List<Object> row = rowIterator.next();
                                switch (tableCondition.getOperator()) {
                                    case EQUAL:
                                        if (!row.get(columnIndex).equals(tableCondition.getCollection().get(0))) {
                                            rowIterator.remove();
                                        }
                                        break;
                                    case NOT_EQUAL:
                                        if (row.get(columnIndex).equals(tableCondition.getCollection().get(0))) {
                                            rowIterator.remove();
                                        }
                                        break;
                                    case IN:
                                        Object value = row.get(columnIndex);
                                        boolean isExist = false;
                                        for (Object o : tableCondition.getCollection()) {
                                            if (value.equals(o)) {
                                                isExist = true;
                                                break;
                                            }
                                        }
                                        if (!isExist) {
                                            rowIterator.remove();
                                        }
                                        break;
                                    case NOT_IN:
                                        value = row.get(columnIndex);
                                        isExist = false;
                                        for (Object o : tableCondition.getCollection()) {
                                            if (value.equals(o)) {
                                                isExist = true;
                                                break;
                                            }
                                        }
                                        if (isExist) {
                                            rowIterator.remove();
                                        }
                                        break;
                                }
                            }

                            if (result.getRowList().size() == 0) {
                                throw new NoDataException();
                            }
                        }
                    } else {
                        twoTableCondition.add(tableCondition);
                    }
                }

                //第二步计算：两表关联表达式
                for (TableCondition tableCondition : twoTableCondition) {
                    if (isExistPreviousExpr) {
                        ConditionType condition = tableCondition.getCondition();
                        switch (condition) {
                            case BOOLEAN_AND:
                                selectBuilder.append(" and ");
                                break;
                            case BOOLEAN_OR:
                                selectBuilder.append(" or ");
                                break;
                        }
                    } else {
                        isExistPreviousExpr = true;
                    }

                    Column resultColumn;
                    if (tableCondition.getLeft().getAlias().equals(getTableAlias(right))) {
                        selectBuilder.append(tableCondition.getLeft().getColumn());
                        resultColumn = tableCondition.getRight();
                    } else {
                        selectBuilder.append(tableCondition.getRight().getColumn());
                        resultColumn = tableCondition.getLeft();
                    }
                    selectBuilder.append(" in ("); //目前2表之间只能使用t1.id = t2.id来关联
                    int index = getColumnIndex(resultColumn, result);
                    for (List<Object> values : result.getRowList()) {
                        Object value = values.get(index);
                        if (value instanceof Number) {
                            selectBuilder.append(value);
                        } else {
                            selectBuilder.append("'").append(value).append("'");
                        }
                        selectBuilder.append(",");
                    }
                    selectBuilder.deleteCharAt(selectBuilder.length() - 1);
                    selectBuilder.append(")");
                }

                execMySQL(selectBuilder.toString(), rightColumnList, tableRela.getRela(), twoTableCondition, result, right.getBaseInfo());
                break;
            case REDIS:
                //
                break;
        }
    }

    /**
     * 执行mysql
     *
     * @param sql 生成的单表查询SQL
     * @param rightColumnList 此SQL中查询出来的字段名称集合
     * @param rela 此SQL语句与结果集Result之间的关系
     * @param twoTableCondition 此SQL语句与结果集Result之间的条件
     * @param result 结果集Result
     * @param prop 数据库属性
     *
     * @throws Exception
     */
    private static void execMySQL(String sql, List<Column> rightColumnList, RelaType rela, List<TableCondition> twoTableCondition,
                                  Result result, Properties prop) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = MySQLUtil.getConnection(prop);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            for (Column column : rightColumnList) {
                result.addColumn(column);
            }

            for (TableCondition tableCondition : twoTableCondition) {
                Column resultColumn = null;
                Column rightTableColumn = null;
                Column left = tableCondition.getLeft();
                Column right = tableCondition.getRight();
                for (Column rightColumn : rightColumnList) {
                    if (left.getAlias().equals(rightColumn.getAlias()) && left.getColumn().equals(rightColumn.getColumn())) {
                        resultColumn = right;
                        rightTableColumn = left;

                    }
                    if (right.getAlias().equals(rightColumn.getAlias()) && right.getColumn().equals(rightColumn.getColumn())) {
                        resultColumn = left;
                        rightTableColumn = right;
                    }
                }

                //merge collection
                int columnIndex = getColumnIndex(resultColumn, result);
                Iterator<List<Object>> rowIterator = result.getRowList().iterator();
                while (rowIterator.hasNext()) {
                    List<Object> row = rowIterator.next();
                    resultSet.beforeFirst();
                    boolean isEqual = false;
                    while (resultSet.next()) {
                        Object value = resultSet.getObject(rightTableColumn.getColumn());
                        if (row.get(columnIndex).toString().equals(value.toString())) {
                            for (Column column : rightColumnList) {
                                row.add(resultSet.getObject(column.getColumn()));
                            }
                            isEqual = true;
                        }
                    }
                    switch (rela) {
                        case NONE:
                        case INNER_JOIN:
                            if (!isEqual) {
                                rowIterator.remove();
                            }
                            break;
                        case LEFT_JOIN:
                            if (!isEqual) {
                                for (List<Object> finalRow : result.getRowList()) {
                                    for (int i = 0; i < rightColumnList.size(); i++) {
                                        finalRow.add(null);
                                    }
                                }
                            }
                            break;
                    }

                }
            }
        } catch (SQLException e) {
            throw new Exception("执行MySQL查询时发生异常：" + e.getMessage());
        } finally {
            MySQLUtil.close(statement, connection, resultSet);
        }
    }

    /**
     * 获得字段在结果集的下标
     *
     * @param indexColumn 字段
     * @param result 结果集
     * @return
     */
    private static int getColumnIndex(Column indexColumn, Result result) {
        int index = 0;
        for (Column column : result.getColumnList()) {
            if (indexColumn.getAlias().equals(column.getAlias()) && indexColumn.getColumn().equals(column.getColumn())) {
                break;
            }
            index++;
        }

        return index;
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
            case LEFT_OUTER_JOIN: //LEFT JOIN
                tableRela.setRela(RelaType.LEFT_JOIN);
                parseCondition(tables.getCondition(),  tableRela, null);
                break;
            case JOIN:
            case INNER_JOIN: //INNER JOIN
                tableRela.setRela(RelaType.INNER_JOIN);
                parseCondition(tables.getCondition(), tableRela, null);
                break;
            default:
                throw new Exception("不支持这种表的关联关系：" + tables.getJoinType().toString());
        }
    }

    /**
     * 新增表结构
     *
     * @param tableSource 表的数据源
     * @param tableRela 表的关系
     * @param isLeft 左侧还是右侧的表
     */
    private static void addTable(SQLTableSource tableSource, TableRela tableRela, boolean isLeft) throws Exception {
        SQLExpr tableExpr = ((SQLExprTableSource) tableSource).getExpr();
        String dbName = null;
        String tableName = null;
        //表名只支持 dbName.tableName 或 tableName这2种格式
        if (tableExpr instanceof SQLPropertyExpr) {
            SQLPropertyExpr expr = (SQLPropertyExpr) tableExpr;
            SQLExpr owner = expr.getOwner();
            if (owner != null) {
                if (!(owner instanceof SQLIdentifierExpr)) {
                    throw new Exception("未识别的库名:" + owner.toString());
                } else {
                    dbName = ((SQLIdentifierExpr) owner).getName();
                }
            }
            tableName = expr.getName();
        } else if (tableExpr instanceof SQLIdentifierExpr) {
            tableName = ((SQLIdentifierExpr) tableExpr).getName();
        }

        String alias = tableSource.getAlias();
        Table table = new Table(dbName, tableName, alias);

        //目前多数据源只支持MySQL类型
        table.setDbType(DbType.MYSQL);
        Properties prop = new Properties();
        //TODO 表信息补全
        prop.put("user", "root");
        prop.put("pwd", "123456");
        prop.put("addr", "mysql1.het.com");
        prop.put("port", "3306");
        prop.put("database", "db_device");
        table.setBaseInfo(prop);

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

            if (left instanceof SQLBinaryOpExpr || left instanceof SQLInListExpr) {
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
                            tableCondition.setOperator(OperatorType.EQUAL);
                            break;
                        case NotEqual:
                            throw new Exception("两个表之间禁止使用!=来进行关联查询");
                    }

                    addCondition(tableRela, tableCondition);
                } else {
                    //使用tableCondition.left存放查询条件内的唯一字段
                    SQLExpr valueExpr;
                    if (left instanceof SQLPropertyExpr) {
                        tableCondition.setLeft(parseColumn(left, tableRela));
                        valueExpr = right;
                    } else if (right instanceof SQLPropertyExpr) {
                        tableCondition.setLeft(parseColumn(right, tableRela));
                        valueExpr = left;
                    } else {
                        throw new Exception("SQL中不支持没有字段的纯逻辑表达式:" + where.toString());
                    }
                    switch (where.getOperator()) {
                        case Equality:
                            tableCondition.setOperator(OperatorType.EQUAL);
                            break;
                        case NotEqual:
                            tableCondition.setOperator(OperatorType.NOT_EQUAL);
                    }
                    tableCondition.setCollection(parseCollection(Collections.singletonList(valueExpr)));

                    addCondition(tableRela, tableCondition);
                }
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
            Column column = parseColumn(where.getExpr(), tableRela);

            if (conditionType == null) {
                tableCondition.setCondition(ConditionType.BOOLEAN_AND); //默认值
            } else {
                tableCondition.setCondition(conditionType);
            }

            //使用tableCondition.left存放查询条件内的唯一字段
            tableCondition.setLeft(column);
            if (where.isNot()) {
                tableCondition.setOperator(OperatorType.NOT_IN);
            } else {
                tableCondition.setOperator(OperatorType.IN);
            }
            List<SQLExpr> targetList = where.getTargetList();
            if (targetList.size() > 100) {
                throw new Exception("使用IN ()语句时，括号内的集合数不能超过100");
            }
            tableCondition.setCollection(parseCollection(targetList));

            addCondition(tableRela, tableCondition);
        } else {
            throw new Exception("出现不支持的语法");
        }
    }

    /**
     * 解析表达式集合
     *
     * @param targetList 表达式集合
     * @return
     */
    private static List<Object> parseCollection(List<SQLExpr> targetList) {
        List<Object> collection;
        if (targetList.size() == 1) {
            collection = Collections.singletonList(parseValue(targetList.get(0)));
        } else {
            collection = new ArrayList<>();
            for (SQLExpr sqlExpr : targetList) {
                collection.add(parseValue(sqlExpr));
            }
        }

        return collection;
    }

    /**
     * 解析表达式中的值
     *
     * @param expr 表达式
     * @return
     */
    private static Object parseValue(SQLExpr expr) {
        if (expr instanceof SQLIntegerExpr) {
           return ((SQLIntegerExpr) expr).getNumber();
        } else {
           return ((SQLCharExpr) expr).getText();
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
     * @param isLastCheck true-最后一次检测字段；false-检测一次字段后改变为true
     */
    private static void addCondition(TableRela tableRela, TableCondition tableCondition) {
        TableRela parent = tableRela.getParent();
        Table rightTable = tableRela.getRight();

        if (parent == null) { //不用使用leftTable来做判断，一直递归到parent == null可直接插入条件
            tableRela.addCondition(tableCondition);
        } else {
            String checkAlias = getTableAlias(rightTable);

            Column leftColumn = tableCondition.getLeft();
            Column rightColumn = tableCondition.getRight();
            if ((leftColumn != null && checkAlias.equals(leftColumn.getAlias())) ||
                    (rightColumn != null && checkAlias.equals(rightColumn.getAlias()))) {
                tableRela.addCondition(tableCondition);
            } else {
                addCondition(parent, tableCondition);
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
        if (tableRela.getParent() == null) {
            tableRela.addColumn(column);
        } else {
            String checkAlias = getTableAlias(tableRela.getRight());
            if (checkAlias.equals(column.getAlias())) {
                tableRela.addColumn(column);
            } else {
                addColumn(column, tableRela.getParent());
            }
        }
    }

    /**
     * 获得表的别名
     *
     * @param table 表的对象
     * @return
     */
    private static String getTableAlias(Table table) {
        if (table == null) {
            return null;
        }

        if (table.getAlias() != null) {
            return table.getAlias();
        } else {
            if (table.getDbName() != null) {
                return table.getDbName() + "." + table.getTableName();
            } else {
                return table.getTableName();
            }
        }
    }

}
