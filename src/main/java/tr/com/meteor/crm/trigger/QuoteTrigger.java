package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.utils.attributevalues.ActivityType;
import tr.com.meteor.crm.utils.attributevalues.ContractType;
import tr.com.meteor.crm.utils.attributevalues.QuoteApprovalStatus;
import tr.com.meteor.crm.utils.configuration.Configurations;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.Quote;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.QuoteRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(QuoteTrigger.QUALIFIER)
public class QuoteTrigger extends Trigger<Quote, UUID, QuoteRepository> {
    final static String QUALIFIER = "QuoteTrigger";

    private final ActivityRepository activityRepository;

    public QuoteTrigger(CacheManager cacheManager, QuoteRepository quoteRepository, BaseUserService baseUserService,
                        BaseConfigurationService baseConfigurationService, ActivityRepository activityRepository) {
        super(cacheManager, Quote.class, QuoteTrigger.class, quoteRepository, baseUserService, baseConfigurationService);
        this.activityRepository = activityRepository;
    }

    @Override
    public Quote beforeInsert(@NotNull Quote quote) throws Exception {
        if (quote.getOwner() == null) {
            quote.setOwner(getCurrentUser());
        }
        return quote;
    }

    @Override
    public Quote beforeUpdate(@NotNull Quote oldEntity, @NotNull Quote newEntity) throws Exception {
        return newEntity;
    }


}

