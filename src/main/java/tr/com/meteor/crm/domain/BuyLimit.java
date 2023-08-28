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
import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "buylimits", displayField = "instanceName", title = "Limit", pluralTitle = "Limitler",
    ownerPath = "user.id", assignerPath = "chief.id", otherPath = "manager.id")
@Table(indexes = {@Index(columnList = "search")})
public class BuyLimit extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Limit", search = true, readOnly = true, priority = 10, active = false)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Maliyet Yeri", defaultValue = "Cost_Place_MeteorMerkez", display = true, priority = 5, filterable = true)
    @AttributeValueValidate(attributeId = "Cost_Place")
    private AttributeValue maliyet;
    @ManyToOne
    @FieldMetadataAnn(title = "Satın Alma Onaycısı", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;
    @ManyToOne
    @FieldMetadataAnn(title = "Satın Alma Sorumlusu", display = true, priority = 0, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User chief;

    @ManyToOne
    @FieldMetadataAnn(title = "Satın Alma Sorumlusu", display = true, priority = 0, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User manager;

    @ManyToOne
    @FieldMetadataAnn(title = "Satın Alma Sorumlusu", display = true, priority = 0, readOnly = true, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User director;

    @FieldMetadataAnn(title = "Personel Limiti(TL)", priority = 100, display = true, required = true)
    private BigDecimal userTl;

    @FieldMetadataAnn(title = "Personel Limiti($)", priority = 101, display = true, required = true)
    private BigDecimal userDl;

    @FieldMetadataAnn(title = "Şef Limiti(TL)", priority = 102, display = true, required = true)
    private BigDecimal chiefTl;

    @FieldMetadataAnn(title = "Şef Limiti($)", priority = 103, display = true, required = true)
    private BigDecimal chiefDl;
    @FieldMetadataAnn(title = "Müdür Limiti(TL)", priority = 104, display = true, required = true)
    private BigDecimal managerTl;

    @FieldMetadataAnn(title = "Müdür Limiti($)", priority = 105, display = true, required = true)
    private BigDecimal managerDl;

    @FieldMetadataAnn(title = "Üst Müdür Limiti(TL)", priority = 106, display = true, required = true)
    private BigDecimal directorTl;

    @FieldMetadataAnn(title = "Müdür Limiti($)", priority = 107, display = true, required = true)
    private BigDecimal directorDl;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getChief() {
        return chief;
    }

    public void setChief(User chief) {
        this.chief = chief;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public BigDecimal getUserTl() {
        return userTl;
    }

    public void setUserTl(BigDecimal userTl) {
        this.userTl = userTl;
    }

    public BigDecimal getChiefTl() {
        return chiefTl;
    }

    public void setChiefTl(BigDecimal chiefTl) {
        this.chiefTl = chiefTl;
    }

    public BigDecimal getManagerTl() {
        return managerTl;
    }

    public void setManagerTl(BigDecimal managerTl) {
        this.managerTl = managerTl;
    }
    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public User getDirector() {
        return director;
    }

    public void setDirector(User director) {
        this.director = director;
    }

    public BigDecimal getDirectorTl() {
        return directorTl;
    }

    public BigDecimal getUserDl() {
        return userDl;
    }

    public void setUserDl(BigDecimal userDl) {
        this.userDl = userDl;
    }

    public BigDecimal getChiefDl() {
        return chiefDl;
    }

    public void setChiefDl(BigDecimal chiefDl) {
        this.chiefDl = chiefDl;
    }

    public BigDecimal getManagerDl() {
        return managerDl;
    }

    public void setManagerDl(BigDecimal managerDl) {
        this.managerDl = managerDl;
    }

    public BigDecimal getDirectorDl() {
        return directorDl;
    }

    public void setDirectorDl(BigDecimal directorDl) {
        this.directorDl = directorDl;
    }

    public AttributeValue getMaliyet() {
        return maliyet;
    }

    public void setMaliyet(AttributeValue maliyet) {
        this.maliyet = maliyet;
    }
}
