package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "correct_groups", displayField = "instanceName", title = "Onay Grubu", pluralTitle = "Onay Grupları")
@Table(indexes = {@Index(columnList = "search")})
public class CorrectGroup extends IdNameAuditingEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_id")
    @FieldMetadataAnn(title = "Şef", priority = 5, active = false)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User chief;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @FieldMetadataAnn(title = "Şef", priority = 5, active = false)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    @FieldMetadataAnn(title = "Şef", priority = 5, active = false)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User director;
    @ManyToOne
    @JoinColumn(name = "approval_group_id")
    @FieldMetadataAnn(title = "Onay Grubu", defaultValue = "Approval_Group_Merkez", priority = 20, filterable = true)
    @AttributeValueValidate(attributeId = "Approval_Group")
    private AttributeValue approvalGroup;

    @FieldMetadataAnn(readOnly = true, title = "Ödeme Grubu Açıklaması", active = false)
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

        if (StringUtils.isNotBlank(approvalGroup.getLabel())) {
            search += " " + approvalGroup.getLabel();
        }

        if (StringUtils.isNotBlank(approvalGroup.getLabel())) {
            search += " " + approvalGroup.getLabel();
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
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

    public User getDirector() {
        return director;
    }

    public void setDirector(User director) {
        this.director = director;
    }

    public AttributeValue getApprovalGroup() {
        return approvalGroup;
    }

    public void setApprovalGroup(AttributeValue approvalGroup) {
        this.approvalGroup = approvalGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
