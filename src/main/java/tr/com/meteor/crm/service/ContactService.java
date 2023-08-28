package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Contact;
import tr.com.meteor.crm.repository.ContactRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContactService extends GenericIdNameAuditingEntityService<Contact, UUID, ContactRepository> {

    public ContactService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                          BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                          BaseConfigurationService baseConfigurationService,
                          ContactRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Contact.class, repository);
    }
}
