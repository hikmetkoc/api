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
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "approval_user_limit", displayField = "instanceName", title = "Onaylı Kullanıcı Limiti", pluralTitle = "Onaylı Kullanıcı Limitleri")
@Table(indexes = {@Index(columnList = "search")})
public class ApprovalUserLimit extends IdNameAuditingEntity<UUID> {

    @ManyToOne
    @FieldMetadataAnn(title = "Kullanıcı", priority = 5, active = false)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;

    @FieldMetadataAnn(title = "TL Limiti", priority = 103)
    private BigDecimal tlLimit;

    @FieldMetadataAnn(title = "Dolar Limiti", priority = 103)
    private BigDecimal dlLimit;

    @FieldMetadataAnn(title = "Euro Limiti", priority = 103)
    private BigDecimal euroLimit;

    @FieldMetadataAnn(readOnly = true, title = "Onay Kullanıcısı Açıklaması", active = false)
    private String name;
    @Transient
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

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(user.getFullName())) {
            search += " " + user.getFullName();
        }

        if (StringUtils.isNotBlank(user.getFullName())) {
            search += " " + user.getFullName();
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public BigDecimal getTlLimit() {
        return tlLimit;
    }

    public void setTlLimit(BigDecimal tlLimit) {
        this.tlLimit = tlLimit;
    }

    public BigDecimal getDlLimit() {
        return dlLimit;
    }

    public void setDlLimit(BigDecimal dlLimit) {
        this.dlLimit = dlLimit;
    }

    public BigDecimal getEuroLimit() {
        return euroLimit;
    }

    public void setEuroLimit(BigDecimal euroLimit) {
        this.euroLimit = euroLimit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
