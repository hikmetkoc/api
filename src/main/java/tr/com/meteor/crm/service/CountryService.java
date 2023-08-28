package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Country;
import tr.com.meteor.crm.repository.CountryRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountryService extends GenericIdNameAuditingEntityService<Country, UUID, CountryRepository> {

    public CountryService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                          BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                          BaseConfigurationService baseConfigurationService,
                          CountryRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Country.class, repository);
    }
}
