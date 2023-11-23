package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.attributevalues.MotionSumsStatus;
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
@EntityMetadataAnn(apiName = "motionsumss", displayField = "instanceName", title = "Maliyet Yeri", pluralTitle = "Maliyet Yerleri")
@Table(indexes = {@Index(columnList = "search")})
public class MotionSums extends IdNameAuditingEntity<UUID> {

    @ManyToOne
    @FieldMetadataAnn(title = "Maliyet Yeri Sahibi", display = true, priority = 5)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @FieldMetadataAnn(title = "Tedarikçi", display = true, priority = 30)
    private Customer customer;

    @ManyToOne
    @FieldMetadataAnn(title = "Üst Cari", priority = 40, required = true)
    @JsonIgnoreProperties("customer")
    private Customer parent;

    @FieldMetadataAnn(title = "Açıklama", priority = 100)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Borç")
    private BigDecimal loan = BigDecimal.ZERO;

    @FieldMetadataAnn(title = "Alacak")
    private BigDecimal receive = BigDecimal.ZERO;

    @FieldMetadataAnn(title = "Bakiye")
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @FieldMetadataAnn(title = "Maliyet Yeri", priority = 60)
    @AttributeValueValidate(attributeId = "Cost_Place")
    private AttributeValue cost;

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

        if (StringUtils.isNotBlank(cost.getLabel())) {
            search += " " + cost.getLabel();
        }

        if (StringUtils.isNotBlank(description)) {
            search += " " + description;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public Customer getParent() {
        return parent;
    }

    public void setParent(Customer parent) {
        this.parent = parent;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getLoan() {
        return loan;
    }

    public void setLoan(BigDecimal loan) {
        this.loan = loan;
    }

    public BigDecimal getReceive() {
        return receive;
    }

    public void setReceive(BigDecimal receive) {
        this.receive = receive;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AttributeValue getCost() {
        return cost;
    }

    public void setCost(AttributeValue cost) {
        this.cost = cost;
    }
}
