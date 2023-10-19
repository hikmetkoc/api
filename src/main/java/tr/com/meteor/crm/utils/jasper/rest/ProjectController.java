package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Material;
import tr.com.meteor.crm.domain.Project;
import tr.com.meteor.crm.repository.MaterialRepository;
import tr.com.meteor.crm.repository.ProjectRepository;
import tr.com.meteor.crm.service.MaterialService;
import tr.com.meteor.crm.service.ProjectService;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController extends GenericIdNameAuditingEntityController<Project, UUID, ProjectRepository, ProjectService> {

    public ProjectController(ProjectService service) {
        super(service);
    }
}
