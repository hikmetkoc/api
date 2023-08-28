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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "buys", displayField = "instanceName", title = "Teklif", pluralTitle = "Teklifler",
    ownerPath = "owner.id", assignerPath = "assigner.id", otherPath = "secondAssigner.id")
@Table(indexes = {@Index(columnList = "search")})
public class Buy extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Teklif", search = true, readOnly = true, priority = 10, active = false)
    private String name;
    @ManyToOne
    @FieldMetadataAnn(title = "Satın Alma Sorumlusu", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;
    @ManyToOne
    @FieldMetadataAnn(title = "Öneren", active = false)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;

    @ManyToOne
    @FieldMetadataAnn(title = "Onaycı", display = false, priority = 3, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User secondAssigner;

    @FieldMetadataAnn(title = "Öneri Tarihi", active = false)
    private Instant okeyFirst;

    @FieldMetadataAnn(title = "Onay Tarihi", display = true, priority = 61)
    private Instant okeySecond;

    @ManyToOne
    @FieldMetadataAnn(title = "Onay Durumu", defaultValue = "Sat_Dur_Bekle", display = true, readOnly = false, priority = 140)
    @AttributeValueValidate(attributeId = "Sat_Dur")
    private AttributeValue quoteStatus;

    @ManyToOne
    @FieldMetadataAnn(title = "Teklif Durumu", defaultValue = "Tek_Dur_Haz", display = true, readOnly = true, priority = 138)
    @AttributeValueValidate(attributeId = "Tek_Dur")
    private AttributeValue preparation;

    @FieldMetadataAnn(title = "Teklif Gerekçesi", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String description;

    @ManyToOne
    @FieldMetadataAnn(title = "Tedarikçi", display = true, priority = 30, filterable = true)
    private Customer customer;

    @ManyToOne
    @FieldMetadataAnn(title = "Satın Alma Talebi", display = false, priority = 0 ,active = true)
    private Store store;

    @FieldMetadataAnn(title = "Satın Alma Kodu", display = true, readOnly = true, filterable = true)
    private String stcode;
    @ManyToOne
    @FieldMetadataAnn(title = "Para Birimi", defaultValue = "Par_Bir_Tl",priority = 113, display = true)
    @AttributeValueValidate(attributeId = "Par_Bir")
    private AttributeValue moneyType;

    @FieldMetadataAnn(title = "Tutar", priority = 100, display = true, required = true)
    private BigDecimal fuelTl;

    @FieldMetadataAnn(title = "Onaylanan Tutar", priority = 100, active = false, readOnly = false, required = false)
    private BigDecimal onayTl;

    @ManyToOne
    @FieldMetadataAnn(title = "Ödeme Yöntemi", priority = 113, display = true)
    @AttributeValueValidate(attributeId = "Söz_ÖdY")
    private AttributeValue paymentMethod;
    @FieldMetadataAnn(title = "Teklif Başlangıç Tarihi", display = true, priority = 60)
    private Instant startDate;

    @FieldMetadataAnn(title = "Teklif Bitiş Tarihi", display = true, priority = 70)
    private Instant endDate;

    @FieldMetadataAnn(title = "Vade Tarihi", display = true, priority = 70)
    private Instant maturityDate;

    @FieldMetadataAnn(title = "İşlem", active = false)
    private Boolean islem;

    @FieldMetadataAnn(title = "Öneri", display = true, readOnly = true, priority = 139)
    private Boolean suggest;

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getFuelTl() {
        return fuelTl;
    }

    public void setFuelTl(BigDecimal fuelTl) {
        this.fuelTl = fuelTl;
    }

    public AttributeValue getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(AttributeValue paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
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

    /*public AttributeValue getSirket() {
        return sirket;
    }

    public void setSirket(AttributeValue sirket) {
        this.sirket = sirket;
    }

    public AttributeValue getBirim() {
        return birim;
    }

    public void setBirim(AttributeValue birim) {
        this.birim = birim;
    }*/

    /*public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }*/
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

    public Boolean getIslem() {
        return islem;
    }

    public void setIslem(Boolean islem) {
        this.islem = islem;
    }

    /*public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }*/

    public BigDecimal getOnayTl() {
        return onayTl;
    }

    public void setOnayTl(BigDecimal onayTl) {
        this.onayTl = onayTl;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public User getSecondAssigner() {
        return secondAssigner;
    }

    public void setSecondAssigner(User secondAssigner) {
        this.secondAssigner = secondAssigner;
    }

    public Instant getOkeyFirst() {
        return okeyFirst;
    }

    public void setOkeyFirst(Instant okeyFirst) {
        this.okeyFirst = okeyFirst;
    }

    public Instant getOkeySecond() {
        return okeySecond;
    }

    public void setOkeySecond(Instant okeySecond) {
        this.okeySecond = okeySecond;
    }

    public AttributeValue getQuoteStatus() {
        return quoteStatus;
    }

    public void setQuoteStatus(AttributeValue quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    public AttributeValue getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(AttributeValue moneyType) {
        this.moneyType = moneyType;
    }

    public String getStcode() {
        return stcode;
    }

    public void setStcode(String stcode) {
        this.stcode = stcode;
    }

    public Instant getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Instant maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Boolean getSuggest() {
        return suggest;
    }

    public void setSuggest(Boolean suggest) {
        this.suggest = suggest;
    }

    public AttributeValue getPreparation() {
        return preparation;
    }

    public void setPreparation(AttributeValue preparation) {
        this.preparation = preparation;
    }
}
