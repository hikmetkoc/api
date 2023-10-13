package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Announcement;
import tr.com.meteor.crm.domain.CompanyMaterial;
import tr.com.meteor.crm.repository.AnnouncementRepository;
import tr.com.meteor.crm.repository.CompanyMaterialRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.service.PostaGuverciniService;

import java.util.UUID;

@Component(CompanyMaterialTrigger.QUALIFIER)
public class CompanyMaterialTrigger extends Trigger<CompanyMaterial, UUID, CompanyMaterialRepository> {
    final static String QUALIFIER = "CompanyMaterialTrigger";

    public CompanyMaterialTrigger(CacheManager cacheManager, CompanyMaterialRepository repository,
                                  BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, CompanyMaterial.class, CompanyMaterialTrigger.class, repository, baseUserService, baseConfigurationService);
    }
}
