package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Material;
import tr.com.meteor.crm.repository.MaterialRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialService extends GenericIdNameAuditingEntityService<Material, UUID, MaterialRepository> {

    public MaterialService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService,
                           MaterialRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Material.class, repository);
    }
}
