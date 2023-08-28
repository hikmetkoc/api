package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.FieldMetadata;
import tr.com.meteor.crm.repository.FieldMetadataRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class FieldMetadataService extends GenericIdNameAuditingEntityService<FieldMetadata, String, FieldMetadataRepository> {

    public FieldMetadataService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                BaseConfigurationService baseConfigurationService,
                                FieldMetadataRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            FieldMetadata.class, repository);
    }
}
