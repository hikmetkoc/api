package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.ApprovalUserLimit;
import tr.com.meteor.crm.domain.CompanyMaterial;
import tr.com.meteor.crm.repository.ApprovalUserLimitRepository;
import tr.com.meteor.crm.repository.CompanyMaterialRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import java.util.UUID;

@Component(ApprovalUserLimitTrigger.QUALIFIER)
public class ApprovalUserLimitTrigger extends Trigger<ApprovalUserLimit, UUID, ApprovalUserLimitRepository> {
    final static String QUALIFIER = "ApprovalUserLimitTrigger";

    public ApprovalUserLimitTrigger(CacheManager cacheManager, ApprovalUserLimitRepository repository,
                                    BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, ApprovalUserLimit.class, ApprovalUserLimitTrigger.class, repository, baseUserService, baseConfigurationService);
    }
}
