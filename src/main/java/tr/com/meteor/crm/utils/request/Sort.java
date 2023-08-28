package tr.com.meteor.crm.utils.request;

import java.io.Serializable;

public class Sort implements Serializable {
    private String sortBy;
    private SortOrder sortOrder;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
