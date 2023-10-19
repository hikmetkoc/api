package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.*;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "user_acceptances", displayField = "instanceName", title = "Zimmet", pluralTitle = "Personel Zimmeti",
    ownerPath = "user.id", assignerPath = "assigner.id")
@Table(indexes = {@Index(columnList = "search")})
public class UserAcceptance extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Zimmet", search = true, readOnly = true, active = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @FieldMetadataAnn(title = "Personel", filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "assigner_id")
    @FieldMetadataAnn(title = "Zimmetleyen", filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;

    @ManyToOne
    @JoinColumn(name = "material_id")
    @FieldMetadataAnn(title = "Demirbaş", filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Material material;

    @FieldMetadataAnn(title = "Teslim Alma Tarihi", display = true)
    private Instant receiveDate;

    @FieldMetadataAnn(title = "Teslim Etme Tarihi", display = true)
    private Instant submitDate;

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

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }

    public Instant getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Instant receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Instant getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Instant submitDate) {
        this.submitDate = submitDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
