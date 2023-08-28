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
@EntityMetadataAnn(
    apiName = "exuseholidays", displayField = "instanceName", title = "Mazeret İzni Talebi", pluralTitle = "Mazeret İzinleri",
    ownerPath = "owner.id", assignerPath = "assigner.id", otherPath="user.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class ExuseHoliday extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "İzin Başlığı", search = true, readOnly = false, priority = 0, active = false)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Talebi Oluşturan", display = false, priority = 0, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;
    @ManyToOne
    @FieldMetadataAnn(title = "Talep Eden", priority = 0, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @FieldMetadataAnn(title = "Onaycı", display = true, priority = 10, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;
    @FieldMetadataAnn(title = "Açıklama", priority = 140)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "İzin Başlangıç Tarihi", display = true, priority = 60, filterable = true)
    private Instant startDate;

    @FieldMetadataAnn(title = "İzin Bitiş Tarihi", display = true, priority = 70, filterable = true)
    private Instant endDate;

    @FieldMetadataAnn(title = "İşe Dönüş Tarihi", display = true, priority = 80, filterable = true)
    private Instant comeDate;

    @FieldMetadataAnn(title = "Kullanılan İzin Günü", display = true, priority = 300, filterable = true)
    private Double izingun = 0.00;

    @ManyToOne
    @AttributeValueValidate(attributeId = "Izin_Dur")
    @FieldMetadataAnn(title = "Onay Durumu", display = true, defaultValue = "Izin_Dur_Pasif", priority = 150, filterable = true)
    private AttributeValue approvalStatus;
    @Formula("name")
    @FieldMetadataAnn(title = "Başlık", active = false)
    private String instanceName;
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
    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }
        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public AttributeValue getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(AttributeValue approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getComeDate() {
        return comeDate;
    }

    public void setComeDate(Instant comeDate) {
        this.comeDate = comeDate;
    }

    public Double getIzingun() {
        return izingun;
    }

    public void setIzingun(Double izingun) {
        this.izingun = izingun;
    }
}
