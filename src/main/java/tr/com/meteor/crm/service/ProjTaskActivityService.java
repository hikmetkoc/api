package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.ProjTaskActivity;
import tr.com.meteor.crm.domain.ProjTaskOfficer;
import tr.com.meteor.crm.repository.ProjTaskActivityRepository;
import tr.com.meteor.crm.repository.ProjTaskOfficerRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjTaskActivityService extends GenericIdNameAuditingEntityService<ProjTaskActivity, UUID, ProjTaskActivityRepository> {

    public ProjTaskActivityService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                   BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                   BaseConfigurationService baseConfigurationService,
                                   ProjTaskActivityRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            ProjTaskActivity.class, repository);
    }
}
