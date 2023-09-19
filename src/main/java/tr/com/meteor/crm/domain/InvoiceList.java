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
@EntityMetadataAnn(apiName = "invoice_lists", displayField = "instanceName", title = "Fatura Listesi", pluralTitle = "Faturalar",
    ownerPath = "owner.id", otherPath = "permission.id")
@Table(indexes = {@Index(columnList = "search")})
public class InvoiceList extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Fatura", search = true, readOnly = true, priority = 10, active = false)
    private String name;

    /*@ManyToOne
    @JoinColumn(name = "store_id")
    @FieldMetadataAnn(title = "Satın Alma Kodu")
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Store store;*/

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @FieldMetadataAnn(title = "Fatura Sahibi", priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    @FieldMetadataAnn(title = "Görüntüleme Yetkilisi", priority = 5, active = false)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User permission;

    /*@ManyToOne
    @FieldMetadataAnn(title = "Atayan Muhasebeci", display = true, priority = 2, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;*/

    /*@ManyToOne
    @FieldMetadataAnn(title = "2.Onaycı", display = true, priority = 2, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User secondAssigner;*/

    @FieldMetadataAnn(title = "Fatura Tarihi", display = true, priority = 60, filterable = true)
    private Instant invoiceDate;

    @FieldMetadataAnn(title = "Gönderim Tarihi", display = true, priority = 55, filterable = true)
    private Instant sendDate;

    @FieldMetadataAnn(title = "Vade Tarihi", display = true, priority = 65)
    private Instant maturityDate;

    @FieldMetadataAnn(title = "Atama Tarihi", priority = 65)
    private Instant successDate;

    @FieldMetadataAnn(title = "Fatura Numarası", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String invoiceNum;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @FieldMetadataAnn(title = "Ödeme Yapılacak Firma", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "sirket_id")
    @FieldMetadataAnn(title = "Fatura Sahibi Firma", defaultValue = "Fatura_Sirketleri_Meteor", display = true, readOnly = true, priority = 20, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    @AttributeValueValidate(attributeId = "Fatura_Sirketleri")
    private AttributeValue sirket;

    @ManyToOne
    @JoinColumn(name = "odeme_yapan_sirket_id")
    @FieldMetadataAnn(title = "Ödeme Yapan Firma", display = true, readOnly = false, priority = 21, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    @AttributeValueValidate(attributeId = "Fatura_Sirketleri")
    private AttributeValue odemeYapanSirket;

    @ManyToOne
    @JoinColumn(name = "cost_id")
    @FieldMetadataAnn(title = "Ödeme Konusu", defaultValue = "Cost_Place_MeteorMerkez", priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Cost_Place")
    private AttributeValue cost;

    @FieldMetadataAnn(title = "Tutar", priority = 100, display = true, required = true)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "money_type_id")
    @FieldMetadataAnn(title = "Faturadaki Para Birimi", defaultValue = "Par_Bir_Tl",priority = 113, display = true, readOnly = true)
    @AttributeValueValidate(attributeId = "Par_Bir")
    private AttributeValue moneyType;

    @ManyToOne
    @JoinColumn(name = "iban_id")
    @FieldMetadataAnn(title = "Iban", display = false, priority = 110)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Iban iban;

    @FieldMetadataAnn(title = "Açıklama", display = true, priority = 121)
    @Column(length = 2048)
    private String description;

    @ManyToOne
    @JoinColumn(name = "payment_subject_id")
    @FieldMetadataAnn(title = "Ödeme Konusu", defaultValue = "Payment_Sub_Fatura", priority = 71)
    @AttributeValueValidate(attributeId = "Payment_Sub")
    private AttributeValue paymentSubject;

    @ManyToOne
    @JoinColumn(name = "invoice_status_id")
    @FieldMetadataAnn(title = "Fatura Durumu", defaultValue = "Fatura_Durumlari_Sahipsiz", display = true, readOnly = true, priority = 140, filterable = true)
    @AttributeValueValidate(attributeId = "Fatura_Durumlari")
    private AttributeValue invoiceStatus;

    @FieldMetadataAnn(title = "Dekont Talebi", priority = 100)
    private Boolean dekont = false;

    @FieldMetadataAnn(title = "Kısmi Ödeme", priority = 400)
    private Boolean kismi = false;

    @ManyToOne
    @JoinColumn(name = "payment_type_id")
    @FieldMetadataAnn(title = "Ödeme Şekli", defaultValue = "PaymentType_Havale", priority = 300)
    @AttributeValueValidate(attributeId = "PaymentType")
    private AttributeValue paymentType;

    @ManyToOne
    @JoinColumn(name = "payment_style_id")
    @FieldMetadataAnn(title = "Ödeme Yapılacak Para Birimi", priority = 1500)
    @AttributeValueValidate(attributeId = "Payment_Style")
    private AttributeValue paymentStyle;

    @ManyToOne
    @JoinColumn(name = "exchange_id")
    @FieldMetadataAnn(title = "Kur Tarihi", priority = 1600)
    @AttributeValueValidate(attributeId = "Exchange_Date")
    private AttributeValue exchange;

    @FieldMetadataAnn(title = "Ödenen TL Tutarı", priority = 103)
    private BigDecimal payTl;

    @FieldMetadataAnn(title = "Ödeme Yapıldı", priority = 310)
    private Boolean success = false;

    @FieldMetadataAnn(title = "Otomatik Ödemede", priority = 310)
    private Boolean autopay = false;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    public String ToInstant() {
        return Instant.now().toString();
    }

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

    /*public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
    }*/

    public AttributeValue getSirket() {
        return sirket;
    }

    public void setSirket(AttributeValue sirket) {
        this.sirket = sirket;
    }


    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public Instant getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Instant invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AttributeValue getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(AttributeValue moneyType) {
        this.moneyType = moneyType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Iban getIban() {
        return iban;
    }

    public void setIban(Iban iban) {
        this.iban = iban;
    }

    public Instant getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Instant maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Boolean getDekont() {
        return dekont;
    }

    public void setDekont(Boolean dekont) {
        this.dekont = dekont;
    }

    public AttributeValue getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(AttributeValue invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Instant getSendDate() {
        return sendDate;
    }

    public void setSendDate(Instant sendDate) {
        this.sendDate = sendDate;
    }

    public AttributeValue getExchange() {
        return exchange;
    }

    public void setExchange(AttributeValue exchange) {
        this.exchange = exchange;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public AttributeValue getPaymentSubject() {
        return paymentSubject;
    }

    public void setPaymentSubject(AttributeValue paymentSubject) {
        this.paymentSubject = paymentSubject;
    }

    public AttributeValue getCost() {
        return cost;
    }

    public void setCost(AttributeValue cost) {
        this.cost = cost;
    }

    public AttributeValue getOdemeYapanSirket() {
        return odemeYapanSirket;
    }

    public void setOdemeYapanSirket(AttributeValue odemeYapanSirket) {
        this.odemeYapanSirket = odemeYapanSirket;
    }

    public Boolean getAutopay() {
        return autopay;
    }

    public void setAutopay(Boolean autopay) {
        this.autopay = autopay;
    }

    public AttributeValue getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(AttributeValue paymentType) {
        this.paymentType = paymentType;
    }

    public Boolean getKismi() {
        return kismi;
    }

    public void setKismi(Boolean kismi) {
        this.kismi = kismi;
    }

    public Instant getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(Instant successDate) {
        this.successDate = successDate;
    }

    public AttributeValue getPaymentStyle() {
        return paymentStyle;
    }

    public void setPaymentStyle(AttributeValue paymentStyle) {
        this.paymentStyle = paymentStyle;
    }

    public BigDecimal getPayTl() {
        return payTl;
    }

    public void setPayTl(BigDecimal payTl) {
        this.payTl = payTl;
    }

    public User getPermission() {
        return permission;
    }

    public void setPermission(User permission) {
        this.permission = permission;
    }
}
