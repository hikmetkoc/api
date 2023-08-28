package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@Entity
@IdType(idType = IdType.IdTypeEnum.String)
@EntityMetadataAnn(apiName = "attribute-values", displayField = "instanceName", title = "Değişken Değeri", pluralTitle = "Değişken Değerleri")
@Table(indexes = {@Index(columnList = "search")})
public class AttributeValue extends IdNameEntity<String> {

    @FieldMetadataAnn(required = true, title = "Etiket", search = true, display = true, priority = 10)
    private String label;

    @ManyToOne
    @FieldMetadataAnn(required = true, title = "Değişken Adı", display = true, priority = 20)
    private Attribute attribute;


    @Formula("label")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    @FieldMetadataAnn(required = true, title = "Statik", display = true, priority = 30)
    private Boolean isStatic;

    @FieldMetadataAnn(required = true, title = "Ağırlık", display = true, priority = 40)
    private Integer weight = 0;

    @FieldMetadataAnn(required = true, title = "İlgili Birim", search = true, display = true, priority = 10)
    private String ilgilibirim;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public Boolean getIsStatic() {
        return isStatic;
    }

    public void setIsStatic(Boolean aStatic) {
        isStatic = aStatic;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    private void prePersist() {
        String attId = attribute == null
            ? ""
            : attribute.getId();

        setId(attId + "_" + label);
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(label)) {
            search += " " + label;
        }

        if (attribute != null) {
            search += " " + attribute.getSearch();
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }


    public String getIlgilibirim() {
        return ilgilibirim;
    }

    public void setIlgilibirim(String ilgilibirim) {
        this.ilgilibirim = ilgilibirim;
    }
}
