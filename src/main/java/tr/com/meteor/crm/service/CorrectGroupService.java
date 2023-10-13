package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.CorrectGroup;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.repository.CorrectGroupRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CorrectGroupService extends GenericIdNameAuditingEntityService<CorrectGroup, UUID, CorrectGroupRepository> {

    public CorrectGroupService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                               BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                               BaseConfigurationService baseConfigurationService,
                               CorrectGroupRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            CorrectGroup.class, repository);
    }
}
