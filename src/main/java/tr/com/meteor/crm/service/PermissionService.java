package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Permission;
import tr.com.meteor.crm.repository.PermissionRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionService extends GenericIdNameAuditingEntityService<Permission, UUID, PermissionRepository> {

    public PermissionService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                             BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                             BaseConfigurationService baseConfigurationService,
                             PermissionRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Permission.class, repository);
    }
}
