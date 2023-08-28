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
@EntityMetadataAnn(
    apiName = "leads", displayField = "instanceName", title = "Müşteri Adayı", pluralTitle = "Müşteri Adayları",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Lead extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "Ad", search = true, display = true, priority = 10)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Müşteri", display = true, priority = 40)
    private Customer customer;

    @ManyToOne
    @FieldMetadataAnn(title = "Müşteri Adayı Sahibi", display = true, readOnly = true, filterable = true, priority = 30)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @FieldMetadataAnn(title = "Telefon", type = "Phone", search = true, display = true, priority = 20)
    private String phone;

    @FieldMetadataAnn(title = "Açıklama")
    @Column(length = 2048)
    private String description;

    @ManyToOne
    @AttributeValueValidate(attributeId = "MüA_Dur")
    @FieldMetadataAnn(title = "Durum", display = true, filterable = true, priority = 50)
    private AttributeValue status;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
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

        if (StringUtils.isNotBlank(phone)) {
            search += " " + phone;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
