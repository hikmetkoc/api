package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.BuyLimit;
import tr.com.meteor.crm.repository.BuyLimitRepository;
import tr.com.meteor.crm.repository.BuyRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class BuyLimitService extends GenericIdNameAuditingEntityService<BuyLimit, UUID, BuyLimitRepository> {

    private BuyRepository buyRepository;
    public BuyLimitService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService,
                           BuyLimitRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            BuyLimit.class, repository);
        this.buyRepository = buyRepository;
    }
}
