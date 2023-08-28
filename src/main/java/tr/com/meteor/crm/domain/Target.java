package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "targets", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Hedef", pluralTitle = "Hedefler", masterPath = "owner.id", ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search"), @Index(columnList = "termStart")})
public class Target extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Sahip", display = true, priority = 10)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @FieldMetadataAnn(title = "Dönem", display = true, priority = 20)
    private Instant termStart;

    @FieldMetadataAnn(title = "Tip", display = true, priority = 30)
    @AttributeValueValidate(attributeId = "Hed_Tip")
    @ManyToOne
    private AttributeValue type;

    @FieldMetadataAnn(title = "Hedef", display = true, priority = 40)
    private Double amount;

    @FieldMetadataAnn(title = "Real", display = true, priority = 50)
    private Double realizedAmount;

    @Formula("id")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Instant getTermStart() {
        return termStart;
    }

    public void setTermStart(Instant termStart) {
        this.termStart = termStart;
    }

    public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRealizedAmount() {
        return realizedAmount;
    }

    public void setRealizedAmount(Double realizedAmount) {
        this.realizedAmount = realizedAmount;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }
}
