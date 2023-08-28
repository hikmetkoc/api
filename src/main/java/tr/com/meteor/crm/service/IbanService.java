package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.Iban;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.repository.IbanRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class IbanService extends GenericIdNameAuditingEntityService<Iban, UUID, IbanRepository> {

    public IbanService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                       BaseConfigurationService baseConfigurationService,
                       IbanRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Iban.class, repository);
    }
}
