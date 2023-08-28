package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "announcements", displayField = "instanceName", title = "Duyuru", pluralTitle = "Duyurular")
@Table(indexes = {@Index(columnList = "search")})
public class Announcement extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "Başlık", search = true, display = true, priority = 10)
    private String title;

    @FieldMetadataAnn(title = "Açıklama", priority = 40)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Aktif", display = true, priority = 20)
    private Boolean active;

    @Formula("title")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    @ManyToOne
    @FieldMetadataAnn(required =  true, title = "Tip", display = true, priority = 30)
    @AttributeValueValidate(attributeId = "Duy_Tip")
    private AttributeValue type;

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(title)) {
            search += " " + title;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
