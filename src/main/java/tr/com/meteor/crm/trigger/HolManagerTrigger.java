package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.BuyLimit;
import tr.com.meteor.crm.domain.HolManager;
import tr.com.meteor.crm.repository.BuyLimitRepository;
import tr.com.meteor.crm.repository.BuyRepository;
import tr.com.meteor.crm.repository.HolManagerRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.MobileNotificationService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(HolManagerTrigger.QUALIFIER)
public class HolManagerTrigger extends Trigger<HolManager, UUID, HolManagerRepository> {
    final static String QUALIFIER = "HolManagerTrigger";

    private final MailService mailService;

    private final MobileNotificationService mobileNotificationService;

    public HolManagerTrigger(CacheManager cacheManager, BaseUserService baseUserService,
                             BaseConfigurationService baseConfigurationService,
                             HolManagerRepository holManagerRepository, MailService mailService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, HolManager.class, HolManagerTrigger.class, holManagerRepository, baseUserService, baseConfigurationService);
        this.mailService = mailService;
        this.mobileNotificationService = mobileNotificationService;
    }

    @Override
    public HolManager beforeInsert(@NotNull HolManager newEntity) throws Exception {
        return newEntity;
    }
    @Override
    public HolManager beforeUpdate(HolManager oldEntity, HolManager newEntity) throws Exception {
        return newEntity;
    }
}
