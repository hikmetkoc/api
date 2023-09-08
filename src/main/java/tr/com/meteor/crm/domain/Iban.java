package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "ibans", displayField = "instanceName", title = "Iban", pluralTitle = "Ibanlar")
@Table(indexes = {@Index(columnList = "search")})
public class Iban extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, type="iban", defaultValue = "TR", title = "Iban Numarası", priority = 6)
    @Column(length = 30)
    private String name;
    @ManyToOne
    @FieldMetadataAnn(title = "Tedarikçi", readOnly = true, priority = 3)
    private Customer customer;

    @ManyToOne
    @AttributeValueValidate(attributeId = "Müş_Bnk")
    @FieldMetadataAnn(title = "Banka", priority = 10, display = true, readOnly = false)
    private AttributeValue bank;

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

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public String toAddressString() {
        List<String> parts = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            parts.add(name);
        }

        return String.join(" ", parts);
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (customer != null) {
            search += " " + customer.getCommercialTitle();
        }

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public AttributeValue getBank() {
        return bank;
    }

    public void setBank(AttributeValue bank) {
        this.bank = bank;
    }
}
