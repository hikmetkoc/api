package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.config.BackendVersionUpdater;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.repository.AttributeValueRepository;
import tr.com.meteor.crm.repository.ConfigurationRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

@Component(AttributeValueTrigger.QUALIFIER)
public class AttributeValueTrigger extends Trigger<AttributeValue, String, AttributeValueRepository> {
    final static String QUALIFIER = "AttributeValueTrigger";

    private final ConfigurationRepository configurationRepository;

    public AttributeValueTrigger(CacheManager cacheManager, AttributeValueRepository taskRepository,
                                 BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                                 ConfigurationRepository configurationRepository) {
        super(cacheManager, AttributeValue.class, AttributeValueTrigger.class, taskRepository, baseUserService, baseConfigurationService);
        this.configurationRepository = configurationRepository;
    }

    @Override
    public AttributeValue afterInsert(AttributeValue newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public AttributeValue afterUpdate(AttributeValue oldEntity, AttributeValue newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public void afterDelete(AttributeValue entity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
    }

    @Override
    public void onClearCache(AttributeValue entity) {
        cacheManager.getCache(AttributeValueRepository.cacheName).clear();
    }
}
