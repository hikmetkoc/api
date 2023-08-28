package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.*;
import java.util.Locale;

@Entity
@IdType(idType = IdType.IdTypeEnum.String)
@EntityMetadataAnn(apiName = "field-metadatas", displayField = "instanceName", title = "Nesne Alan Tanımı", pluralTitle = "Nesne Alan Tanımları")
@Table(indexes = {@Index(columnList = "search")})
public class FieldMetadata extends IdNameAuditingEntity<String> {

    @ManyToOne(optional = false)
    @FieldMetadataAnn(title = "Nesne Tanımı", display = true, priority = 20)
    private EntityMetadata entityMetadata;

    @Column(nullable = false)
    @FieldMetadataAnn(title = "Nesne Alan Tanım Adı", search = true, display = true, priority = 30)
    private String name;

    @FieldMetadataAnn(title = "Başlık", search = true, display = true, priority = 10)
    private String title;

    @FieldMetadataAnn(title = "Görüntü", priority = 40)
    private Boolean display;

    @FieldMetadataAnn(title = "Salt Okunurluk", priority = 50)
    private Boolean readOnly;

    @FieldMetadataAnn(title = "Zorunluluk", priority = 60)
    private Boolean required;

    @FieldMetadataAnn(title = "Varsayılan Değer", priority = 70)
    private String defaultValue;

    @FieldMetadataAnn(title = "Öncelik", priority = 80)
    private Integer priority;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    public String getId() {
        String id = "";
        if (entityMetadata != null) {
            id += entityMetadata.getId();
        }
        id += "_" + name;

        return id;
    }

    public EntityMetadata getEntityMetadata() {
        return entityMetadata;
    }

    public void setEntityMetadata(EntityMetadata entityMetadata) {
        this.entityMetadata = entityMetadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Boolean isReadonly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean isRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        if (StringUtils.isNotBlank(title)) {
            search += " " + title;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
