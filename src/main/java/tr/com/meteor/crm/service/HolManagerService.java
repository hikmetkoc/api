package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.BuyLimit;
import tr.com.meteor.crm.domain.HolManager;
import tr.com.meteor.crm.repository.BuyLimitRepository;
import tr.com.meteor.crm.repository.BuyRepository;
import tr.com.meteor.crm.repository.HolManagerRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class HolManagerService extends GenericIdNameAuditingEntityService<HolManager, UUID, HolManagerRepository> {

    public HolManagerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                             BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                             BaseConfigurationService baseConfigurationService,
                             HolManagerRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            HolManager.class, repository);
    }
}
