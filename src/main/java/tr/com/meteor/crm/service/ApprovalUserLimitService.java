package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.ApprovalUserLimit;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.repository.ApprovalUserLimitRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApprovalUserLimitService extends GenericIdNameAuditingEntityService<ApprovalUserLimit, UUID, ApprovalUserLimitRepository> {

    public ApprovalUserLimitService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                    BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                    BaseConfigurationService baseConfigurationService,
                                    ApprovalUserLimitRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            ApprovalUserLimit.class, repository);
    }
}
