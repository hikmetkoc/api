package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.attributevalues.ConfigurationType;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;

@Entity
@IdType(idType = IdType.IdTypeEnum.String)
@EntityMetadataAnn(apiName = "configurations", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Konfigürasyon", pluralTitle = "Konfigürasyonlar"
)
@Table(indexes = {@Index(columnList = "search")})
public class Configuration extends IdNameAuditingEntity<String> {

    @FieldMetadataAnn(readOnly = true, title = "İsim", search = true, display = true, priority = 10)
    private String name;

    @FieldMetadataAnn(readOnly = true, title = "Tip", display = true, priority = 20)
    @AttributeValueValidate(attributeId = "Kon_Tip")
    @ManyToOne
    private AttributeValue valueType;

    @FieldMetadataAnn(title = "Değer", display = true, priority = 30)
    private String storedValue;

    @Transient
    @FieldMetadataAnn(title = "Değer")
    private Object value;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeValue getValueType() {
        return valueType;
    }

    public void setValueType(AttributeValue valueType) {
        this.valueType = valueType;
    }

    public String getStoredValue() {
        return storedValue;
    }

    public void setStoredValue(String storedValue) {
    }

    public Object getValue() {
        if (valueType == null) return null;

        if (valueType.getId().equals(ConfigurationType.INTEGER.getAttributeValue().getId())) {
            return Integer.parseInt(storedValue);
        } else if (valueType.getId().equals(ConfigurationType.DOUBLE.getAttributeValue().getId())) {
            return Double.parseDouble(storedValue);
        } else if (valueType.getId().equals(ConfigurationType.LONG.getAttributeValue().getId())) {
            return Long.parseLong(storedValue);
        } else if (valueType.getId().equals(ConfigurationType.INSTANT.getAttributeValue().getId())) {
            return Instant.parse(storedValue);
        } else if (valueType.getId().equals(ConfigurationType.LOCAL_DATE.getAttributeValue().getId())) {
            return LocalDate.parse(storedValue);
        } else if (valueType.getId().equals(ConfigurationType.BOOLEAN.getAttributeValue().getId())) {
            return Boolean.valueOf(storedValue);
        } else {
            return storedValue;
        }
    }

    public void setValue(Object value) {
        this.value = value;
        this.storedValue = value == null ? null : value.toString();
    }

    @JsonIgnore
    public Integer getIntegerValue() {
        return (Integer) getValue();
    }

    @JsonIgnore
    public Double getDoubleValue() {
        return (Double) getValue();
    }

    @JsonIgnore
    public Long getLongValue() {
        return (Long) getValue();
    }

    @JsonIgnore
    public Instant getInstantValue() {
        return (Instant) getValue();
    }

    @JsonIgnore
    public LocalDate getLocalDateValue() {
        return (LocalDate) getValue();
    }

    @JsonIgnore
    public Boolean getBooleanValue() {
        return (Boolean) getValue();
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @PrePersist
    private void prePersist() {
        setId(name.replaceAll("[^\\dA-Za-z ]", "_").replaceAll("\\s+", "_"));
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        if (StringUtils.isNotBlank(storedValue)) {
            search += " " + storedValue;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
