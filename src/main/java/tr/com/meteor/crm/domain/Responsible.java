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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "responsibles", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Tedarikçi Sorumlusu", pluralTitle = "Tedarikçi Sorumluları",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Responsible extends IdNameAuditingEntity<UUID> {

    @ManyToOne
    @FieldMetadataAnn(title = "Tedarikçi", display = true, priority = 5, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Customer customer;
    @FieldMetadataAnn(title = "Sorumlu", display = true, priority = 10, filterable = true)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @FieldMetadataAnn(title = "Atayan Muhasebeci", display = true, readOnly = true, priority = 0, filterable = true)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;

    @ManyToOne
    @FieldMetadataAnn(title = "Öncelik Sırası", defaultValue = "Res_Onc_1", display = true, priority = 140)
    @AttributeValueValidate(attributeId = "Res_Onc")
    private AttributeValue oncelik;

    @FieldMetadataAnn(title = "Açıklama")
    @Column(length = 2048)
    private String description;

    @Formula("description")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

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
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        search = search.toLowerCase(new Locale("tr", "TR"));
    }


    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AttributeValue getOncelik() {
        return oncelik;
    }

    public void setOncelik(AttributeValue oncelik) {
        this.oncelik = oncelik;
    }
}
