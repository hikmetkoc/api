package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Target;
import tr.com.meteor.crm.repository.TargetRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(TargetTrigger.QUALIFIER)
public class TargetTrigger extends Trigger<Target, UUID, TargetRepository> {
    final static String QUALIFIER = "TargetTrigger";

    public TargetTrigger(CacheManager cacheManager, TargetRepository targetRepository, BaseUserService baseUserService,
                         BaseConfigurationService baseConfigurationService) {
        super(cacheManager, Target.class, TargetTrigger.class, targetRepository, baseUserService, baseConfigurationService);
    }

    @Override
    public Target beforeInsert(@NotNull Target newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }

        return newEntity;
    }
}
