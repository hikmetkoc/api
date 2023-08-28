package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Operation;
import tr.com.meteor.crm.repository.OperationRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationService extends GenericIdNameAuditingEntityService<Operation, String, OperationRepository> {

    public OperationService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                            BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                            BaseConfigurationService baseConfigurationService,
                            OperationRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Operation.class, repository);
    }
}
