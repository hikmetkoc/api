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
@EntityMetadataAnn(
    apiName = "contracts", displayField = "instanceName", title = "Satın Alma", pluralTitle = "Satın Alma Talepleri",
    masterPath = "customer.owner.id", ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Contract extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Satın Alma Talebi", search = true, readOnly = true, priority = 10, active = false)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Onay Kişisi", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @FieldMetadataAnn(title = "Talep Eden", display = true, priority = 0, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;

    @ManyToOne
    @FieldMetadataAnn(title = "Şirket", defaultValue = "Sirketler_Meteor", display = true, priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Sirketler")
    private AttributeValue sirket;

    @ManyToOne
    @FieldMetadataAnn(title = "Birim", defaultValue = "Birimler_T_IT", display = true, priority = 25, filterable = true)
    @AttributeValueValidate(attributeId = "Birimler_T")
    private AttributeValue birim;
    @FieldMetadataAnn(title = "Satın Alma Gerekçesi", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String description;

    @ManyToOne
    @FieldMetadataAnn(title = "Tedarikçi", display = true, priority = 30, filterable = true)
    private Customer customer;

    /*@ManyToOne
    @FieldMetadataAnn(title = "Teklif", priority = 80)
    private Quote quote;*/

    /*@FieldMetadataAnn(title = "Aylık Tüketim(LT)", priority = 90)
    private BigDecimal fuelLt;*/

    @FieldMetadataAnn(title = "Tutar(TL)", priority = 100, display = true)
    private BigDecimal fuelTl;

    /*@FieldMetadataAnn(title = "Motorin İndirim", display = true, priority = 40)
    private Double discountDiesel;

    @FieldMetadataAnn(title = "Benzin İndirim", display = true, priority = 30)
    private Double discountGasoline;*/

    /*@FieldMetadataAnn(title = "Ödeme Günü", priority = 110)
    private Integer paymentDay;

    @ManyToOne
    @FieldMetadataAnn(title = "Ödeme Periyodu", priority = 110)
    @AttributeValueValidate(attributeId = "Söz_ÖdP")
    private AttributeValue paymentPeriod;
*/
    @ManyToOne
    @FieldMetadataAnn(title = "Ödeme Yöntemi", priority = 113, display = true)
    @AttributeValueValidate(attributeId = "Söz_ÖdY")
    private AttributeValue paymentMethod;

    @ManyToOne
    @FieldMetadataAnn(title = "Onay Durumu", defaultValue = "Söz_Dur_Pasif", display = true, priority = 140)
    @AttributeValueValidate(attributeId = "Söz_Dur")
    private AttributeValue status;

    /*@FieldMetadataAnn(readOnly = true, title = "Onaylanan Ürün", display = false)
    private String correctStatus;*/

    @FieldMetadataAnn(title = "Ürünler", display = true)
    private String products;
    @FieldMetadataAnn(title = "Teklif Başlangıç Tarihi", display = true, priority = 60)
    private Instant startDate;

    @FieldMetadataAnn(title = "Teklif Bitiş Tarihi", display = true, priority = 70)
    private Instant endDate;

    /*@ManyToOne
    @AttributeValueValidate(attributeId = "Müş_Dur")
    @FieldMetadataAnn(title = "Tedarikçi Durumu", display = true, priority = 150, readOnly = true)
    private AttributeValue customerStatus;*/

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    /*@ManyToOne
    @AttributeValueValidate(attributeId = "Söz_Tip")
    @FieldMetadataAnn(title = "Tip", readOnly = true, priority = 160)
    private AttributeValue type;*/

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /*public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public BigDecimal getFuelLt() {
        return fuelLt;
    }

    public void setFuelLt(BigDecimal fuelLt) {
        this.fuelLt = fuelLt;
    }
*/
    public BigDecimal getFuelTl() {
        return fuelTl;
    }

    public void setFuelTl(BigDecimal fuelTl) {
        this.fuelTl = fuelTl;
    }

    /*public Double getDiscountDiesel() {
        return discountDiesel;
    }

    public void setDiscountDiesel(Double discountDiesel) {
        this.discountDiesel = discountDiesel;
    }

    public Double getDiscountGasoline() {
        return discountGasoline;
    }

    public void setDiscountGasoline(Double discountGasoline) {
        this.discountGasoline = discountGasoline;
    }
*/
    /*public Integer getPaymentDay() {
        return paymentDay;
    }

    public void setPaymentDay(Integer paymentDay) {
        this.paymentDay = paymentDay;
    }

    public AttributeValue getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(AttributeValue paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }
*/
    public AttributeValue getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(AttributeValue paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
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

    /*public AttributeValue getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(AttributeValue customerStatus) {
        this.customerStatus = customerStatus;
    }

    public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
    }*/

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
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

    public AttributeValue getSirket() {
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
    }

    /*public String getCorrectStatus() {
        return correctStatus;
    }

    public void setCorrectStatus(String correctStatus) {
        this.correctStatus = correctStatus;
    }
*/
    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
