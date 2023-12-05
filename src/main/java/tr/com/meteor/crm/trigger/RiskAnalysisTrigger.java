package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Behavior;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.domain.MotionSums;
import tr.com.meteor.crm.domain.RiskAnalysis;
import tr.com.meteor.crm.repository.BehaviorRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.MotionSumsRepository;
import tr.com.meteor.crm.repository.RiskAnalysisRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.attributevalues.BehaviorStatus;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(RiskAnalysisTrigger.QUALIFIER)
public class RiskAnalysisTrigger extends Trigger<RiskAnalysis, UUID, RiskAnalysisRepository> {
    final static String QUALIFIER = "RiskAnalysisTrigger";

    private final MailService mailService;
    public RiskAnalysisTrigger(CacheManager cacheManager, RiskAnalysisRepository activityRepository,
                               BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                               MailService mailService) {
        super(cacheManager, RiskAnalysis.class, RiskAnalysisTrigger.class, activityRepository, baseUserService, baseConfigurationService);
        this.mailService = mailService;
    }

    @Override
    public RiskAnalysis beforeInsert(@NotNull RiskAnalysis newEntity) throws Exception {
        return newEntity;
    }

    @Override
    public RiskAnalysis beforeUpdate(@NotNull RiskAnalysis oldEntity, @NotNull RiskAnalysis newEntity) throws Exception {
        return newEntity;
    }
}
