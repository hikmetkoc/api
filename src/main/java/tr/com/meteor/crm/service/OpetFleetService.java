package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.OpetFleet;
import tr.com.meteor.crm.repository.OpetFleetRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class OpetFleetService extends GenericIdEntityService<OpetFleet, UUID, OpetFleetRepository> {

    public OpetFleetService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                            BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                            BaseConfigurationService baseConfigurationService,
                            OpetFleetRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            OpetFleet.class, repository);
    }
}
