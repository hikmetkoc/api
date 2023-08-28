package tr.com.meteor.crm.domain;

import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "survey-questions", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Anket Sorusu", pluralTitle = "Anket Soruları"
)
@Table(indexes = {@Index(columnList = "search")})
public class SurveyQuestion extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Soru", display = true, search = true, required = true, priority = 10)
    private String name;

    @FieldMetadataAnn(title = "Konu", display = true, required = true, priority = 20, defaultValue = "AnS_Tip_Varsayılan")
    @AttributeValueValidate(attributeId = "AnS_Tip")
    @ManyToOne
    private AttributeValue type;

    @FieldMetadataAnn(required = true, title = "Aktif ?", display = true, priority = 30, defaultValue = "false")
    private Boolean active;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
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

    public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (name != null) {
            search += name;
        }

        if (type != null) {
            search += type.getLabel();
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
