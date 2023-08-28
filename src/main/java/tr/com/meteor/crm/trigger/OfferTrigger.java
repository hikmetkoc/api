package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Offer;
import tr.com.meteor.crm.repository.OfferRepository;
import tr.com.meteor.crm.repository.TaskRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(OfferTrigger.QUALIFIER)
public class OfferTrigger extends Trigger<Offer, UUID, OfferRepository>{
    final static String QUALIFIER = "OfferTrigger";
    private final TaskRepository taskRepository;

    private final MailService mailService;
    public OfferTrigger(CacheManager cacheManager, OfferRepository offerRepository,
                        BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                        TaskRepository taskRepository, MailService mailService) {
        super(cacheManager, Offer.class, OfferTrigger.class, offerRepository, baseUserService, baseConfigurationService);
        this.taskRepository = taskRepository;
        this.mailService = mailService;
    }

    @Override
    public Offer beforeInsert(@NotNull Offer newEntity) throws Exception {

        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }

        return newEntity;
    }
    @Override
    public Offer beforeUpdate(@NotNull Offer oldEntity, @NotNull Offer newEntity) throws Exception {
        return newEntity;
    }
}
