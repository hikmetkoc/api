package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.ProjOfficer;
import tr.com.meteor.crm.domain.ProjTask;
import tr.com.meteor.crm.repository.ProjOfficerRepository;
import tr.com.meteor.crm.repository.ProjTaskRepository;
import tr.com.meteor.crm.service.ProjOfficerService;
import tr.com.meteor.crm.service.ProjTaskService;

import java.util.UUID;

@RestController
@RequestMapping("/api/proj_officers")
public class ProjOfficerController extends GenericIdNameAuditingEntityController<ProjOfficer, UUID, ProjOfficerRepository, ProjOfficerService> {

    public ProjOfficerController(ProjOfficerService service) {
        super(service);
    }
}
