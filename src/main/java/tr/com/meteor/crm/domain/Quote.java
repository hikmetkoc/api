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
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "quotes", displayField = "instanceName", title = "Araç", pluralTitle = "Raporlar",
    ownerPath = "owner.id", assignerPath = "assigner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Quote extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Rapor Başlığı", search = true, readOnly = false, priority = 12, active = true)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Hazırlayan", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @FieldMetadataAnn(title = "Üst Yönetici", display = true, priority = 0, readOnly = false, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;
    @FieldMetadataAnn(title = "Açıklama", priority = 140)
    @Column(length = 2048)
    private String description;

/*    @ManyToOne
    @FieldMetadataAnn(required = true, title = "Yönetici", display = true, priority = 20)
    private Customer customer;
    @ManyToOne
    @FieldMetadataAnn(required = true, title = "Aşama", display = true, priority = 120, defaultValue = "Tek_Aşa_Yeni")
    @AttributeValueValidate(attributeId = "Tek_Aşa")
    private AttributeValue stage;

    @ManyToOne
    @FieldMetadataAnn(title = "Aktivite", priority = 30)
    private Activity activity;
*/
    /*@FieldMetadataAnn(title = "Aylık Tüketim Litre", display = true, priority = 40)
    private BigDecimal fuelLt;

    @FieldMetadataAnn(title = "Aylık Tüketim TL", display = true, priority = 50)
    private BigDecimal fuelTl;

    @FieldMetadataAnn(title = "Motorin İndirim", display = true, priority = 70)
    private Double discountDiesel;

    @FieldMetadataAnn(title = "Benzin İndirim", display = true, priority = 60)
    private Double discountGasoline;

    @FieldMetadataAnn(title = "Ödeme Günü", priority = 80)
    private Integer paymentDay;

    @ManyToOne
    @FieldMetadataAnn(title = "Ödeme Periyodu", priority = 90)
    @AttributeValueValidate(attributeId = "Söz_ÖdP")
    private AttributeValue paymentPeriod;

    @ManyToOne
    @FieldMetadataAnn(title = "Ödeme Yöntemi", priority = 100)
    @AttributeValueValidate(attributeId = "Söz_ÖdY")
    private AttributeValue paymentMethod;

    @ManyToOne
    @FieldMetadataAnn(title = "Olasılık", priority = 110, defaultValue = "Tek_Ola_Orta")
    @AttributeValueValidate(attributeId = "Tek_Ola")
    private AttributeValue probability;

    @ManyToOne
    @AttributeValueValidate(attributeId = "Müş_Dur")
    @FieldMetadataAnn(title = "Müşteri Durum", display = true, readOnly = true, priority = 130)
    private AttributeValue customerStatus;
*/
   /* @ManyToOne
    @AttributeValueValidate(attributeId = "Tek_Ona")
    @FieldMetadataAnn(title = "Onay Durumu", display = true, priority = 150, readOnly = true)
    private AttributeValue approvalStatus;

    @ManyToOne
    @FieldMetadataAnn(title = "Onay Kişisi", priority = 160, readOnly = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User approvalUser;
*/
    @Formula("name")
    @FieldMetadataAnn(title = "Başlık")
    private String instanceName;

    /*@ManyToOne
    @AttributeValueValidate(attributeId = "Söz_Tip")
    @FieldMetadataAnn(title = "Sözleşme Tip", readOnly = true, priority = 170)
    private AttributeValue contractType;
*/
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
    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


  /* public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AttributeValue getStage() {
        return stage;
    }

    public void setStage(AttributeValue stage) {
        this.stage = stage;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }*/
/*
    public BigDecimal getFuelLt() {
        return fuelLt;
    }

    public void setFuelLt(BigDecimal fuelLt) {
        this.fuelLt = fuelLt;
    }

    public BigDecimal getFuelTl() {
        return fuelTl;
    }

    public void setFuelTl(BigDecimal fuelTl) {
        this.fuelTl = fuelTl;
    }

    public Double getDiscountDiesel() {
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

    public Integer getPaymentDay() {
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

    public AttributeValue getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(AttributeValue paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public AttributeValue getProbability() {
        return probability;
    }

    public void setProbability(AttributeValue probability) {
        this.probability = probability;
    }

    public AttributeValue getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(AttributeValue customerStatus) {
        this.customerStatus = customerStatus;
    }
*/
    /*public AttributeValue getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(AttributeValue approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public User getApprovalUser() {
        return approvalUser;
    }

    public void setApprovalUser(User approvalUser) {
        this.approvalUser = approvalUser;
    }*/
/*
    public AttributeValue getBuyType() {
        return contractType;
    }

    public void setContractType(AttributeValue contractType) {
        this.contractType = contractType;
    }
*/
    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

     /*   if (customer != null) {
            search += " " + customer.getName();
        }
*/
        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
