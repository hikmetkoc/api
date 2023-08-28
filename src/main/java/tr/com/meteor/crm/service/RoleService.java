package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.repository.RoleRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService extends GenericIdEntityService<Role, String, RoleRepository> {

    public RoleService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                       BaseConfigurationService baseConfigurationService,
                       RoleRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Role.class, repository);
    }
}
