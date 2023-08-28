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
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "customer-survey-answers", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Müşteri Anket Cevabı", pluralTitle = "Müşteri Anket Cevapları"
)
@Table(indexes = {@Index(columnList = "search")})
public class CustomerSurveyAnswer extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Müşteri", display = true, required = true, priority = 20)
    @ManyToOne
    private Customer customer;

    @FieldMetadataAnn(title = "Cevap", display = true, required = true, priority = 20)
    @ManyToOne
    private SurveyAnswer surveyAnswer;

    @FieldMetadataAnn(title = "Zaman", display = true, search = true, required = true, priority = 30)
    private Instant time;

    @Formula("id")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public SurveyAnswer getSurveyAnswer() {
        return surveyAnswer;
    }

    public void setSurveyAnswer(SurveyAnswer answer) {
        this.surveyAnswer = answer;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
