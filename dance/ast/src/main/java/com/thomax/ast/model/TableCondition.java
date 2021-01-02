package com.thomax.ast.model;

public class TableCondition {

    private ConditionType condition;

    private Column left;

    private Column right;

    private OperatorType operator;

    private String expr;

    public ConditionType getCondition() {
        return condition;
    }

    public void setCondition(ConditionType condition) {
        this.condition = condition;
    }

    public Column getLeft() {
        return left;
    }

    public void setLeft(Column left) {
        this.left = left;
    }

    public Column getRight() {
        return right;
    }

    public void setRight(Column right) {
        this.right = right;
    }

    public OperatorType getOperator() {
        return operator;
    }

    public void setOperator(OperatorType operator) {
        this.operator = operator;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

}
