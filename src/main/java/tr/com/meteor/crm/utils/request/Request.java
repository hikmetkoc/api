package tr.com.meteor.crm.utils.request;

import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Request implements Serializable {
    private Integer page = null;
    private Integer size = null;
    private Filter filter = null;
    private List<Sort> sorts = null;
    private String search = null;
    private List<Column> columns = null;
    private ExportFileType fileType = null;
    private Long ownerId = null;
    private OwnerType owner = OwnerType.ALL;

    private Long assignerId = null;

    private AssignerType assigner = AssignerType.ALL;

    private Long secondAssignerId = null;

    private SecondAssignerType secondAssigner = SecondAssignerType.ALL;

    private Long otherId = null;

    private OtherType other = OtherType.ALL;


    public static Request build() {
        return new Request();
    }

    public static Request idFilter(Object id) {
        return Request.build().filter(Filter.FilterItem("id", FilterItem.Operator.EQUALS, id));
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Request page(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Request size(Integer size) {
        this.size = size;
        return this;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Request filter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public Request sorts(List<Sort> sorts) {
        this.sorts = sorts;
        return this;
    }

    public Request search(String search) {
        this.search = search;
        return this;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Request columns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    public Request addColumn(Column column) {
        if(columns == null) columns = new ArrayList<>();
        this.columns.add(column);
        return this;
    }

    public ExportFileType getFileType() {
        return fileType;
    }

    public void setFileType(ExportFileType fileType) {
        this.fileType = fileType;
    }

    public Request fileType(ExportFileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Request ownerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public OwnerType getOwner() {
        return owner;
    }

    public void setOwner(OwnerType owner) {
        this.owner = owner;
    }

    public Long getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(Long assignerId) {
        this.assignerId = assignerId;
    }

    public AssignerType getAssigner() {
        return assigner;
    }

    public void setAssigner(AssignerType assigner) {
        this.assigner = assigner;
    }

    public Request assignerId(Long assignerId) {
        this.assignerId = assignerId;
        return this;
    }

    public Long getOtherId() {
        return otherId;
    }

    public void setOtherId(Long otherId) {
        this.otherId = otherId;
    }

    public Request otherId(Long otherId) {
        this.otherId = otherId;
        return this;
    }

    public OtherType getOther() {
        return other;
    }

    public void setOther(OtherType other) {
        this.other = other;
    }


    public Long getSecondAssignerId() {
        return secondAssignerId;
    }

    public void setSecondAssignerId(Long secondAssignerId) {
        this.secondAssignerId = secondAssignerId;
    }

    public SecondAssignerType getSecondAssigner() {
        return secondAssigner;
    }

    public void setSecondAssigner(SecondAssignerType secondAssigner) {
        this.secondAssigner = secondAssigner;
    }
}
