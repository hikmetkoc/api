package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.SurveyQuestion;
import tr.com.meteor.crm.repository.SurveyQuestionRepository;
import tr.com.meteor.crm.service.SurveyQuestionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/survey-questions")
public class SurveyQuestionController extends GenericIdNameAuditingEntityController<SurveyQuestion, UUID, SurveyQuestionRepository, SurveyQuestionService> {

    public SurveyQuestionController(SurveyQuestionService service) {
        super(service);
    }
}
