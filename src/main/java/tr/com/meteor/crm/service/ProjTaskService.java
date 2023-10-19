package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.ProjTask;
import tr.com.meteor.crm.domain.Project;
import tr.com.meteor.crm.repository.ProjTaskRepository;
import tr.com.meteor.crm.repository.ProjectRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjTaskService extends GenericIdNameAuditingEntityService<ProjTask, UUID, ProjTaskRepository> {

    public ProjTaskService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService,
                           ProjTaskRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            ProjTask.class, repository);
    }
}
