package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.CustomerSurveyAnswer;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerSurveyAnswerRepository extends GenericIdNameAuditingEntityRepository<CustomerSurveyAnswer, UUID> {

    List<CustomerSurveyAnswer> findAllByCustomer_IdAndSurveyAnswer_SurveyQuestion_IdIn(UUID customerId, List<UUID> questionIds);

    List<CustomerSurveyAnswer> findAllByCustomer_IdAndSurveyAnswer_SurveyQuestion_Id(UUID customerId, UUID questionId);
}
