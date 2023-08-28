package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.CustomerSurveyAnswer;
import tr.com.meteor.crm.repository.CustomerSurveyAnswerRepository;
import tr.com.meteor.crm.service.CustomerSurveyAnswerService;
import tr.com.meteor.crm.service.dto.CustomerSurveyDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer-survey-answers")
public class CustomerSurveyAnswerController extends GenericIdNameAuditingEntityController<CustomerSurveyAnswer, UUID, CustomerSurveyAnswerRepository, CustomerSurveyAnswerService> {

    public CustomerSurveyAnswerController(CustomerSurveyAnswerService service) {
        super(service);
    }

    @PostMapping("get-all")
    public List<CustomerSurveyDTO> getAll(@RequestParam UUID customerId, @RequestParam(required = false) String typeId) {
        return service.getAll(customerId, typeId);
    }

    @PutMapping("save-all")
    public void saveAll(@RequestParam UUID customerId, @RequestParam(required = false) String typeId,
                        @RequestBody List<CustomerSurveyDTO> customerSurveyDTOS) throws Exception {
        service.saveAll(customerId, typeId, customerSurveyDTOS);
    }
}
