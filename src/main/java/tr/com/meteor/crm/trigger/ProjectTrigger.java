package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Contact;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.domain.Project;
import tr.com.meteor.crm.repository.ContactRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.ProjectRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(ProjectTrigger.QUALIFIER)
public class ProjectTrigger extends Trigger<Project, UUID, ProjectRepository> {
    final static String QUALIFIER = "ProjectTrigger";

    public ProjectTrigger(CacheManager cacheManager, ProjectRepository contactRepository,
                          BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, Project.class, ProjectTrigger.class, contactRepository, baseUserService, baseConfigurationService);
    }

    @Override
    public Project beforeInsert(@NotNull Project newEntity) throws Exception {
        return newEntity;
    }

    @Override
    public Project beforeUpdate(@NotNull Project oldEntity, @NotNull Project newEntity) throws Exception {
        return newEntity;
    }


}
