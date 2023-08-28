package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.SurveyQuestion;

import java.util.List;
import java.util.UUID;

@Repository
public interface SurveyQuestionRepository extends GenericIdNameAuditingEntityRepository<SurveyQuestion, UUID> {

    List<SurveyQuestion> findAllByActiveIsTrueAndType_Id(String typeId);
}
