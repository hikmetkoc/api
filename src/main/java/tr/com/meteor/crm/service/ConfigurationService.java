package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Configuration;
import tr.com.meteor.crm.repository.ConfigurationRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class ConfigurationService extends GenericIdNameAuditingEntityService<Configuration, String, ConfigurationRepository> {

    public ConfigurationService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                BaseConfigurationService baseConfigurationService,
                                ConfigurationRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Configuration.class, repository);
    }

    public Configuration getConfiguration(String id) {
        Configuration c = baseConfigurationService.getConfigurationById(id);
        return c;
    }

    public Configuration findById(String id) {
        return repository.findById(id).get();
    }

    public void updateConfiguration(Configuration configuration) {
        repository.save(configuration);
    }
}
