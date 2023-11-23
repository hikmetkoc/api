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
    apiName = "behaviors", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Hareket", pluralTitle = "Hareketler",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Behavior extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "İşlem Yapan", display = true, priority = 15, filterable = true)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @FieldMetadataAnn(title = "Belge No", priority = 40)
    @Column(length = 2048)
    private String document;

    @FieldMetadataAnn(title = "Tutar", priority = 5, display = true, required = true, filterable = true)
    private BigDecimal fuelTl;

    @FieldMetadataAnn(title = "Konu", priority = 999, active = false)
    @Column(length = 2048)
    private String subject;
    @FieldMetadataAnn(title = "Açıklama", priority = 30, filterable = true)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(required = true, title = "Hareket Tipi", display = true, priority = 10, filterable = true)
    @AttributeValueValidate(attributeId = "Har_Tip")
    @ManyToOne
    private AttributeValue type;

    @FieldMetadataAnn(title = "Giriş Tarihi", display = true, priority = 60, filterable = true)
    private Instant inputDate;

    @ManyToOne
    @FieldMetadataAnn(title = "Maliyet Yeri", priority = 0)
    private MotionSums motionsums;

    @Formula("subject")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;


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

    public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
    }
    public MotionSums getMotionsums() {
        return motionsums;
    }

    public void setMorionsums(MotionSums motionsums) {
        this.motionsums = motionsums;
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


    public BigDecimal getFuelTl() {
        return fuelTl;
    }

    public void setFuelTl(BigDecimal fuelTl) {
        this.fuelTl = fuelTl;
    }

    public Instant getInputDate() {
        return inputDate;
    }

    public void setInputDate(Instant inputDate) {
        this.inputDate = inputDate;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
