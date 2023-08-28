package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Ikfile;
import tr.com.meteor.crm.domain.UserPermission;
import tr.com.meteor.crm.repository.IkfileRepository;
import tr.com.meteor.crm.repository.UserPermissionRepository;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.configuration.Configurations;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(UserPermissionTrigger.QUALIFIER)
public class UserPermissionTrigger extends Trigger<UserPermission, UUID, UserPermissionRepository>{
    final static String QUALIFIER = "UserPermissionTrigger";
    private final UserRepository userRepository;

    private final MailService mailService;
    public UserPermissionTrigger(CacheManager cacheManager, UserPermissionRepository repository,
                                 BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                                 UserRepository userRepository, MailService mailService) {
        super(cacheManager, UserPermission.class, UserPermissionTrigger.class, repository, baseUserService, baseConfigurationService);
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Override
    public UserPermission beforeInsert(@NotNull UserPermission newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        return newEntity;
    }
    @Override
    public UserPermission beforeUpdate(@NotNull UserPermission oldEntity, @NotNull UserPermission newEntity) throws Exception {

        return newEntity;
    }
}
