package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.ProjOfficer;
import tr.com.meteor.crm.domain.ProjTaskOfficer;
import tr.com.meteor.crm.repository.ProjOfficerRepository;
import tr.com.meteor.crm.repository.ProjTaskOfficerRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjTaskOfficerService extends GenericIdNameAuditingEntityService<ProjTaskOfficer, UUID, ProjTaskOfficerRepository> {

    public ProjTaskOfficerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                  BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                  BaseConfigurationService baseConfigurationService,
                                  ProjTaskOfficerRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            ProjTaskOfficer.class, repository);
    }
}
