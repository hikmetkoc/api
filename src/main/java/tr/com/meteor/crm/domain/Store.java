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
@EntityMetadataAnn(apiName = "stores", displayField = "instanceName", title = "Satın Alma", pluralTitle = "Satın Alma Talepleri",
    ownerPath = "owner.id", assignerPath = "assigner.id", otherPath = "buyowner.id")
@Table(indexes = {@Index(columnList = "search")})
public class Store extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Satın Alma Talebi", search = true, readOnly = true, priority = 10, active = false)
    private String name;

    @FieldMetadataAnn(title = "Satın Alma Kodu", display = true, readOnly = true, filterable = true)
    private String stcode;
    @ManyToOne
    @FieldMetadataAnn(title = "Talep Onaycısı", priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @FieldMetadataAnn(title = "Talep Eden", display = true, priority = 2, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;

    @ManyToOne
    @FieldMetadataAnn(title = "Satın Alma Sorumlusu", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User buyowner;

    @ManyToOne
    @FieldMetadataAnn(title = "Ödeme Yapan", display = true, readOnly = true, priority = 20, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    @AttributeValueValidate(attributeId = "Fatura_Sirketleri")
    private AttributeValue sirket;
    @ManyToOne
    @FieldMetadataAnn(title = "Maliyet Yeri", defaultValue = "Cost_Place_MeteorMerkez", display = true, priority = 6, filterable = true)
    @AttributeValueValidate(attributeId = "Cost_Place")
    private AttributeValue maliyet;

    @FieldMetadataAnn(title = "Talep Gerekçesi", display = true, priority = 120)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Talep Edilen Ürün", display = true, priority = 120)
    @Column(length = 2048)
    private String request;
    @ManyToOne
    @FieldMetadataAnn(title = "Talep Onay Durumu", defaultValue = "Söz_Dur_Pasif", display = true, priority = 140, filterable = true)
    @AttributeValueValidate(attributeId = "Söz_Dur")
    private AttributeValue status;

    @ManyToOne
    @FieldMetadataAnn(title = "Satın Alma Onay Durumu", defaultValue = "Sat_Dur_1Bekle", readOnly = true, priority = 150, filterable = true)
    @AttributeValueValidate(attributeId = "Sat_Dur")
    private AttributeValue buyStatus;

    @FieldMetadataAnn(title = "Son Tarih", display = true, priority = 60, filterable = true)
    private Instant endDate;

    @FieldMetadataAnn(title = "İşlem", active = false, priority = 0)
    private Boolean islem;

    @FieldMetadataAnn(title = "Güncelleme Anahtarı", active = false, priority = 0)
    private Boolean buyKey;
    @Formula("stcode")
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

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }

    public User getBuyowner() {
        return buyowner;
    }

    public void setBuyowner(User buyowner) {
        this.buyowner = buyowner;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
    public Boolean getIslem() {
        return islem;
    }

    public void setIslem(Boolean islem) {
        this.islem = islem;
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

    public AttributeValue getBuyStatus() {
        return buyStatus;
    }

    public void setBuyStatus(AttributeValue buyStatus) {
        this.buyStatus = buyStatus;
    }

    public Boolean getBuyKey() {
        return buyKey;
    }

    public void setBuyKey(Boolean buyKey) {
        this.buyKey = buyKey;
    }

    public String getStcode() {
        return stcode;
    }

    public void setStcode(String stcode) {
        this.stcode = stcode;
    }

    public AttributeValue getMaliyet() {
        return maliyet;
    }

    public void setMaliyet(AttributeValue maliyet) {
        this.maliyet = maliyet;
    }

    public AttributeValue getSirket() {
        return sirket;
    }

    public void setSirket(AttributeValue sirket) {
        this.sirket = sirket;
    }
}
