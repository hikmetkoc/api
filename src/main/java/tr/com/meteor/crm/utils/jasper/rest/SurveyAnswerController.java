package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.SurveyAnswer;
import tr.com.meteor.crm.repository.SurveyAnswerRepository;
import tr.com.meteor.crm.service.SurveyAnswerService;

import java.util.UUID;

@RestController
@RequestMapping("/api/survey-answers")
public class SurveyAnswerController extends GenericIdNameAuditingEntityController<SurveyAnswer, UUID, SurveyAnswerRepository, SurveyAnswerService> {

    public SurveyAnswerController(SurveyAnswerService service) {
        super(service);
    }
}
