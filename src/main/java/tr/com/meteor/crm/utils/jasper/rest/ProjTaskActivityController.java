package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.ProjTaskActivity;
import tr.com.meteor.crm.domain.ProjTaskOfficer;
import tr.com.meteor.crm.repository.ProjTaskActivityRepository;
import tr.com.meteor.crm.repository.ProjTaskOfficerRepository;
import tr.com.meteor.crm.service.ProjTaskActivityService;
import tr.com.meteor.crm.service.ProjTaskOfficerService;

import java.util.UUID;

@RestController
@RequestMapping("/api/proj_task_activities")
public class ProjTaskActivityController extends GenericIdNameAuditingEntityController<ProjTaskActivity, UUID, ProjTaskActivityRepository, ProjTaskActivityService> {

    public ProjTaskActivityController(ProjTaskActivityService service) {
        super(service);
    }
}
