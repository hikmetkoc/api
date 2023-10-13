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
@EntityMetadataAnn(apiName = "company_materials", displayField = "instanceName", title = "Demirbaş", pluralTitle = "Şirket Demirbaşları",
    ownerPath = "owner.id", assignerPath = "assigner.id")
@Table(indexes = {@Index(columnList = "search")})
public class CompanyMaterial extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Ödeme Talimatı", search = true, readOnly = true, priority = 10, active = true)
    private String name;

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

    @FieldMetadataAnn(title = "Fatura Tarihi", display = true, priority = 60, filterable = true)
    private Instant invoiceDate;

    @FieldMetadataAnn(title = "Vade Tarihi", display = true, priority = 61, filterable = true)
    private Instant maturityDate;

    @FieldMetadataAnn(title = "Fatura Numarası", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String invoiceNum;

    /*@ManyToOne
    @JoinColumn(name = "cins_id")
    @FieldMetadataAnn(title = "Fatura Kesilen Şirket", display = true, priority = 62, filterable = true)
    @AttributeValueValidate(attributeId = "Fatura_Sirketleri")
    private AttributeValue cins;

    @ManyToOne
    @JoinColumn(name = "marka_id")
    @FieldMetadataAnn(title = "Ödeme Yapan Firma", display = true, readOnly = false, priority = 21, filterable = true)
    @AttributeValueValidate(attributeId = "Fatura_Sirketleri")
    private AttributeValue marka;

    @ManyToOne
    @JoinColumn(name = "model_id")
    @FieldMetadataAnn(title = "Maliyet Yeri", defaultValue = "Cost_Place_MeteorMerkez", priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Cost_Place")
    private AttributeValue model;*/

    @FieldMetadataAnn(title = "Açıklama", priority = 120)
    @Column(length = 2048)
    private String description;

    /*@FieldMetadataAnn(title = "Açıklama", priority = 120)
    @Column(length = 2048)
    private String seriNu;

    @FieldMetadataAnn(title = "Açıklama", priority = 120)
    @Column(length = 2048)
    private String ayirt;*/
    @ManyToOne
    @JoinColumn(name = "payment_subject_id")
    @FieldMetadataAnn(title = "Ödeme Konusu", defaultValue = "Payment_Sub_Fatura", priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Payment_Sub")
    private AttributeValue sirket;

    /*@ManyToOne
    @JoinColumn(name = "money_type_id")
    @FieldMetadataAnn(title = "Faturadaki Para Birimi", defaultValue = "Par_Bir_Tl",priority = 113, display = true)
    @AttributeValueValidate(attributeId = "Par_Bir")
    private AttributeValue birim;

    @ManyToOne
    @JoinColumn(name = "money_type_id")
    @FieldMetadataAnn(title = "Faturadaki Para Birimi", defaultValue = "Par_Bir_Tl",priority = 113, display = true)
    @AttributeValueValidate(attributeId = "Par_Bir")
    private AttributeValue faturaSirket;*/

    @ManyToOne
    @JoinColumn(name = "status_id")
    @FieldMetadataAnn(title = "Onay Durumu", defaultValue = "Payment_Status_Bek1", readOnly = true, priority = 140, filterable = true)
    @AttributeValueValidate(attributeId = "Payment_Status")
    private AttributeValue status;

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
}
