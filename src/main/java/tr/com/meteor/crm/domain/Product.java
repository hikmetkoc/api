package tr.com.meteor.crm.domain;

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
@EntityMetadataAnn(apiName = "products", displayField = "instanceName", title = "Ürün", pluralTitle = "Ürünler")
@Table(indexes = {@Index(columnList = "search")})
public class Product extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Ürün İsmi", search = true, display = true, priority = 10)
    private String name;

    @FieldMetadataAnn(title = "Açıklama", priority = 32)
    @Column(length = 2048)
    private String description;

    @ManyToOne
    @FieldMetadataAnn(title = "Ölçü Birimi", priority = 31)
    @AttributeValueValidate(attributeId = "Ürü_Ölç")
    private AttributeValue unitOfMeasure;

    @ManyToOne
    /*@FieldMetadataAnn(title = "Üst Ürün", display = true, priority = 20)*/
    private Product parent;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AttributeValue getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(AttributeValue unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Product getParent() {
        return parent;
    }

    public void setParent(Product parent) {
        this.parent = parent;
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
