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
@EntityMetadataAnn(apiName = "cities", displayField = "instanceName", title = "Şehir", pluralTitle = "Şehirler")
@Table(indexes = {@Index(columnList = "search")})
public class City extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "Şehir İsmi", display = true, priority = 10, search = true)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(required = true, title = "Ülke", display = true, priority = 20, active = false)
    private Country country;

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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
