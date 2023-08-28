package tr.com.meteor.crm.utils.filter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class Specificator {
    private static Predicate lessThan(Root root, CriteriaBuilder cb, String path, Comparable comparable) {
        if (Objects.nonNull(comparable)) {
            return cb.lessThan(toJpaPath(root, path), comparable);
        }
        return null;
    }

    private static Predicate startsWith(Root root, CriteriaBuilder cb, String path, String param) {
        if (Objects.nonNull(param)) {
            return cb.like(cb.lower(toJpaPath(root, path)), param.toLowerCase(Locale.forLanguageTag("tr")) + "%");
        }
        return null;
    }

    private static Predicate contains(Root root, CriteriaBuilder cb, String path, String param) {
        if (Objects.nonNull(param)) {
            return cb.like(cb.lower(toJpaPath(root, path)), "%" + param.toLowerCase(Locale.forLanguageTag("tr")) + "%");
        }
        return null;
    }

    private static Predicate endsWith(Root root, CriteriaBuilder cb, String path, String param) {
        if (Objects.nonNull(param)) {
            return cb.like(cb.lower(toJpaPath(root, path)), "%" + param.toLowerCase(Locale.forLanguageTag("tr")));
        }
        return null;
    }

    private static Predicate lessThanOrEqualsTo(Root root, CriteriaBuilder cb, String path, Comparable comparable) {
        if (Objects.nonNull(comparable)) {
            return cb.lessThanOrEqualTo(toJpaPath(root, path), comparable);
        }
        return null;
    }

    private static Predicate greaterThan(Root root, CriteriaBuilder cb, String path, Comparable comparable) {
        if (Objects.nonNull(comparable)) {
            return cb.greaterThan(toJpaPath(root, path), comparable);
        }
        return null;
    }

    private static Predicate search(Root root, CriteriaBuilder cb, String path, String search) {
        return cb.like(toJpaPath(root, path), "%" + search.toLowerCase(new Locale("tr", "TR")) + "%");
    }

    //private static Predicate between(Root root, CriteriaBuilder cb, String path, Object values) {
    //    if (values instanceof List) {
    //        cb.bet
    //        return cb.between(root.get("date"), ((List)values).get(0), ((List)values).get(1));
    //    } else if (values instanceof Object[]) {
    //        lessFilter.setValue(((Object[]) filterItem.getValue())[0]);
    //        greaterFilter.setValue(((Object[]) filterItem.getValue())[1]);
    //    }
    //    return cb.between(root.get("date"), dateBefore, dateAfter);
    //}

    private static Predicate greaterThanOrEqualsTo(Root root, CriteriaBuilder cb, String path, Comparable comparable) {
        if (Objects.nonNull(comparable)) {
            return cb.greaterThanOrEqualTo(toJpaPath(root, path), comparable);
        }
        return null;
    }

    private static Predicate equalPredicate(Root root, CriteriaBuilder cb, String path, Object value) {
        return value == null ? cb.isNull(toJpaPath(root, path)) : cb.equal(toJpaPath(root, path), value);
    }

    private static Predicate in(Root root, CriteriaBuilder cb, String path, Collection collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            return cb.in(toJpaPath(root, path)).value(collection);
        }
        return null;
    }

    private static Predicate specified(Root root, CriteriaBuilder cb, String path, boolean value) {
        if (value) {
            return cb.isNotNull(toJpaPath(root, path));
        } else {
            return cb.isNull(toJpaPath(root, path));
        }
    }

    private static <ENTITY> Path<ENTITY> toJpaPath(Root root, String stringPath) {
        String[] pathParts = StringUtils.split(stringPath, '.');

        assert pathParts != null && pathParts.length > 0 : "Path cannot be empty";

        Path jpaPath = null;
        for (String eachPathPart : pathParts) {
            if (jpaPath == null) {
                jpaPath = root.get(eachPathPart);
            } else {
                jpaPath = jpaPath.get(eachPathPart);
            }
        }

        return jpaPath;
    }

    private static <ENTITY> Specification<ENTITY> filterItemToSpec(FilterItem filterItem) {
        if (filterItem == null) return Specification.where(null);

        return new Specification<ENTITY>() {
            @Override
            public Predicate toPredicate(Root<ENTITY> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return filterItemToPredicate(filterItem, root, cb);
            }
        };
    }

    public static <ENTITY> Specification<ENTITY> filterToSpec(Filter filter) {
        if (filter == null || (filter.getOperator() != Filter.Operator.FILTER_ITEM && (filter.getFilterList() == null || filter.getFilterList().isEmpty())))
            return Specification.where(null);

        switch (filter.getOperator()) {
            case AND_GROUP: {
                Specification<ENTITY> specification = Specification.where(null);
                for (Filter f : filter.getFilterList()) {
                    specification = specification.and(filterToSpec(f));
                }

                return specification;
            }
            case OR_GROUP: {
                Specification<ENTITY> specification = Specification.where(null);
                for (Filter f : filter.getFilterList()) {
                    specification = specification.or(filterToSpec(f));
                }

                return specification;
            }
            case FILTER_ITEM: {
                FilterItem filterItem = filter.getFilterItem();

                if (filterItem.getOperator().equals(FilterItem.Operator.BETWEEN)) {
                    return filterToSpec(betweenFilterItemToAndFilter(filter));
                }

                Specification<ENTITY> specification = Specification.where(null);
                return specification.and(filterItemToSpec(filterItem));
            }
            default:
                return Specification.where(null);
        }
    }

    private static Predicate filterItemToPredicate(FilterItem filterItem, Root root, CriteriaBuilder cb) {
        switch (filterItem.getOperator()) {
            case EQUALS:
                return equalPredicate(root, cb, filterItem.getFieldName(), filterItem.getValue());
            case GREATER_OR_EQUAL_THAN:
                return greaterThanOrEqualsTo(root, cb, filterItem.getFieldName(), (Comparable) filterItem.getValue());
            case GREATER_THAN:
                return greaterThan(root, cb, filterItem.getFieldName(), (Comparable) filterItem.getValue());
            case BETWEEN:
                break;
            case IN:
                return in(root, cb, filterItem.getFieldName(), (Collection) filterItem.getValue());
            case LESS_OR_EQUAL_THAN:
                return lessThanOrEqualsTo(root, cb, filterItem.getFieldName(), (Comparable) filterItem.getValue());
            case LESS_THAN:
                return lessThan(root, cb, filterItem.getFieldName(), (Comparable) filterItem.getValue());
            case SPECIFIED:
                break;
            case STARTS_WITH:
                return startsWith(root, cb, filterItem.getFieldName(), (String) filterItem.getValue());
            case CONTAINS:
                return contains(root, cb, filterItem.getFieldName(), (String) filterItem.getValue());
            case ENDS_WITH:
                return endsWith(root, cb, filterItem.getFieldName(), (String) filterItem.getValue());
            case SEARCH:
                return search(root, cb, filterItem.getFieldName(), (String) filterItem.getValue());
        }

        return null;
    }

    public static Predicate filterToPredicate(Filter filter, Root root, CriteriaBuilder cb) {
        if (filter == null || (filter.getOperator() != Filter.Operator.FILTER_ITEM && (filter.getFilterList() == null || filter.getFilterList().isEmpty())))
            return cb.and();

        switch (filter.getOperator()) {
            case AND_GROUP: {
                Predicate[] predicates = filter.getFilterList()
                    .stream()
                    .map(x -> filterToPredicate(x, root, cb))
                    .collect(Collectors.toList())
                    .toArray(new Predicate[filter.getFilterList().size()]);

                return cb.and(predicates);
            }
            case OR_GROUP: {
                Predicate[] predicates = filter.getFilterList()
                    .stream()
                    .map(x -> filterToPredicate(x, root, cb))
                    .collect(Collectors.toList())
                    .toArray(new Predicate[filter.getFilterList().size()]);

                return cb.or(predicates);
            }
            case FILTER_ITEM: {
                FilterItem filterItem = filter.getFilterItem();

                Predicate predicate = null;

                if (filterItem.getOperator().equals(FilterItem.Operator.BETWEEN)) {
                    return filterToPredicate(betweenFilterItemToAndFilter(filter), root, cb);
                }

                return cb.and(filterItemToPredicate(filterItem, root, cb));
            }
            default:
                return cb.and();
        }
    }

    private static void cleanFilter(Filter filter) {
        switch (filter.getOperator()) {
            case AND_GROUP:
            case OR_GROUP: {
                for (Filter f : filter.getFilterList()) {
                    cleanFilter(f);
                }
            }
            case FILTER_ITEM: {
                if (filter.getFilterItem().getOperator().equals(FilterItem.Operator.BETWEEN)) {
                    betweenFilterItemToAndFilter(filter);
                }
            }
        }
    }

    private static Filter betweenFilterItemToAndFilter(Filter filter) {
        FilterItem filterItem = filter.getFilterItem();

        FilterItem lessFilterItem = new FilterItem(filterItem.getFieldName(), filterItem.getOperator().name(), filterItem.getValue());
        FilterItem greaterFilterItem = new FilterItem(filterItem.getFieldName(), filterItem.getOperator().name(), filterItem.getValue());

        if (filterItem.getValue() instanceof List) {
            lessFilterItem.setOperator(FilterItem.Operator.LESS_OR_EQUAL_THAN);
            lessFilterItem.setValue(((List) filterItem.getValue()).get(0));
            greaterFilterItem.setOperator(FilterItem.Operator.GREATER_OR_EQUAL_THAN);
            greaterFilterItem.setValue(((List) filterItem.getValue()).get(1));
        } else if (filterItem.getValue().getClass().isArray()) {
            lessFilterItem.setOperator(FilterItem.Operator.LESS_OR_EQUAL_THAN);
            lessFilterItem.setValue(((Object[]) filterItem.getValue())[0]);
            greaterFilterItem.setOperator(FilterItem.Operator.GREATER_OR_EQUAL_THAN);
            greaterFilterItem.setValue(((Object[]) filterItem.getValue())[1]);
        }

        Filter lessFilter = Filter.FilterItem(lessFilterItem);
        Filter greaterFilter = Filter.FilterItem(greaterFilterItem);

        return Filter.And(lessFilter, greaterFilter);
    }
}
