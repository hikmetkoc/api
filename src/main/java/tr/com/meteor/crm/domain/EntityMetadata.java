package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Locale;

@Entity
@IdType(idType = IdType.IdTypeEnum.String)
@EntityMetadataAnn(apiName = "entity-metadatas", displayField = "instanceName", title = "Nesne Tanımı", pluralTitle = "Nesne Tanımları")
@Table(indexes = {@Index(columnList = "search")})
public class EntityMetadata extends IdNameAuditingEntity<String> {

    @Column(nullable = false, unique = true)
    @FieldMetadataAnn(required = true, title = "Nesne Tanım Adı", search = true, display = true, priority = 20)
    private String name;

    @FieldMetadataAnn(required = true, title = "Görüntü Alanı", priority = 30)
    private String displayField;

    @FieldMetadataAnn(required = true, title = "Varsayılan Sıralama", priority = 40)
    private String defaultSort;

    @FieldMetadataAnn(title = "Başlık", search = true, display = true, priority = 10)
    private String title;

    @FieldMetadataAnn(title = "Başlıklar", search = true, priority = 11)
    private String pluralTitle;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.id = name;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public String getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(String defaultSort) {
        this.defaultSort = defaultSort;
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

    @Override
    public String toString() {
        return "EntityMetadata{" +
            "name='" + name + '\'' +
            '}';
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
