package tr.com.meteor.crm.utils.filter;

import org.hibernate.query.criteria.internal.path.PluralAttributePath;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import tr.com.meteor.crm.utils.request.Column;
import tr.com.meteor.crm.utils.request.ColumnType;
import tr.com.meteor.crm.utils.request.Request;
import tr.com.meteor.crm.utils.request.Sort;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import java.util.*;

public class CbUtils {
    private static List<String> getPaths(Filter filter) {
        List<String> paths = new ArrayList<>();

        if (filter == null) return paths;

        switch (filter.getOperator()) {
            case ROOT:
            case AND_GROUP:
            case OR_GROUP:
                for (Filter f : filter.getFilterList()) {
                    paths.addAll(getPaths(f));
                }

                break;
            case FILTER_ITEM:
                paths.add(filter.getFilterItem().getFieldName());

                break;
        }

        return paths;
    }

    public static List<String> getPaths(Request request) {
        List<String> paths = new ArrayList<>(getPaths(request.getFilter()));

        if (request.getColumns() != null && !request.getColumns().isEmpty()) {
            request.getColumns().forEach(x -> paths.add(x.getName()));
        }

        if (request.getSorts() != null && !request.getSorts().isEmpty()) {
            request.getSorts().forEach(x -> paths.add(x.getSortBy()));
        }

        return paths;
    }

    public static Map<String, Join> getJoins(Root root, List<String> paths) {
        Map<String, Join> joinMap = new LinkedHashMap<>();

        for (String path : paths) {
            joinMap = CbUtils.getJoins(root, path, joinMap);
        }

        return joinMap;
    }

    public static Map<String, Join> getJoins(Root root, String path, Map<String, Join> joinMap) {
        Join join = null;
        List<String> currentPathParts = new ArrayList<>();
        for (String pathPart : path.split("\\.")) {
            Path s = join == null ? root.get(pathPart) : join.get(pathPart);

            if (s instanceof SingularAttributePath) {
                SingularAttributePath attributePath = (SingularAttributePath) s;
                if (attributePath.getAttribute().getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC) {
                    currentPathParts.add(pathPart);
                    String currentPath = String.join(".", currentPathParts);

                    if (joinMap.containsKey(currentPath)) {
                        join = joinMap.get(currentPath);
                    } else {
                        join = join == null ? root.join(pathPart, JoinType.LEFT) : join.join(pathPart, JoinType.LEFT);
                        joinMap.put(currentPath, join);
                    }
                }
            } else if (s instanceof PluralAttributePath) {
                PluralAttributePath attributePath = (PluralAttributePath) s;

                if (attributePath.getAttribute().getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC) {
                    // TODO: 9/17/2019 list ? set? map? collection?
                    currentPathParts.add(pathPart);
                    String currentPath = String.join(".", currentPathParts);

                    if (joinMap.containsKey(currentPath)) {
                        join = joinMap.get(currentPath);
                    } else {
                        join = join == null ? root.joinList(pathPart, JoinType.LEFT) : join.joinList(pathPart, JoinType.LEFT);
                        joinMap.put(currentPath, join);
                    }
                }
            }
        }

        return joinMap;
    }

    public static List<Selection<?>> columnsToSelections(Request request, CriteriaBuilder criteriaBuilder, Root root, Map<String, Join> joinMap) {
        List<Selection<?>> selections = new ArrayList<>();
        if (request.getColumns() != null && !request.getColumns().isEmpty()) {
            for (Column column : request.getColumns()) {
                Path selection;

                if (column.getName().contains(".")) {
                    String joinName = column.getName().substring(0, column.getName().lastIndexOf("."));
                    selection = joinMap.get(joinName).get(column.getName().replace(joinName + ".", ""));
                } else {
                    selection = root.get(column.getName());
                }

                switch (column.getColumnType()) {
                    case DEFAULT:
                        selections.add(selection.alias(column.getTitle()));
                        break;
                    case COUNT:
                        selections.add(criteriaBuilder.count(selection).alias(column.getTitle()));
                        break;
                    case SUM:
                        selections.add(criteriaBuilder.sum(selection).alias(column.getTitle()));
                        break;
                    case MIN:
                        selections.add(criteriaBuilder.min(selection).alias(column.getTitle()));
                        break;
                    case MAX:
                        selections.add(criteriaBuilder.max(selection).alias(column.getTitle()));
                        break;
                    case AVG:
                        selections.add(criteriaBuilder.avg(selection).alias(column.getTitle()));
                        break;
                }
            }
        }

        return selections;
    }

    public static List<Order> createOrders(Request request, CriteriaBuilder criteriaBuilder, Root root, Map<String, Join> joinMap) {
        List<Order> orders = new ArrayList<>();
        if (request.getSorts() != null && !request.getSorts().isEmpty()) {
            for (Sort sort : request.getSorts()) {
                Path path;

                if (sort.getSortBy().contains(".")) {
                    String joinName = sort.getSortBy().substring(0, sort.getSortBy().lastIndexOf("."));
                    path = joinMap.get(joinName).get(sort.getSortBy().replace(joinName + ".", ""));
                } else {
                    path = root.get(sort.getSortBy());
                }

                switch (sort.getSortOrder()) {
                    case ASC:
                        orders.add(criteriaBuilder.asc(path));
                        break;
                    case DESC:
                        orders.add(criteriaBuilder.desc(path));
                        break;
                }
            }
        }

        return orders;
    }

    public static List<Map<String, Object>> tupleListToMap(List<Tuple> list) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (Tuple tuple : list) {
            Map<String, Object> map = new LinkedHashMap<>();

            for (TupleElement element : tuple.getElements()) {
                map.put(element.getAlias(), tuple.get(element.getAlias()));
            }

            mapList.add(map);
        }

        return mapList;
    }

    public static List<Expression<?>> columnsToGroupBy(Request request, CriteriaQuery<Tuple> query, Root root, Map<String, Join> joinMap) {
        List<Expression<?>> selections = new ArrayList<>();
        if (request.getColumns() != null && !request.getColumns().isEmpty()) {
            for (Column column : request.getColumns()) {
                Path selection;

                if (column.getName().contains(".")) {
                    String joinName = column.getName().substring(0, column.getName().lastIndexOf("."));
                    selection = joinMap.get(joinName).get(column.getName().replace(joinName + ".", ""));
                } else {
                    selection = root.get(column.getName());
                }

                if (column.getColumnType() == ColumnType.DEFAULT) {
                    selections.add(selection);
                }
            }
        }

        return selections;
    }
}
