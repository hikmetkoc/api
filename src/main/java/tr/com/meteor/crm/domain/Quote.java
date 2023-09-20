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
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "quotes", displayField = "instanceName", title = "Araç", pluralTitle = "Raporlar",
    ownerPath = "owner.id", assignerPath = "assigner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Quote extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Rapor Başlığı", search = true, readOnly = false, priority = 12, active = true)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Hazırlayan", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @FieldMetadataAnn(title = "Üst Yönetici", display = true, priority = 0, readOnly = false, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;
    @FieldMetadataAnn(title = "Açıklama", priority = 140)
    @Column(length = 2048)
    private String description;

    @Formula("name")
    @FieldMetadataAnn(title = "Başlık")
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
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
}
