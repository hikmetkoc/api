package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "personal_contracts", displayField = "instanceName", title = "Personel Sözleşmeleri", pluralTitle = "Personel Sözleşmeleri",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class PersonalContract extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Sözleşme Adı", search = true, readOnly = true, priority = 10, active = false)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Yükleyen", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;
    @ManyToOne
    @FieldMetadataAnn(title = "Şirket", defaultValue = "Sirketler_Meteor", display = true, priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Sirketler")
    private AttributeValue sirket;

    @FieldMetadataAnn(title = "Açıklama", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Sözleşme Adı", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String sozlesme;

    @FieldMetadataAnn(title = "Word Belgesi", priority = 110)
    private String belge;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public AttributeValue getSirket() {
        return sirket;
    }

    public void setSirket(AttributeValue sirket) {
        this.sirket = sirket;
    }

    public String getBelge() {
        return belge;
    }

    public void setBelge(String belge) {
        this.belge = belge;
    }

    public String getSozlesme() {
        return sozlesme;
    }

    public void setSozlesme(String sozlesme) {
        this.sozlesme = sozlesme;
    }
}
