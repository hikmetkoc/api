package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "documents", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Dosya", pluralTitle = "Dosyalar",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Document extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Ekleyen")
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;
    @FieldMetadataAnn(title = "Konu", search = true, priority = 3, filterable = true)
    private String subject;

    @ManyToOne
    @FieldMetadataAnn(title = "Şirket", defaultValue = "Dokuman_Sirketleri_Meteor", display = true, priority = 2, search = true, filterable = true, required = true)
    @AttributeValueValidate(attributeId = "Dokuman_Sirketleri")
    private AttributeValue sirket;

    @FieldMetadataAnn(title = "Açıklama", priority = 5)
    private String description;
    @Formula("subject")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }


    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public AttributeValue getSirket() {
        return sirket;
    }

    public void setSirket(AttributeValue sirket) {
        this.sirket = sirket;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
