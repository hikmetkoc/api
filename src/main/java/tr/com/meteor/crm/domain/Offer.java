package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;
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
@EntityMetadataAnn(
    apiName = "offers", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Teklif Havuzu", pluralTitle = "Teklif Havuzları",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Offer extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Hazırlayan", display = true, priority = 10, filterable = true)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @FieldMetadataAnn(title = "Teklif Başlangıç Tarihi", display = true, priority = 60, filterable = true)
    private Instant startDate;

    @FieldMetadataAnn(title = "Teklif Bitiş Tarihi", display = true, priority = 70, filterable = true)
    private Instant endDate;

    @ManyToOne
    @FieldMetadataAnn(title = "Para Birimi", priority = 110, display = true, required = true, filterable = true)
    @AttributeValueValidate(attributeId = "Par_Bir")
    private AttributeValue moneyType;

    @FieldMetadataAnn(title = "Tutar", priority = 100, display = true, required = true)
    private BigDecimal fuelTl;
    @FieldMetadataAnn(title = "Dolar Kuru", priority = 120, required = true)
    private BigDecimal dolRate;
    @FieldMetadataAnn(title = "Euro Kuru", priority = 130, required = true)
    private BigDecimal eurRate;
    @ManyToOne
    @FieldMetadataAnn(title = "Ödeme Yöntemi", priority = 80, required = true)
    @AttributeValueValidate(attributeId = "Söz_ÖdYt")
    private AttributeValue paymentMethod;

    @FieldMetadataAnn(title = "Ödeme Vadesi (Gün)", priority = 90, required = true)
    private Integer paymentDay;

    @FieldMetadataAnn(required = true, title = "Teklif Tipi", display = true, priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "InTek_Tip")
    @ManyToOne
    private AttributeValue type;

    /*@FieldMetadataAnn(required = true, title = "Maliyet Yeri", display = true, priority = 10)
    @AttributeValueValidate(attributeId = "Maliyet_Yerleri")
    @ManyToOne
    private AttributeValue costPlace;*/


    @FieldMetadataAnn(title = "Teklif Konusu", priority = 40, required = true, display = true)
    @Column(length = 255)
    private String subject;
    @FieldMetadataAnn(title = "Açıklama", priority = 160)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Anlaşma Koşulları", priority = 140)
    @Column(length = 2048)
    private String agreeTerms;
    @FieldMetadataAnn(required = true, title = "Teklif Durumu", display = true, priority = 150, filterable = true)
    @AttributeValueValidate(attributeId = "TedTek_Dur")
    @ManyToOne
    private AttributeValue offStat;

    @Formula("subject")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    @FieldMetadataAnn(title = "Tedarikçi", display = true, required = true, filterable = true, priority = 50)
    @ManyToOne
    private Customer customer;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @PreUpdate
    public void preUpdate() {
        generateSubject();
    }

    @PrePersist
    public void postUpdate() {
        generateSubject();
    }

    private void generateSubject() {
        List<String> parts = new ArrayList<>();
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();
        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /*public AttributeValue getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(AttributeValue approvalStatus) {
        this.approvalStatus = approvalStatus;
    }*/

    public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
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

    public AttributeValue getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(AttributeValue paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getPaymentDay() {
        return paymentDay;
    }

    public void setPaymentDay(Integer paymentDay) {
        this.paymentDay = paymentDay;
    }

    public AttributeValue getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(AttributeValue moneyType) {
        this.moneyType = moneyType;
    }

    public BigDecimal getFuelTl() {
        return fuelTl;
    }

    public void setFuelTl(BigDecimal fuelTl) {
        this.fuelTl = fuelTl;
    }

    public BigDecimal getDolRate() {
        return dolRate;
    }

    public void setDolRate(BigDecimal dolRate) {
        this.dolRate = dolRate;
    }

    public BigDecimal getEurRate() {
        return eurRate;
    }

    public void setEurRate(BigDecimal eurRate) {
        this.eurRate = eurRate;
    }

    public String getAgreeTerms() {
        return agreeTerms;
    }

    public void setAgreeTerms(String agreeTerms) {
        this.agreeTerms = agreeTerms;
    }

    public AttributeValue getOffStat() {
        return offStat;
    }

    public void setOffStat(AttributeValue offStat) {
        this.offStat = offStat;
    }

    /*public AttributeValue getCostPlace() {
        return costPlace;
    }

    public void setCostPlace(AttributeValue costPlace) {
        this.costPlace = costPlace;
    }*/
}
