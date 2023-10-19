package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Announcement;
import tr.com.meteor.crm.domain.Project;
import tr.com.meteor.crm.repository.AnnouncementRepository;
import tr.com.meteor.crm.repository.ProjectRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectService extends GenericIdNameAuditingEntityService<Project, UUID, ProjectRepository> {

    public ProjectService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                          BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                          BaseConfigurationService baseConfigurationService,
                          ProjectRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Project.class, repository);
    }
}
