package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "countries", displayField = "instanceName", title = "Ülke", pluralTitle = "Ülkeler")
@Table(indexes = {@Index(columnList = "search")})
public class Country extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "Ülke İsmi", search = true, display = true, priority = 10)
    private String name;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
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

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
