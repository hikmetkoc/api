package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.config.BackendVersionUpdater;
import tr.com.meteor.crm.domain.Attribute;
import tr.com.meteor.crm.repository.AttributeRepository;
import tr.com.meteor.crm.repository.ConfigurationRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

@Component(AttributeTrigger.QUALIFIER)
public class AttributeTrigger extends Trigger<Attribute, String, AttributeRepository> {
    final static String QUALIFIER = "AttributeTrigger";

    private final ConfigurationRepository configurationRepository;

    public AttributeTrigger(CacheManager cacheManager, AttributeRepository taskRepository,
                            BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                            ConfigurationRepository configurationRepository) {
        super(cacheManager, Attribute.class, AttributeTrigger.class, taskRepository, baseUserService, baseConfigurationService);
        this.configurationRepository = configurationRepository;
    }

    @Override
    public Attribute afterInsert(Attribute newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public Attribute afterUpdate(Attribute oldEntity, Attribute newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public void afterDelete(Attribute entity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
    }

    @Override
    public void onClearCache(Attribute entity) {
        cacheManager.getCache(AttributeRepository.cacheName).clear();
    }
}
