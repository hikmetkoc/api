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
@EntityMetadataAnn(apiName = "districts", displayField = "instanceName", title = "İlçe", pluralTitle = "İlçeler")
@Table(indexes = {@Index(columnList = "search")})
public class District extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "İlçe İsmi", search = true, display = true, priority = 10)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(required = true, title = "Şehir", display = true, priority = 20)
    private City city;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", priority = 1)
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        if(city != null) {
            search += city.getName();
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
