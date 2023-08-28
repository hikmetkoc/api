package tr.com.meteor.crm.utils.filter;

import java.io.Serializable;

public class FilterItem implements Serializable {
    private String fieldName;
    private Operator operator;
    private Object value;

    public FilterItem() {
    }

    public FilterItem(String fieldName, String operator, Object value) {
        this.fieldName = fieldName;
        this.operator = Operator.valueOf(operator);
        this.value = value;
    }

    public FilterItem(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setOperator(String operator) {
        this.operator = Operator.valueOf(operator);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("'%s'%s'%s'", fieldName, operator, value);
    }

    public enum Operator {
        EQUALS,
        GREATER_OR_EQUAL_THAN,
        GREATER_THAN,
        BETWEEN,
        IN,
        LESS_OR_EQUAL_THAN,
        LESS_THAN,
        SPECIFIED,
        CONTAINS,
        STARTS_WITH,
        ENDS_WITH,
        SEARCH
    }
}
