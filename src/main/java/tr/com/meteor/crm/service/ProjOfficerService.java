package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.ProjOfficer;
import tr.com.meteor.crm.domain.ProjTask;
import tr.com.meteor.crm.repository.ProjOfficerRepository;
import tr.com.meteor.crm.repository.ProjTaskRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjOfficerService extends GenericIdNameAuditingEntityService<ProjOfficer, UUID, ProjOfficerRepository> {

    public ProjOfficerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                              BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                              BaseConfigurationService baseConfigurationService,
                              ProjOfficerRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            ProjOfficer.class, repository);
    }
}
