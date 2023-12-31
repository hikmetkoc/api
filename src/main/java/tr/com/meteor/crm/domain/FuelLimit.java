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
@EntityMetadataAnn(apiName = "fuellimits", displayField = "instanceName", title = "Yakıt Limit", pluralTitle = "Yakıt Limitleri",
    ownerPath = "owner.id", assignerPath = "assigner.id")
@Table(indexes = {@Index(columnList = "search")})
public class FuelLimit extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Yakıt Limitleri", search = true, readOnly = true, priority = 5, active = false)
    private String name;
    @ManyToOne
    @FieldMetadataAnn(title = "Talebi Oluşturan", display = true, priority = 10, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;
    @ManyToOne
    @FieldMetadataAnn(title = "Onaycı", display = true, priority = 2, readOnly = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;

    @FieldMetadataAnn(title = "İşlem Tarihi", display = true, priority = 60)
    private Instant okeyFirst;

    @FieldMetadataAnn(title = "Cari Kod", display = true, priority = 20, filterable = true)
    @Column(length = 2048)
    private String curcode;
    @FieldMetadataAnn(title = "Ek Limit Gerekçesi", display = true, priority = 40, filterable = true)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Tutar", active = false)
    private BigDecimal fuelTl;

    @ManyToOne
    @FieldMetadataAnn(title = "Tutar", defaultValue = "Fuel_Limit_Total_3", display = true, priority = 30, filterable = true)
    @AttributeValueValidate(attributeId = "Fuel_Limit_Total")
    private AttributeValue total;
    @ManyToOne
    @FieldMetadataAnn(title = "Onay Durumu", defaultValue = "Fuel_Dur_Bekle", display = true, readOnly = true, priority = 140, filterable = true)
    @AttributeValueValidate(attributeId = "Fuel_Dur")
    private AttributeValue status;
    @FieldMetadataAnn(title = "Başlangıç Tarihi", display = true, readOnly = true, priority = 60)
    private Instant startDate;

    @FieldMetadataAnn(title = "Bitiş Tarihi", display = true, readOnly = true, priority = 70)
    private Instant endDate;

    @FieldMetadataAnn(title = "Cari Unvan", display = true, readOnly = true, priority = 80, filterable = true)
    private String unvan;

    @FieldMetadataAnn(title = "Toplam Kullanılabilir Limit", readOnly = true)
    private BigDecimal totalTl;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFuelTl() {
        return fuelTl;
    }

    public void setFuelTl(BigDecimal fuelTl) {
        this.fuelTl = fuelTl;
    }

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
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

    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
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
    public Instant getOkeyFirst() {
        return okeyFirst;
    }

    public void setOkeyFirst(Instant okeyFirst) {
        this.okeyFirst = okeyFirst;
    }

    public String getCurcode() {
        return curcode;
    }

    public void setCurcode(String curcode) {
        this.curcode = curcode;
    }

    public String getUnvan() {
        return unvan;
    }

    public void setUnvan(String unvan) {
        this.unvan = unvan;
    }

    public BigDecimal getTotalTl() {
        return totalTl;
    }

    public void setTotalTl(BigDecimal totalTl) {
        this.totalTl = totalTl;
    }

    public AttributeValue getTotal() {
        return total;
    }

    public void setTotal(AttributeValue total) {
        this.total = total;
    }
}
