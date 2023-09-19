package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Ikfile;
import tr.com.meteor.crm.domain.Resign;
import tr.com.meteor.crm.repository.IkfileRepository;
import tr.com.meteor.crm.repository.ResignRepository;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.configuration.Configurations;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(ResignTrigger.QUALIFIER)
public class ResignTrigger extends Trigger<Resign, UUID, ResignRepository>{
    final static String QUALIFIER = "ResignTrigger";
    private final UserRepository userRepository;

    private final MailService mailService;
    public ResignTrigger(CacheManager cacheManager, ResignRepository repository,
                         BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                         UserRepository userRepository, MailService mailService) {
        super(cacheManager, Resign.class, ResignTrigger.class, repository, baseUserService, baseConfigurationService);
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Override
    public Resign beforeInsert(@NotNull Resign newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        return newEntity;
    }
    @Override
    public Resign beforeUpdate(@NotNull Resign oldEntity, @NotNull Resign newEntity) throws Exception {
        return newEntity;
    }
}
