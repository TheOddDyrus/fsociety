package com.thomax.ast.model;

import java.util.List;

public class TableCondition {

    private ConditionType condition;

    private Column left;

    private Column right;

    private OperatorType operator;

    private List<Object> collection;

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

    public List<Object> getCollection() {
        return collection;
    }

    public void setCollection(List<Object> collection) {
        this.collection = collection;
    }

}
