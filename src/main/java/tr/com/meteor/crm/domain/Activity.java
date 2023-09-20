package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "activities", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "İşlem", pluralTitle = "İşlemler",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Activity extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "İşlem Yapan", display = true, priority = 15, filterable = true)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;
    @FieldMetadataAnn(title = "Konu Başlığı", display = true, priority = 15, filterable = true)
    @Column(length = 2048)
    private String subjdesc;

    @FieldMetadataAnn(title = "Konu", priority = 10, filterable = true)
    @Column(length = 2048)
    private String subject;
    @FieldMetadataAnn(title = "Açıklama")
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(required = true, title = "Durum", display = true, priority = 60, defaultValue = "Akt_Dur_Yeni", filterable = true)
    @AttributeValueValidate(attributeId = "Akt_Dur")
    @ManyToOne
    private AttributeValue status;

    @FieldMetadataAnn(title = "Planlanan Tarih", display = true, priority = 9999, readOnly = true, filterable = true)
    private Instant checkOutTime;

    @ManyToOne
    @FieldMetadataAnn(title = "Talep", priority = 0)
    private Task task;

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

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
    }

    public Instant getCheckOutTime() {
        return checkOutTime;
    }
    public void setCheckOutTime(Instant checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getSubjdesc() {
        return subjdesc;
    }

    public void setSubjdesc(String subjdesc) {
        this.subjdesc = subjdesc;
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
}
