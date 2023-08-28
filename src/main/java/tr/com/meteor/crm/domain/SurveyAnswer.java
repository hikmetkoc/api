package tr.com.meteor.crm.domain;

import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "survey-answers", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Anket Cevabı", pluralTitle = "Anket Cevapları"
)
@Table(indexes = {@Index(columnList = "search")})
public class SurveyAnswer extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Cevap", display = true, search = true, required = true, priority = 10)
    private String name;

    @FieldMetadataAnn(title = "Soru", display = true, required = true, priority = 20)
    @ManyToOne
    private SurveyQuestion surveyQuestion;

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

    public SurveyQuestion getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(SurveyQuestion question) {
        this.surveyQuestion = question;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (name != null) {
            search += name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
