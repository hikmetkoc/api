package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "customtasks", displayField = "instanceName", title = "Görev", pluralTitle = "Görevler",
    ownerPath = "owner.id", masterPath = "customer.owner.id")
@Table(indexes = {@Index(columnList = "search")})
public class CustomTask extends IdNameAuditingEntity<UUID> {

    @ManyToOne
    @FieldMetadataAnn(title = "Görev Sahibi", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    /*@ManyToOne
    @FieldMetadataAnn(title = "Talep Eden", display = true, priority = 0, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;*/

    @ManyToOne
    @FieldMetadataAnn(title = "Tedarikçi", display = true, priority = 30)
    private Customer customer;
    @FieldMetadataAnn(title = "Yapılacak Zaman", required = true, display = true, priority = 40, filterable = true)
    private Instant dueTime;
    @FieldMetadataAnn(title = "Açıklama", priority = 100)
    @Column(length = 2048)
    private String description;
    @ManyToOne
    @FieldMetadataAnn(title = "Konu", defaultValue = "Gorev_Konular_Ran", display = true, priority = 10, required = true, search = true, filterable = true)
    @AttributeValueValidate(attributeId = "Gorev_Konular")
    private AttributeValue type;

    @FieldMetadataAnn(title = "Konu Başlığı", priority = 100, search = true)
    @Column(length = 2048)
    private String subjectdesc;

    @ManyToOne
    @FieldMetadataAnn(title = "Durum", defaultValue = "T_Gör_Dur_Yeni", display = true, priority = 60, filterable = true)
    @AttributeValueValidate(attributeId = "T_Gör_Dur")
    private AttributeValue status;

    @ManyToOne
    @FieldMetadataAnn(title = "Önem Derecesi", defaultValue = "Gör_Imp_Orta", display = true, priority = 65, filterable = true)
    @AttributeValueValidate(attributeId = "Gör_Imp")
    private AttributeValue importance;


/*@FieldMetadataAnn(title = "Başarısızlık Nedeni", priority = 0)
    private String failReason;*/

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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
   /* public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }
*/
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Instant getDueTime() {
        return dueTime;
    }

    public void setDueTime(Instant dueTime) {
        this.dueTime = dueTime;
    }

    /*public Instant getOktime() {
        return oktime;
    }

    public void setOktime(Instant oktime) {
        this.oktime = oktime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
*/
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubjectdesc() {
        return subjectdesc;
    }

    public void setSubjectdesc(String subjectdesc) {
        this.subjectdesc = subjectdesc;
    }
    public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
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

    /*public AttributeValue getBirim() {
        return birim;
    }

    public void setBirim(AttributeValue birim) {
        this.birim = birim;
    }*/
    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(subjectdesc)) {
            search += " " + subjectdesc;
        }

        if (StringUtils.isNotBlank(description)) {
            search += " " + description;
        }

        if (type != null) {
            search += type.getLabel();
        }
        /*if (customer != null) {
            search += customer.getName();
        }*/

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
