package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Locale;

@Entity
@IdType(idType = IdType.IdTypeEnum.String)
@EntityMetadataAnn(apiName = "attributes", displayField = "instanceName", title = "Değişken", pluralTitle = "Değişkenler")
@Table(indexes = {@Index(columnList = "search")})
public class Attribute extends IdNameEntity<String> {

    @FieldMetadataAnn(readOnly = true, required = true, title = "Nesne Adı", display = true, priority = 10, search = true)
    private String objectName;

    @FieldMetadataAnn(readOnly = true, required = true, title = "Alan Adı", display = true, priority = 20, search = true)
    private String fieldName;

    @Formula("concat(object_name,' ',field_name)")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @PrePersist
    private void prePersist() {
        String objName = StringUtils.isBlank(objectName)
            ? ""
            : objectName.substring(0, objectName.length() >= 3
            ? 3
            : objectName.length());

        String fName = StringUtils.isBlank(fieldName)
            ? ""
            : fieldName.substring(0, fieldName.length() >= 3
            ? 3
            : fieldName.length());

        setId(objName + "_" + fName);
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(objectName)) {
            search += " " + objectName;
        }

        if (StringUtils.isNotBlank(fieldName)) {
            search += " " + fieldName;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
