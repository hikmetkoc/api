package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.ProjOfficer;
import tr.com.meteor.crm.domain.ProjTaskOfficer;
import tr.com.meteor.crm.repository.ProjOfficerRepository;
import tr.com.meteor.crm.repository.ProjTaskOfficerRepository;
import tr.com.meteor.crm.service.ProjOfficerService;
import tr.com.meteor.crm.service.ProjTaskOfficerService;

import java.util.UUID;

@RestController
@RequestMapping("/api/proj_task_officers")
public class ProjTaskOfficerController extends GenericIdNameAuditingEntityController<ProjTaskOfficer, UUID, ProjTaskOfficerRepository, ProjTaskOfficerService> {

    public ProjTaskOfficerController(ProjTaskOfficerService service) {
        super(service);
    }
}
