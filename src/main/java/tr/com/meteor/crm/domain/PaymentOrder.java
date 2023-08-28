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
@EntityMetadataAnn(apiName = "payment_orders", displayField = "instanceName", title = "Ödeme Talimatı", pluralTitle = "Ödeme Talimatları",
    ownerPath = "owner.id", assignerPath = "assigner.id", secondAssignerPath = "secondAssigner.id", otherPath = "muhasebeci.id")
@Table(indexes = {@Index(columnList = "search")})
public class PaymentOrder extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Ödeme Talimatı", search = true, readOnly = true, priority = 10, active = true)
    private String name;
    @ManyToOne
    @JoinColumn(name = "store_id")
    @FieldMetadataAnn(title = "Satın Alma Kodu", priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "muhasebeci_id")
    @FieldMetadataAnn(title = "Muhasebeci", priority = 5, active = false)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User muhasebeci;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @FieldMetadataAnn(title = "Talimat Oluşturan", priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigner_id")
    @FieldMetadataAnn(title = "1.Onaycı", display = true, priority = 2, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_assigner_id")
    @FieldMetadataAnn(title = "2.Onaycı", display = true, priority = 2, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User secondAssigner;

    @FieldMetadataAnn(title = "Fatura Tarihi", display = true, priority = 60, filterable = true)
    private Instant invoiceDate;

    @FieldMetadataAnn(title = "Vade Tarihi", display = true, priority = 61, filterable = true)
    private Instant maturityDate;

    @FieldMetadataAnn(title = "Fatura Numarası", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String invoiceNum;

    @ManyToOne
    @FieldMetadataAnn(title = "Ödeme Yapılacak Firma", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "sirket_id")
    @FieldMetadataAnn(title = "Fatura Kesilen Şirket", display = true, priority = 62, filterable = true)
    @AttributeValueValidate(attributeId = "Fatura_Sirketleri")
    private AttributeValue sirket;

    @ManyToOne
    @JoinColumn(name = "odeme_yapan_sirket_id")
    @FieldMetadataAnn(title = "Ödeme Yapan Firma", display = true, readOnly = false, priority = 21, filterable = true)
    @AttributeValueValidate(attributeId = "Fatura_Sirketleri")
    private AttributeValue odemeYapanSirket;

    @ManyToOne
    @JoinColumn(name = "cost_id")
    @FieldMetadataAnn(title = "Maliyet Yeri", defaultValue = "Cost_Place_MeteorMerkez", priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Cost_Place")
    private AttributeValue cost;

    @ManyToOne
    @JoinColumn(name = "payment_subject_id")
    @FieldMetadataAnn(title = "Ödeme Konusu", defaultValue = "Payment_Sub_Fatura", priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Payment_Sub")
    private AttributeValue paymentSubject;

    @FieldMetadataAnn(title = "Toplam Tutar", priority = 100, display = true, required = true)
    private BigDecimal amount;

    @FieldMetadataAnn(title = "Ödenen Tutar", priority = 101)
    private BigDecimal payamount;

    @FieldMetadataAnn(title = "Kalan Tutar", priority = 102)
    private BigDecimal nextamount;

    @FieldMetadataAnn(title = "Ödenen TL Tutarı", priority = 800)
    private BigDecimal payTl;

    @ManyToOne
    @JoinColumn(name = "money_type_id")
    @FieldMetadataAnn(title = "Faturadaki Para Birimi", defaultValue = "Par_Bir_Tl",priority = 113, display = true)
    @AttributeValueValidate(attributeId = "Par_Bir")
    private AttributeValue moneyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iban_id")
    @FieldMetadataAnn(title = "Iban", required = true, priority = 110)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Iban iban;
    @FieldMetadataAnn(title = "Açıklama", priority = 120)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Fatura Kaynağı", priority = 120)
    @Column(length = 255)
    private String kaynak;

    @FieldMetadataAnn(title = "IBAN", priority = 120)
    @Column(length = 255)
    private String strIban;
    @ManyToOne
    @JoinColumn(name = "status_id")
    @FieldMetadataAnn(title = "Onay Durumu", defaultValue = "Payment_Status_Bek1", readOnly = true, priority = 140, filterable = true)
    @AttributeValueValidate(attributeId = "Payment_Status")
    private AttributeValue status;

    @FieldMetadataAnn(title = "1.Onay Tarihi", priority = 60, filterable = true)
    private Instant okeyFirst;

    @FieldMetadataAnn(title = "2.Onay Tarihi", priority = 61, filterable = true)
    private Instant okeySecond;

    @FieldMetadataAnn(title = "Muhasebe Tarihi", priority = 62, filterable = true)
    private Instant okeyMuh;

    @FieldMetadataAnn(title = "Reddetme Tarihi", priority = 63, filterable = true)
    private Instant cancelDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancel_user_id")
    @FieldMetadataAnn(title = "Reddeden", priority = 1000)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User cancelUser;

    @FieldMetadataAnn(title = "Dekont Talebi", priority = 400)
    private Boolean dekont = false;

    @FieldMetadataAnn(title = "Kısmi Ödeme?", priority = 400)
    private Boolean kismi = false;

    @ManyToOne
    @JoinColumn(name = "payment_type_id")
    @FieldMetadataAnn(title = "Ödeme Şekli", defaultValue = "PaymentType_Nakit", priority = 300)
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

    @FieldMetadataAnn(title = "Ödeme Yapıldı Mı?", priority = 310)
    private Boolean success = false;

    @FieldMetadataAnn(title = "Otomatik Ödemede Mi?", priority = 310)
    private Boolean autopay = false;

    @FieldMetadataAnn(title = "PDF", priority = 120, active = false)
    private String base64File;

    @Transient
    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    @PostLoad
    private void populateInstanceName() {
        this.instanceName = this.name;
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

    public User getSecondAssigner() {
        return secondAssigner;
    }

    public void setSecondAssigner(User secondAssigner) {
        this.secondAssigner = secondAssigner;
    }

    public AttributeValue getPaymentSubject() {
        return paymentSubject;
    }

    public void setPaymentSubject(AttributeValue paymentSubject) {
        this.paymentSubject = paymentSubject;
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

    public Instant getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Instant maturityDate) {
        this.maturityDate = maturityDate;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Boolean getDekont() {
        return dekont;
    }

    public void setDekont(Boolean dekont) {
        this.dekont = dekont;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Iban getIban() {
        return iban;
    }

    public void setIban(Iban iban) {
        this.iban = iban;
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

    public AttributeValue getCost() {
        return cost;
    }

    public void setCost(AttributeValue cost) {
        this.cost = cost;
    }

    public String getBase64File() {
        return base64File;
    }

    public void setBase64File(String base64File) {
        this.base64File = base64File;
    }

    public User getMuhasebeci() {
        return muhasebeci;
    }

    public void setMuhasebeci(User muhasebeci) {
        this.muhasebeci = muhasebeci;
    }

    public Boolean getAutopay() {
        return autopay;
    }

    public void setAutopay(Boolean autopay) {
        this.autopay = autopay;
    }

    public AttributeValue getOdemeYapanSirket() {
        return odemeYapanSirket;
    }

    public void setOdemeYapanSirket(AttributeValue odemeYapanSirket) {
        this.odemeYapanSirket = odemeYapanSirket;
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

    public BigDecimal getPayamount() {
        return payamount;
    }

    public void setPayamount(BigDecimal payamount) {
        this.payamount = payamount;
    }

    public BigDecimal getNextamount() {
        return nextamount;
    }

    public void setNextamount(BigDecimal nextamount) {
        this.nextamount = nextamount;
    }

    public Instant getOkeyMuh() {
        return okeyMuh;
    }

    public void setOkeyMuh(Instant okeyMuh) {
        this.okeyMuh = okeyMuh;
    }

    public Instant getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Instant cancelDate) {
        this.cancelDate = cancelDate;
    }

    public User getCancelUser() {
        return cancelUser;
    }

    public void setCancelUser(User cancelUser) {
        this.cancelUser = cancelUser;
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

    public String getKaynak() {
        return kaynak;
    }

    public void setKaynak(String kaynak) {
        this.kaynak = kaynak;
    }

    public String getStrIban() {
        return strIban;
    }

    public void setStrIban(String strIban) {
        this.strIban = strIban;
    }
}
