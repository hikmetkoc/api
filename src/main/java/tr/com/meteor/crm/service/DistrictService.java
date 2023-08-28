package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.District;
import tr.com.meteor.crm.repository.DistrictRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class DistrictService extends GenericIdNameAuditingEntityService<District, UUID, DistrictRepository> {

    public DistrictService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService,
                           DistrictRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            District.class, repository);
    }
}
