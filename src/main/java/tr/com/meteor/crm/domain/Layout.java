package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "layouts", displayField = "instanceName", title = "Tasarım Planı", pluralTitle = "Tasarım Planları")
@Table(indexes = {@Index(columnList = "search")})
public class Layout extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "Tasarım Plan İsmi", search = true)
    private String name;

    @FieldMetadataAnn(required = true, title = "Nesne Adı", search = true)
    private String objectName;

    @FieldMetadataAnn(required = true, title = "Json")
    @Column(length = 2048)
    private String json;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Instance Name", active = false)
    private String instanceName;

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        if (StringUtils.isNotBlank(objectName)) {
            search += " " + objectName;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
