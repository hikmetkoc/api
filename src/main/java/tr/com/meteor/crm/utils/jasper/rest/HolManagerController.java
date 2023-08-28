package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.HolManager;
import tr.com.meteor.crm.repository.HolManagerRepository;
import tr.com.meteor.crm.service.HolManagerService;

import java.util.UUID;

@RestController
@RequestMapping("/api/holmanagers")
public class HolManagerController extends GenericIdNameAuditingEntityController<HolManager, UUID, HolManagerRepository, HolManagerService> {

    public HolManagerController(HolManagerService service) {
        super(service);
    }

}
