package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.BuyLimit;
import tr.com.meteor.crm.repository.BuyLimitRepository;
import tr.com.meteor.crm.repository.BuyRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.MobileNotificationService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(BuyLimitTrigger.QUALIFIER)
public class BuyLimitTrigger extends Trigger<BuyLimit, UUID, BuyLimitRepository> {
    final static String QUALIFIER = "BuyLimitTrigger";
    private final BuyRepository buyRepository;

    private final MailService mailService;

    private final MobileNotificationService mobileNotificationService;

    public BuyLimitTrigger(CacheManager cacheManager, BuyLimitRepository buylimitRepository, BaseUserService baseUserService,
                           BaseConfigurationService baseConfigurationService,
                           BuyRepository buyRepository, MailService mailService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, BuyLimit.class, BuyLimitTrigger.class, buylimitRepository, baseUserService, baseConfigurationService);
        this.buyRepository = buyRepository;
        this.mailService = mailService;
        this.mobileNotificationService = mobileNotificationService;
    }

    @Override
    public BuyLimit beforeInsert(@NotNull BuyLimit newEntity) throws Exception {
        return newEntity;
    }
    @Override
    public BuyLimit beforeUpdate(BuyLimit oldEntity, BuyLimit newEntity) throws Exception {
        return newEntity;
    }
}
