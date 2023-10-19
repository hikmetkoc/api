package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.ProjTask;
import tr.com.meteor.crm.domain.Project;
import tr.com.meteor.crm.repository.ProjTaskRepository;
import tr.com.meteor.crm.repository.ProjectRepository;
import tr.com.meteor.crm.service.ProjTaskService;
import tr.com.meteor.crm.service.ProjectService;

import java.util.UUID;

@RestController
@RequestMapping("/api/proj_tasks")
public class ProjTaskController extends GenericIdNameAuditingEntityController<ProjTask, UUID, ProjTaskRepository, ProjTaskService> {

    public ProjTaskController(ProjTaskService service) {
        super(service);
    }
}
