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
@EntityMetadataAnn(apiName = "materials", displayField = "instanceName", title = "Demirbaş", pluralTitle = "Şirket Demirbaşları",
    ownerPath = "user.id")
@Table(indexes = {@Index(columnList = "search")})
public class Material extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Demirbaş", search = true, readOnly = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @FieldMetadataAnn(title = "Kullanıcı", filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "type_id")
    @FieldMetadataAnn(title = "Cins", display = true, filterable = true)
    @AttributeValueValidate(attributeId = "Material_Type")
    private AttributeValue type;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @FieldMetadataAnn(title = "Marka", display = true, filterable = true)
    @AttributeValueValidate(attributeId = "Material_Brand")
    private AttributeValue brand;

    @FieldMetadataAnn(title = "Model")
    @Column(length = 2048)
    private String model;

    @FieldMetadataAnn(title = "Seri Numara")
    @Column(length = 2048)
    private String serialNumber;

    @FieldMetadataAnn(title = "Ayırt Edici Özellik")
    @Column(length = 2048)
    private String ayirt;
    @FieldMetadataAnn(title = "Açıklama")
    @Column(length = 2048)
    private String description;

    @ManyToOne
    @JoinColumn(name = "sirket_id")
    @FieldMetadataAnn(title = "Şirket", priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Sirketler")
    private AttributeValue sirket;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @FieldMetadataAnn(title = "Durum", defaultValue = "Material_Status_Aktif", display = true, filterable = true)
    @AttributeValueValidate(attributeId = "Material_Status")
    private AttributeValue status;

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

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
    }

    public AttributeValue getSirket() {
        return sirket;
    }

    public void setSirket(AttributeValue sirket) {
        this.sirket = sirket;
    }



    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(description)) {
            search += " " + description;
        }

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAyirt() {
        return ayirt;
    }

    public void setAyirt(String ayirt) {
        this.ayirt = ayirt;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public AttributeValue getBrand() {
        return brand;
    }

    public void setBrand(AttributeValue brand) {
        this.brand = brand;
    }

    public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
