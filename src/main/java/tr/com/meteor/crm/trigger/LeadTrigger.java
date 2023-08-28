package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Lead;
import tr.com.meteor.crm.repository.LeadRepository;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.utils.Utils;
import tr.com.meteor.crm.utils.attributevalues.LeadStatus;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(LeadTrigger.QUALIFIER)
public class LeadTrigger extends Trigger<Lead, UUID, LeadRepository> {
    final static String QUALIFIER = "LeadTrigger";

    public LeadTrigger(CacheManager cacheManager, LeadRepository leadRepository,
                       BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, Lead.class, LeadTrigger.class, leadRepository, baseUserService, baseConfigurationService);
    }

    @Override
    public Lead beforeInsert(@NotNull Lead newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }

        if (newEntity.getStatus() == null) {
            newEntity.setStatus(LeadStatus.YENI.getAttributeValue());
        }

        updateStatus(null, newEntity);

        return newEntity;
    }

    @Override
    public Lead beforeUpdate(Lead oldEntity, Lead newEntity) throws Exception {
        updateStatus(oldEntity, newEntity);

        return newEntity;
    }

    private void updateStatus(Lead oldEntity, Lead newEntity) {
        if(newEntity.getCustomer() != null && (oldEntity == null || Utils.isChanged(oldEntity.getCustomer(), newEntity.getCustomer()))) {
            newEntity.setStatus(LeadStatus.ISLENDI.getAttributeValue());
        }
    }
}
