package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@EntityMetadataAnn(apiName = "proj_task_officers", displayField = "instanceName", title = "Proje Görevi Sorumlusu", pluralTitle = "Proje Görevi Sorumluları",
    ownerPath = "owner.id")
@Table(indexes = {@Index(columnList = "search")})
public class ProjTaskOfficer extends IdNameAuditingEntity<UUID> {

    @ManyToOne
    @FieldMetadataAnn(title = "Sorumlu Kişi", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @FieldMetadataAnn(title = "Yetkisi", defaultValue = "ProjTaskOfficer_Auth_Officer", display = true, filterable = true)
    @AttributeValueValidate(attributeId = "ProjTaskOfficer_Auth")
    private AttributeValue authority;

    @FieldMetadataAnn(title = "Açıklama", priority = 100)
    @Column(length = 2048)
    private String description;

    @ManyToOne
    @FieldMetadataAnn(title = "Bağlı Olduğu Proje Görevi", display = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private ProjTask projtask;

    @Formula("description")
    @FieldMetadataAnn(title = "Başlık", active = false)
    private String instanceName;

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

        if (StringUtils.isNotBlank(description)) {
            search += " " + description;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public AttributeValue getAuthority() {
        return authority;
    }

    public void setAuthority(AttributeValue authority) {
        this.authority = authority;
    }

    public ProjTask getProjtask() {
        return projtask;
    }

    public void setProjtask(ProjTask projtask) {
        this.projtask = projtask;
    }
}
