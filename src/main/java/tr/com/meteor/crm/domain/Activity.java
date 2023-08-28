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


    /*@FieldMetadataAnn(required = true, title = "Konu", display = true, priority = 10, defaultValue = "Akt_Tip_Firma_Ziyareti")
    @AttributeValueValidate(attributeId = "Akt_Tip")
    @ManyToOne
    private AttributeValue type;
*/

    /*@FieldMetadataAnn(required = true, title = "Neden", active = false, readOnly = true, priority = 50, defaultValue = "Akt_Ned_Satış")
    @AttributeValueValidate(attributeId = "Akt_Ned")
    @ManyToOne
    private AttributeValue reason; */

   /* @FieldMetadataAnn(required = false, title = "Müşteri", display = false, priority = 0)
    @ManyToOne
    private Customer customer;*/

    /*@FieldMetadataAnn(title = "Kampanya", priority = 160)
    @ManyToOne
    private Campaign campaign;*/

    /*@FieldMetadataAnn(title = "Planlanan Başlangıç Zamanı", priority = 70, readOnly = true, active = false)
    private Instant plannedStartTime;

    @FieldMetadataAnn(title = "Planlanan Bitiş Zamanı", priority = 0, readOnly = true, active = false)
    private Instant plannedEndTime;

    @FieldMetadataAnn(title = "İşlem Tarihi", display = true, priority = 90, readOnly = true, filterable = true)
    private Instant checkInTime;*/
/*
    @FieldMetadataAnn(title = "Giriş Boylam", priority = 120, active = false, readOnly = true)
    private Double checkInLongitude;

    @FieldMetadataAnn(title = "Giriş Enlem", priority = 110, active = false, readOnly = true)
    private Double checkInLatitude;
*/

/*
    @FieldMetadataAnn(title = "Çıkış Boylam", priority = 140, active = false, readOnly = true)
    private Double checkOutLongitude;

    @FieldMetadataAnn(title = "Çıkış Enlem", priority = 130, active = false, readOnly = true)
    private Double checkOutLatitude;
*/
    /*@FieldMetadataAnn(title = "Katılımcılar", priority = 150)
    @Column(length = 1024)
    private String participants;*/

    /*@ManyToOne
    @FieldMetadataAnn(title = "Görüşülen Kişi", display = true, priority = 35)
    private Contact contact;*/



    /*@ManyToOne
    @AttributeValueValidate(attributeId = "Müş_Dur")
    @FieldMetadataAnn(title = "Müşteri Durum", display = true, priority = 160, readOnly = true)
    private AttributeValue customerStatus;*/



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

    /*public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
    }
*/
    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
    }
/*
    public AttributeValue getReason() {
        return reason;
    }

    public void setReason(AttributeValue reason) {
        this.reason = reason;
    }*/

    /*public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }*/

   /* public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }
*/
    /*public Instant getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(Instant plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public Instant getPlannedEndTime() {
        return plannedEndTime;
    }

    public void setPlannedEndTime(Instant plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public Instant getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Instant checkInTime) {
        this.checkInTime = checkInTime;
    }*/
/*
    public Double getCheckInLongitude() {
        return checkInLongitude;
    }

    public void setCheckInLongitude(Double checkInLongitude) {
        this.checkInLongitude = checkInLongitude;
    }

    public Double getCheckInLatitude() {
        return checkInLatitude;
    }

    public void setCheckInLatitude(Double checkInLatitude) {
        this.checkInLatitude = checkInLatitude;
    }
*/
    public Instant getCheckOutTime() {
        return checkOutTime;
    }
    public void setCheckOutTime(Instant checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
/*
    public Double getCheckOutLongitude() {
        return checkOutLongitude;
    }

    public void setCheckOutLongitude(Double checkOutLongitude) {
        this.checkOutLongitude = checkOutLongitude;
    }

    public Double getCheckOutLatitude() {
        return checkOutLatitude;
    }

    public void setCheckOutLatitude(Double checkOutLatitude) {
        this.checkOutLatitude = checkOutLatitude;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

   public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
*/
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    /*public AttributeValue getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(AttributeValue customerStatus) {
        this.customerStatus = customerStatus;
    }*/

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
/*
        if (type != null) {
            parts.add(type.getLabel());
        }
*/
        /*if (checkInTime != null) {
            parts.add(DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(checkInTime));
        }

        if (customer != null) {
            parts.add(customer.getName());
        }*/

        //this.subject = String.join(" - ", parts);
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();
/*
        if (type != null) {
            search += type.getLabel();
        }
*/
        /*if (customer != null) {
            search += customer.getName();
        }*/

        search = search.toLowerCase(new Locale("tr", "TR"));
    }


}
