package tr.com.meteor.crm.utils.filter;

import org.apache.commons.lang3.StringUtils;
import tr.com.meteor.crm.domain.IdEntity;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {
    public static Filter addSearchColumns(Class<? extends IdEntity> aClass, Filter filter) {
        switch (filter.getOperator()) {
            case ROOT:
            case AND_GROUP:
            case OR_GROUP:
                List<Filter> filters = new ArrayList<>();
                for (Filter f : filter.getFilterList()) {
                    filters.add(addSearchColumns(aClass, f));
                }

                filter.setFilterList(filters);
                break;
            case FILTER_ITEM:
                if (filter.getFilterItem().getOperator() == FilterItem.Operator.SEARCH) {
                    filter.getFilterItem().setFieldName("search");
                }

                break;
        }

        return filter;
    }

    public static Filter createSearchFilter(Class<? extends IdEntity> aClass, String search) {
        if (StringUtils.isBlank(search)) return null;

        return addSearchColumns(aClass, Filter.FilterItem("", FilterItem.Operator.SEARCH, search));
    }
}
