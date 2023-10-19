package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "proj_task_activities", displayField = "instanceName", title = "Proje Görevi İşlemi", pluralTitle = "Proje Görevi İşlemleri")
@Table(indexes = {@Index(columnList = "search")})
public class ProjTaskActivity extends IdNameAuditingEntity<UUID> {

    @ManyToOne
    @FieldMetadataAnn(title = "Proje Görevi", display = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private ProjTask projtask;
    @FieldMetadataAnn(title = "İşlem")
    private String subject;
    @FieldMetadataAnn(title = "Detaylı Açıklama")
    private String description;

    @FieldMetadataAnn(title = "Tahmini Başlangıç Zamanı", required = true, display = true, filterable = true)
    private Instant startDate;

    @FieldMetadataAnn(title = "Tahmini Bitiş Zamanı", required = true, display = true, filterable = true)
    private Instant endDate;
    @FieldMetadataAnn(title = "Tamamlanma Tarihi", display = true, readOnly = true)
    private Instant okTime;
    @ManyToOne
    @FieldMetadataAnn(title = "Önem Derecesi", defaultValue = "ProjTaskActivity_Imp_Orta", display = true)
    @AttributeValueValidate(attributeId = "ProjTaskActivity_Imp")
    private AttributeValue importance;
    @ManyToOne
    @FieldMetadataAnn(title = "Durum", defaultValue = "ProjTaskActivity_Status_Yeni", display = true, filterable = true)
    @AttributeValueValidate(attributeId = "ProjTaskActivity_Status")
    private AttributeValue status;

    @Formula("subject")
    @FieldMetadataAnn(title = "Başlık", active = false)
    private String instanceName;

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
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


    public AttributeValue getImportance() {
        return importance;
    }

    public void setImportance(AttributeValue importance) {
        this.importance = importance;
    }
    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(subject)) {
            search += " " + subject;
        }

        if (StringUtils.isNotBlank(description)) {
            search += " " + description;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Instant getOkTime() {
        return okTime;
    }

    public void setOkTime(Instant okTime) {
        this.okTime = okTime;
    }

    public ProjTask getProjtask() {
        return projtask;
    }

    public void setProjtask(ProjTask projtask) {
        this.projtask = projtask;
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
}
