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
@EntityMetadataAnn(
    apiName = "vocationdays", displayField = "instanceName", title = "Tati Günü", pluralTitle = "Tatil Günleri"
)
@Table(indexes = {@Index(columnList = "search")})
public class VocationDay extends IdNameAuditingEntity<UUID> {
    @FieldMetadataAnn(title = "Gün Başlığı", search = true, readOnly = false, priority = 0, active = false)
    private String name;
    @ManyToOne
    @FieldMetadataAnn(title = "İzni Oluşturan", display = true, readOnly = true, priority = 0, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @FieldMetadataAnn(title = "Tatil Başlangıç Tarihi", display = true, priority = 10, filterable = true)
    private Instant holStart;

    @FieldMetadataAnn(title = "Tatil Bitiş Tarihi", display = true, priority = 20, filterable = true)
    private Instant holEnd;

    @FieldMetadataAnn(title = "Açıklama", display = true, readOnly = true, priority = 140)
    @Column(length = 2048)
    private String description;
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

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }
        search = search.toLowerCase(new Locale("tr", "TR"));
    }


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Instant getHolStart() {
        return holStart;
    }

    public void setHolStart(Instant holStart) {
        this.holStart = holStart;
    }

    public Instant getHolEnd() {
        return holEnd;
    }

    public void setHolEnd(Instant holEnd) {
        this.holEnd = holEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
