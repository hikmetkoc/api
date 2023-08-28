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
@EntityMetadataAnn(
    apiName = "contacts", displayField = "instanceName", title = "Kişi", pluralTitle = "Kişiler",
    masterPath = "customer.owner.id"
    //masterPath = "customer.owner.id", segmentPath = "customer.segment.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Contact extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "Ad", search = true, display = true, priority = 10)
    private String firstName;

    @FieldMetadataAnn(title = "Soyad", search = true, display = true, priority = 20)
    private String lastName;

    @FieldMetadataAnn(title = "Mail", type = "Email", search = true, display = true, priority = 40)
    private String email;

    @FieldMetadataAnn(title = "Telefon", type = "Phone", search = true, display = true, priority = 50)
    private String phone;

    @Formula("concat(first_name,' ',last_name)")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    /*@ManyToOne
    @AttributeValueValidate(attributeId = "Kiş_Tip")
    @FieldMetadataAnn(title = "Tip", priority = 25)
    private AttributeValue type;*/

    @FieldMetadataAnn(title = "Aktif", priority = 50, defaultValue = "true")
    private Boolean active;

    @ManyToOne
    @FieldMetadataAnn(title = "Tedarikçi", display = true, priority = 30)
    private Customer customer;

    @FieldMetadataAnn(title = "Açıklama", priority = 60)
    @Column(length = 2048)
    private String description;

    /*public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
    }*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(firstName)) {
            search += " " + firstName;
        }

        if (StringUtils.isNotBlank(lastName)) {
            search += " " + lastName;
        }

        if (StringUtils.isNotBlank(phone)) {
            search += " " + phone;
        }

        if (StringUtils.isNotBlank(email)) {
            search += " " + email;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
