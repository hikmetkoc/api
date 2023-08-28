package tr.com.meteor.crm.utils.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.utils.request.Sort;
import tr.com.meteor.crm.repository.GenericIdEntityRepository;
import tr.com.meteor.crm.trigger.Trigger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityMetadataFull {

    private String name;
    private String apiName;
    private String javaType;
    private String displayField;

    private Map<String, FieldMetadataFull> fieldMetadataMap = new HashMap<>();

    private int size;
    private List<Sort> sorts = new ArrayList<>();

    private String title;
    private String pluralTitle;

    private String ownerPath;

    private String assignerPath;

    private String secondAssignerPath;
    private String otherPath;

    @JsonIgnore
    private Class<? extends IdEntity> entityClass;

    @JsonIgnore
    private Class<? extends Trigger<? extends IdEntity, ? extends Serializable, ? extends GenericIdEntityRepository>> triggerClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public Map<String, FieldMetadataFull> getFieldMetadataMap() {
        return fieldMetadataMap;
    }

    public void setFieldMetadataMap(Map<String, FieldMetadataFull> fieldMetadataMap) {
        this.fieldMetadataMap = fieldMetadataMap;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPluralTitle() {
        return pluralTitle;
    }

    public void setPluralTitle(String pluralTitle) {
        this.pluralTitle = pluralTitle;
    }

    public Class<? extends IdEntity> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<? extends IdEntity> entityClass) {
        this.entityClass = entityClass;
    }

    public String getOwnerPath() {
        return ownerPath;
    }

    public void setOwnerPath(String ownerPath) {
        this.ownerPath = ownerPath;
    }

    public String getAssignerPath() {
        return assignerPath;
    }

    public void setAssignerPath(String assignerPath) {
        this.assignerPath = assignerPath;
    }
    public Class<? extends Trigger<? extends IdEntity, ? extends Serializable, ? extends GenericIdEntityRepository>> getTriggerClass() {
        return triggerClass;
    }

    public void setTriggerClass(Class<? extends Trigger<? extends IdEntity, ? extends Serializable, ? extends GenericIdEntityRepository>> triggerClass) {
        this.triggerClass = triggerClass;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("name", name)
            .append("packageName", javaType)
            .append("apiName", apiName)
            .append("fieldMetadataMap", fieldMetadataMap)
            .toString();
    }

    public String getOtherPath() {
        return otherPath;
    }

    public void setOtherPath(String otherPath) {
        this.otherPath = otherPath;
    }

    public String getSecondAssignerPath() {
        return secondAssignerPath;
    }

    public void setSecondAssignerPath(String secondAssignerPath) {
        this.secondAssignerPath = secondAssignerPath;
    }
}
