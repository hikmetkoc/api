package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.config.BackendVersionUpdater;
import tr.com.meteor.crm.domain.FieldMetadata;
import tr.com.meteor.crm.repository.ConfigurationRepository;
import tr.com.meteor.crm.repository.FieldMetadataRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

@Component(FieldMetadataTrigger.QUALIFIER)
public class FieldMetadataTrigger extends Trigger<FieldMetadata, String, FieldMetadataRepository> {
    final static String QUALIFIER = "FieldMetadataTrigger";

    private final ConfigurationRepository configurationRepository;

    public FieldMetadataTrigger(CacheManager cacheManager, FieldMetadataRepository taskRepository,
                                BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                                ConfigurationRepository configurationRepository) {
        super(cacheManager, FieldMetadata.class, FieldMetadataTrigger.class, taskRepository, baseUserService, baseConfigurationService);
        this.configurationRepository = configurationRepository;
    }

    @Override
    public FieldMetadata afterInsert(FieldMetadata newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public FieldMetadata afterUpdate(FieldMetadata oldEntity, FieldMetadata newEntity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
        return newEntity;
    }

    @Override
    public void afterDelete(FieldMetadata entity) throws Exception {
        BackendVersionUpdater.update(configurationRepository);
    }
}
