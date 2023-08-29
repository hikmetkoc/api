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
    apiName = "spends", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Ödeme", pluralTitle = "Ödemeler",
    ownerPath = "owner.id", otherPath = "finance.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Spend extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "İşlem Yapan", display = true, priority = 15, filterable = true)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @FieldMetadataAnn(title = "Finans Personeli", active = false)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User finance;
    @ManyToOne
    @JoinColumn(name = "paymentorder_id")
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    @FieldMetadataAnn(title = "Fatura", priority = 0)
    private PaymentOrder paymentorder;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    @FieldMetadataAnn(title = "Ödeme Yapılacak Şirket", priority = 0, filterable = true)
    private Customer customer;
    @FieldMetadataAnn(title = "Açıklama")
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Ödeme Durumu", display = true, priority = 60, defaultValue = "Spend_Status_No", filterable = true)
    @AttributeValueValidate(attributeId = "Spend_Status")
    @ManyToOne
    private AttributeValue status;

    @FieldMetadataAnn(title = "Talimat Onay Durumu")
    private String paymentStatus;

    @FieldMetadataAnn(title = "İşlem Tarihi", display = true, priority = 70, readOnly = true, filterable = true)
    private Instant spendDate;

    @FieldMetadataAnn(title = "Vade Tarihi", display = true, priority = 70, readOnly = false, filterable = true)
    private Instant maturityDate;

    @FieldMetadataAnn(title = "Ödeme Sırası")
    private String paymentNum;

    @FieldMetadataAnn(title = "Ödenecek Tutar", priority = 100, display = true, required = true)
    private BigDecimal amount;

    @FieldMetadataAnn(title = "Ödenen Tl Tutarı", priority = 100, display = true)
    private BigDecimal payTl;

    @FieldMetadataAnn(title = "Kur Tutarı", priority = 101, display = true)
    private BigDecimal exchangeMoney;

    @FieldMetadataAnn(title = "Kilit", priority = 9999999)
    private Boolean lock = false;

    @FieldMetadataAnn(title = "Dekont", priority = 120, active = false)
    private String dekont;
    @Formula("description")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;


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

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
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

    public Instant getSpendDate() {
        return spendDate;
    }

    public void setSpendDate(Instant spendDate) {
        this.spendDate = spendDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentOrder getPaymentorder() {
        return paymentorder;
    }

    public void setPaymentorder(PaymentOrder paymentorder) {
        this.paymentorder = paymentorder;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public User getFinance() {
        return finance;
    }

    public void setFinance(User finance) {
        this.finance = finance;
    }

    public Instant getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Instant maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getPaymentNum() {
        return paymentNum;
    }

    public void setPaymentNum(String paymentNum) {
        this.paymentNum = paymentNum;
    }

    public BigDecimal getPayTl() {
        return payTl;
    }

    public void setPayTl(BigDecimal payTl) {
        this.payTl = payTl;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getExchangeMoney() {
        return exchangeMoney;
    }

    public void setExchangeMoney(BigDecimal exchangeMoney) {
        this.exchangeMoney = exchangeMoney;
    }

    public String getDekont() {
        return dekont;
    }

    public void setDekont(String dekont) {
        this.dekont = dekont;
    }
}
