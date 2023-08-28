package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.config.BackendVersionUpdater;
import tr.com.meteor.crm.domain.EntityMetadata;
import tr.com.meteor.crm.repository.ConfigurationRepository;
import tr.com.meteor.crm.repository.EntityMetadataRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

@Component(EntityMetadataTrigger.QUALIFIER)
public class EntityMetadataTrigger extends Trigger<EntityMetadata, String, EntityMetadataRepository> {
    final static String QUALIFIER = "EntityMetadataTrigger";

    private final ConfigurationRepository configurationRepository;

    public EntityMetadataTrigger(CacheManager cacheManager, EntityMetadataRepository taskRepository,
                                 BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                                 ConfigurationRepository configurationRepository) {
        super(cacheManager, EntityMetadata.class, EntityMetadataTrigger.class, taskRepository, baseUserService, baseConfigurationService);
        this.configurationRepository = configurationRepository;
    }

    @Override
    public EntityMetadata afterInsert(EntityMetadata newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public EntityMetadata afterUpdate(EntityMetadata oldEntity, EntityMetadata newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public void afterDelete(EntityMetadata entity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
    }
}
