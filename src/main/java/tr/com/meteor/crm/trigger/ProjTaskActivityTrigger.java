package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.ProjTaskActivity;
import tr.com.meteor.crm.domain.ProjTaskOfficer;
import tr.com.meteor.crm.repository.ProjTaskActivityRepository;
import tr.com.meteor.crm.repository.ProjTaskOfficerRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(ProjTaskActivityTrigger.QUALIFIER)
public class ProjTaskActivityTrigger extends Trigger<ProjTaskActivity, UUID, ProjTaskActivityRepository> {
    final static String QUALIFIER = "ProjTaskActivityTrigger";

    public ProjTaskActivityTrigger(CacheManager cacheManager, ProjTaskActivityRepository contactRepository,
                                   BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, ProjTaskActivity.class, ProjTaskActivityTrigger.class, contactRepository, baseUserService, baseConfigurationService);
    }

    @Override
    public ProjTaskActivity beforeInsert(@NotNull ProjTaskActivity newEntity) throws Exception {
        return newEntity;
    }

    @Override
    public ProjTaskActivity beforeUpdate(@NotNull ProjTaskActivity oldEntity, @NotNull ProjTaskActivity newEntity) throws Exception {
        return newEntity;
    }


}
