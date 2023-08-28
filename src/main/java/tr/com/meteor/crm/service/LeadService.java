package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Lead;
import tr.com.meteor.crm.repository.LeadRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class LeadService extends GenericIdNameAuditingEntityService<Lead, UUID, LeadRepository> {

    public LeadService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                       BaseConfigurationService baseConfigurationService,
                       LeadRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Lead.class, repository);
    }
}
