package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "permissions", displayField = "instanceName", title = "İzin", pluralTitle = "İzinler")
@Table(indexes = {@Index(columnList = "search")})
public class Permission extends IdNameAuditingEntity<UUID> {

    @ManyToOne
    @FieldMetadataAnn(title = "Rol", display = true, priority = 10)
    private Role role;

    @FieldMetadataAnn(title = "Nesne Adı", search = true, display = true, priority = 20)
    private String objectName;

    @FieldMetadataAnn(title = "Oku", priority = 30)
    private Boolean read;

    @FieldMetadataAnn(title = "Güncelle", priority = 40)
    private Boolean update;

    @FieldMetadataAnn(title = "Sil", priority = 50)
    private Boolean delete;

    @FieldMetadataAnn(title = "Hiyerarşi", priority = 60)
    private Boolean isHierarchical;

    @Formula("concat(role_id,' - ',object_name)")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Boolean isRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean isUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean isDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Boolean isHierarchical() {
        return isHierarchical;
    }

    public void setHierarchical(Boolean hierarchical) {
        isHierarchical = hierarchical;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(objectName)) {
            search += " " + objectName;
        }

        if (role != null) {
            search += " " + role.getId();
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
