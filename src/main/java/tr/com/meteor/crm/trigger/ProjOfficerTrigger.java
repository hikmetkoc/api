package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.ProjOfficer;
import tr.com.meteor.crm.domain.ProjTask;
import tr.com.meteor.crm.repository.ProjOfficerRepository;
import tr.com.meteor.crm.repository.ProjTaskRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(ProjOfficerTrigger.QUALIFIER)
public class ProjOfficerTrigger extends Trigger<ProjOfficer, UUID, ProjOfficerRepository> {
    final static String QUALIFIER = "ProjOfficerTrigger";

    public ProjOfficerTrigger(CacheManager cacheManager, ProjOfficerRepository contactRepository,
                              BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, ProjOfficer.class, ProjOfficerTrigger.class, contactRepository, baseUserService, baseConfigurationService);
    }

    @Override
    public ProjOfficer beforeInsert(@NotNull ProjOfficer newEntity) throws Exception {
        return newEntity;
    }

    @Override
    public ProjOfficer beforeUpdate(@NotNull ProjOfficer oldEntity, @NotNull ProjOfficer newEntity) throws Exception {
        return newEntity;
    }


}
