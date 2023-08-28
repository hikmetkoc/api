package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.repository.AddressRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AddressService extends GenericIdNameAuditingEntityService<Address, UUID, AddressRepository> {

    public AddressService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                          BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                          BaseConfigurationService baseConfigurationService,
                          AddressRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Address.class, repository);
    }
}
