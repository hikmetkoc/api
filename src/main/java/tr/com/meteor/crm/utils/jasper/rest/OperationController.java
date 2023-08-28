package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Operation;
import tr.com.meteor.crm.repository.OperationRepository;
import tr.com.meteor.crm.service.OperationService;

@RestController
@RequestMapping("/api/operations")
public class OperationController extends GenericIdNameAuditingEntityController<Operation, String, OperationRepository, OperationService> {

    public OperationController(OperationService service) {
        super(service);
    }
}
