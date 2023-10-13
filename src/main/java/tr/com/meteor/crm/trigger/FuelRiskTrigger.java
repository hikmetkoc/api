package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Announcement;
import tr.com.meteor.crm.domain.FuelRisk;
import tr.com.meteor.crm.repository.AnnouncementRepository;
import tr.com.meteor.crm.repository.FuelRiskRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.service.PostaGuverciniService;

import java.util.UUID;

@Component(FuelRiskTrigger.QUALIFIER)
public class FuelRiskTrigger extends Trigger<FuelRisk, UUID, FuelRiskRepository> {
    final static String QUALIFIER = "FuelRiskTrigger";

    public FuelRiskTrigger(CacheManager cacheManager, FuelRiskRepository repository,
                           BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, FuelRisk.class, FuelRiskTrigger.class, repository, baseUserService, baseConfigurationService);
    }
}
