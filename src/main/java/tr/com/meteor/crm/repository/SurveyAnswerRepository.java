package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.SurveyAnswer;

import java.util.List;
import java.util.UUID;

@Repository
public interface SurveyAnswerRepository extends GenericIdNameAuditingEntityRepository<SurveyAnswer, UUID> {

    List<SurveyAnswer> findAllBySurveyQuestion_IdIn(List<UUID> questionIds);
}
