package tr.com.meteor.crm.utils.filter;

import tr.com.meteor.crm.utils.metadata.MetadataReader;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.utils.metadata.EntityMetadataFull;
import tr.com.meteor.crm.utils.metadata.FieldMetadataFull;
import tr.com.meteor.crm.utils.jasper.rest.errors.WrongFilterPathException;

import java.io.Serializable;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Filter implements Serializable {
    private Operator operator;
    private List<Filter> filterList = new ArrayList<Filter>();
    private FilterItem filterItem;

    public Filter() {
    }

    public static Filter And(Filter... filters) {
        Filter filter = new Filter();
        filter.operator = Operator.AND_GROUP;
        filter.filterList.addAll(Arrays.asList(filters));

        return filter;
    }

    public static Filter Or(Filter... filters) {
        Filter filter = new Filter();
        filter.operator = Operator.OR_GROUP;
        filter.filterList.addAll(Arrays.asList(filters));

        return filter;
    }

    public static Filter FilterItem(FilterItem filterItem) {
        Filter filter = new Filter();
        filter.operator = Operator.FILTER_ITEM;
        filter.filterItem = filterItem;

        return filter;
    }

    public static Filter FilterItem(String fieldName, String operator, Object value) {
        Filter filter = new Filter();
        filter.operator = Operator.FILTER_ITEM;
        filter.filterItem = new FilterItem(fieldName, operator, value);

        return filter;
    }

    public static Filter FilterItem(String fieldName, FilterItem.Operator operator, Object value) {
        Filter filter = new Filter();
        filter.operator = Operator.FILTER_ITEM;
        filter.filterItem = new FilterItem(fieldName, operator, value);

        return filter;
    }

    private static void validatePath(Class<? extends IdEntity> aClass, Filter filter) throws Exception {
        if (filter == null || filter.getOperator() == null) return;

        if (!MetadataReader.getClassMetadataList().containsKey(aClass.getName())) {
            throw new Exception(MessageFormat.format("Object {0} not found in metadata!", aClass.getName()));
        }

        switch (filter.getOperator()) {
            case ROOT:
            case AND_GROUP:
            case OR_GROUP:
                for (Filter f : filter.getFilterList()) {
                    validatePath(aClass, f);
                }

                break;
            case FILTER_ITEM:
                validatePath(aClass, filter.getFilterItem());
                break;
        }
    }

    private static void validatePath(Class<? extends IdEntity> aClass, FilterItem filterItem) {
        if (filterItem.getOperator() == FilterItem.Operator.SEARCH) return;

        EntityMetadataFull currentMetadata = MetadataReader.getClassMetadataList().get(aClass.getName());
        String[] pathParts = filterItem.getFieldName().split("\\.");

        String previousClassName = "";
        for (String pathPart : pathParts) {
            if (currentMetadata == null || !currentMetadata.getFieldMetadataMap().containsKey(pathPart)) {
                throw new WrongFilterPathException(previousClassName, filterItem.getFieldName(), pathPart);
            }

            FieldMetadataFull fieldMetadataFull = currentMetadata.getFieldMetadataMap().get(pathPart);

            if (fieldMetadataFull.getJavaType().startsWith("tr.com.meteor.crm.domain")) {
                previousClassName = currentMetadata.getFieldMetadataMap().get(pathPart).getJavaType();
                currentMetadata = MetadataReader.getClassMetadataList().get(fieldMetadataFull.getJavaType());
            } else {
                currentMetadata = null;
            }
        }
    }

    private static Filter fixTypes(Class<? extends IdEntity> aClass, Filter filter) throws Exception {
        switch (filter.getOperator()) {
            case ROOT:
            case AND_GROUP:
            case OR_GROUP:
                List<Filter> filters = new ArrayList<>();
                for (Filter f : filter.getFilterList()) {
                    filters.add(fixTypes(aClass, f));
                }

                filter.setFilterList(filters);
                break;
            case FILTER_ITEM:
                filter.setFilterItem(fixTypes(aClass, filter.getFilterItem()));
                break;
        }

        return filter;
    }

    private static FilterItem fixTypes(Class<? extends IdEntity> aClass, FilterItem filterItem) throws Exception {
        if (filterItem.getOperator() == FilterItem.Operator.SEARCH) return filterItem;

        FieldMetadataFull fieldMetadataFull = MetadataReader.getCrmFieldMetadataFromPath(aClass, filterItem.getFieldName());

        if (fieldMetadataFull.getJavaType().endsWith(".AttributeValue")) {
            filterItem.setFieldName(filterItem.getFieldName() + ".id");
        }

        if (filterItem.getOperator() == FilterItem.Operator.IN && filterItem.getValue() instanceof ArrayList) {
            ArrayList<Object> list = new ArrayList<>();
            for (Object o : (ArrayList) filterItem.getValue()) {
                list.add(fixType(fieldMetadataFull, o));
            }

            filterItem.setValue(list);
        } else if (filterItem.getOperator() != FilterItem.Operator.IN && !(filterItem.getValue() instanceof ArrayList)) {
            filterItem.setValue(fixType(fieldMetadataFull, filterItem.getValue()));
        }

        return filterItem;
    }

    private static Object fixType(FieldMetadataFull fieldMetadataFull, Object value) {
        switch (fieldMetadataFull.getType()) {
            case "UUID":
                if (value instanceof String) {
                    value = UUID.fromString(String.valueOf(value));
                }

                break;
            case "Long":
                if (value instanceof String) {
                    value = Long.parseLong(String.valueOf(value));
                }

                if (value instanceof Integer) {
                    value = Long.parseLong(String.valueOf(value));
                }

                break;
            case "Integer":
                if (value instanceof String) {
                    value = Integer.parseInt(String.valueOf(value));
                }

                break;
            case "Instant":
                if (value instanceof String) {
                    value = Instant.parse(String.valueOf(value));
                }

                break;
        }

        return value;
    }

    public static Filter equalsTo(String fieldName, Object value) {
        return FilterItem(new FilterItem(fieldName, FilterItem.Operator.EQUALS, value));
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public List<Filter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }

    public FilterItem getFilterItem() {
        return filterItem;
    }

    public void setFilterItem(FilterItem filterItem) {
        this.filterItem = filterItem;
    }

    public void validateAndFix(Class<? extends IdEntity> entityClass) throws Exception {
        validatePath(entityClass, this);
        fixTypes(entityClass);
    }

    private void fixTypes(Class<? extends IdEntity> aClass) throws Exception {
        switch (operator) {
            case ROOT:
            case AND_GROUP:
            case OR_GROUP:
                List<Filter> filters = new ArrayList<>();
                for (Filter f : filterList) {
                    filters.add(fixTypes(aClass, f));
                }

                filterList = filters;
                break;
            case FILTER_ITEM:
                filterItem = fixTypes(aClass, filterItem);
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        String op = "";
        switch (operator) {
            case AND_GROUP:
                op = " && ";
                break;
            case OR_GROUP:
                op = " || ";
                break;
            case FILTER_ITEM:
                return filterItem.toString();
        }

        for (Filter filter : filterList) {
            stringBuilder.append(filter.toString());
            stringBuilder.append(op);
        }

        String result = stringBuilder.toString();

        return result.substring(0, result.length() - 4) + ")";
    }

    public enum Operator {
        ROOT, AND_GROUP, OR_GROUP, FILTER_ITEM
    }
}
