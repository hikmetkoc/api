package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.ProjTask;
import tr.com.meteor.crm.domain.Project;
import tr.com.meteor.crm.repository.ProjTaskRepository;
import tr.com.meteor.crm.repository.ProjectRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(ProjTaskTrigger.QUALIFIER)
public class ProjTaskTrigger extends Trigger<ProjTask, UUID, ProjTaskRepository> {
    final static String QUALIFIER = "ProjTaskTrigger";

    public ProjTaskTrigger(CacheManager cacheManager, ProjTaskRepository contactRepository,
                           BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, ProjTask.class, ProjTaskTrigger.class, contactRepository, baseUserService, baseConfigurationService);
    }

    @Override
    public ProjTask beforeInsert(@NotNull ProjTask newEntity) throws Exception {
        return newEntity;
    }

    @Override
    public ProjTask beforeUpdate(@NotNull ProjTask oldEntity, @NotNull ProjTask newEntity) throws Exception {
        return newEntity;
    }


}
