package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.CorrectGroup;
import tr.com.meteor.crm.repository.CorrectGroupRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import java.util.UUID;

@Component(CorrectGroupTrigger.QUALIFIER)
public class CorrectGroupTrigger extends Trigger<CorrectGroup, UUID, CorrectGroupRepository> {
    final static String QUALIFIER = "CorrectGroupTrigger";

    public CorrectGroupTrigger(CacheManager cacheManager, CorrectGroupRepository repository,
                               BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, CorrectGroup.class, CorrectGroupTrigger.class, repository, baseUserService, baseConfigurationService);
    }
}
