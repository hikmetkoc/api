package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.config.BackendVersionUpdater;
import tr.com.meteor.crm.domain.Permission;
import tr.com.meteor.crm.repository.ConfigurationRepository;
import tr.com.meteor.crm.repository.PermissionRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import java.util.UUID;

@Component(PermissionTrigger.QUALIFIER)
public class PermissionTrigger extends Trigger<Permission, UUID, PermissionRepository> {
    final static String QUALIFIER = "PermissionTrigger";

    private final ConfigurationRepository configurationRepository;

    public PermissionTrigger(CacheManager cacheManager, PermissionRepository taskRepository,
                             BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                             ConfigurationRepository configurationRepository) {
        super(cacheManager, Permission.class, PermissionTrigger.class, taskRepository, baseUserService, baseConfigurationService);
        this.configurationRepository = configurationRepository;
    }

    @Override
    public Permission afterInsert(Permission newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public Permission afterUpdate(Permission oldEntity, Permission newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public void afterDelete(Permission entity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
    }
}
