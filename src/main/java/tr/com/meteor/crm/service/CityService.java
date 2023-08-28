package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.City;
import tr.com.meteor.crm.repository.CityRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CityService extends GenericIdNameAuditingEntityService<City, UUID, CityRepository> {

    public CityService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                       BaseConfigurationService baseConfigurationService,
                       CityRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            City.class, repository);
    }
}
