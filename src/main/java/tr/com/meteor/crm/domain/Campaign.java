package tr.com.meteor.crm.domain;

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
@EntityMetadataAnn(apiName = "campaigns", displayField = "instanceName", title = "Kampanya", pluralTitle = "Kampanyalar")
@Table(indexes = {@Index(columnList = "search")})
public class Campaign extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "Kampanya İsmi", search = true, display = true, priority = 10)
    private String name;

    @FieldMetadataAnn(title = "Başlangıç Tarihi", display = true, priority = 20)
    private Instant startDate;

    @FieldMetadataAnn(title = "Bitiş Tarihi", display = true, priority = 30)
    private Instant endDate;

    @FieldMetadataAnn(title = "Açıklama", priority = 53)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Beklenen Gelir", priority = 51)
    private BigDecimal expectedRevenue;

    @FieldMetadataAnn(title = "Bütçe", priority = 52)
    private BigDecimal budget;

    @FieldMetadataAnn(required = true, title = "Durum", display = true, priority = 40, defaultValue = "Kam_Dur_Aktif")
    @AttributeValueValidate(attributeId = "Kam_Dur")
    @ManyToOne
    private AttributeValue status;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(BigDecimal expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
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

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}



