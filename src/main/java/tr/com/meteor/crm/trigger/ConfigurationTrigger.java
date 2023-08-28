package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.config.BackendVersionUpdater;
import tr.com.meteor.crm.domain.Configuration;
import tr.com.meteor.crm.repository.ConfigurationRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import java.util.Objects;

@Component(ConfigurationTrigger.QUALIFIER)
public class ConfigurationTrigger extends Trigger<Configuration, String, ConfigurationRepository> {
    final static String QUALIFIER = "ConfigurationTrigger";

    public ConfigurationTrigger(CacheManager cacheManager, ConfigurationRepository taskRepository,
                                BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, Configuration.class, ConfigurationTrigger.class, taskRepository, baseUserService, baseConfigurationService);
    }

    @Override
    public Configuration afterInsert(Configuration newEntity) throws Exception {
        BackendVersionUpdater.update(repository);
        return newEntity;
    }

    @Override
    public Configuration afterUpdate(Configuration oldEntity, Configuration newEntity) throws Exception {
        BackendVersionUpdater.update(repository);
        return newEntity;
    }

    @Override
    public void afterDelete(Configuration entity) throws Exception {
        BackendVersionUpdater.update(repository);
    }

    @Override
    public void onClearCache(Configuration entity) {
        Objects.requireNonNull(cacheManager.getCache(BaseConfigurationService.CONFIGURATION_BY_ID_CACHE)).clear();
    }
}
