package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.*;
import java.util.Locale;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "operations", displayField = "instanceName", title = "Operasyon", pluralTitle = "Operasyonlar")
@Table(indexes = {@Index(columnList = "search")})
public class Operation extends IdNameAuditingEntity<String> {

    @FieldMetadataAnn(readOnly = true, title = "Operasyon Adı", search = true, display = true, priority = 10)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(readOnly = true, title = "Üst Operasyon", priority = 20)
    private Operation parent;

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

    public Operation getParent() {
        return parent;
    }

    public void setParent(Operation parent) {
        this.parent = parent;
    }

    @PrePersist
    private void prePersist() {
        setId(name.replaceAll("[^\\dA-Za-z ]", "_").replaceAll("\\s+", "_"));
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
