package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "contproducts", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Ürün", pluralTitle = "Ürünler",
    ownerPath = "owner.id", assignerPath = "assigner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class ContProduct extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Ürün Adı", search = true, readOnly = true, priority = 10, active = false)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @FieldMetadataAnn(title = "Onay Kişisi", display = true, priority = 5, readOnly = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @FieldMetadataAnn(title = "Talep Eden", display = true, priority = 0, readOnly = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;
    /*@ManyToOne(fetch=FetchType.LAZY)
    @FieldMetadataAnn(title = "Talep Türü", defaultValue = "Talep_Hizmet", display = true, priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Talepler")
    private AttributeValue talepturu;*/
    @FieldMetadataAnn(title = "Tanımı", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String description;
    @FieldMetadataAnn(title = "Özelliği", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String ozellik;
    @FieldMetadataAnn(title = "Miktarı", priority = 110)
    private Integer miktar;
    @FieldMetadataAnn(title = "Gerekçe", display = true, priority = 120, filterable = true)
    @Column(length = 2048)
    private String gerekce;
    @FieldMetadataAnn(title = "Tutar(TL)", priority = 100, display = true)
    private BigDecimal fuelTl;
    /*@ManyToOne
    @FieldMetadataAnn(title = "Önem Derecesi", defaultValue = "Gör_Imp_Orta", display = true, priority = 65, filterable = true)
    @AttributeValueValidate(attributeId = "Gör_Imp")
    private AttributeValue importance;*/
    @FieldMetadataAnn(title = "Onay Durumu", display = true)
    private Boolean status = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @FieldMetadataAnn(title = "Satın Alma Talebi", priority = 0)
    private Buy buy;
    @Formula("description")
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

    /*public AttributeValue getTalepturu() {
        return talepturu;
    }

    public void setTalepturu(AttributeValue talepturu) {
        this.talepturu = talepturu;
    }*/


    public Integer getMiktar() {
        return miktar;
    }

    public void setMiktar(Integer miktar) {
        this.miktar = miktar;
    }

    public String getOzellik() {
        return ozellik;
    }

    public void setOzellik(String ozellik) {
        this.ozellik = ozellik;
    }

    public String getGerekce() {
        return gerekce;
    }

    public void setGerekce(String gerekce) {
        this.gerekce = gerekce;
    }

    /*public AttributeValue getImportance() {
        return importance;
    }

    public void setImportance(AttributeValue importance) {
        this.importance = importance;
    }*/
    public Buy getBuy() {
        return buy;
    }
    public void setBuy(Buy buy) {
        this.buy = buy;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }


}
