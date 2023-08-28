package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.EntityMetadata;
import tr.com.meteor.crm.repository.EntityMetadataRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class EntityMetadataService extends GenericIdNameAuditingEntityService<EntityMetadata, String, EntityMetadataRepository> {

    public EntityMetadataService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                 BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                 BaseConfigurationService baseConfigurationService,
                                 EntityMetadataRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            EntityMetadata.class, repository);
    }
}
