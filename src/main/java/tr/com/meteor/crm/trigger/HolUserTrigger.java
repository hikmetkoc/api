package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.Holiday;
import tr.com.meteor.crm.repository.HolUserRepository;
import tr.com.meteor.crm.repository.HolidayRepository;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.UserService;
import tr.com.meteor.crm.utils.attributevalues.ContractStatus;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.util.UUID;

@Component(HolUserTrigger.QUALIFIER)
public class HolUserTrigger extends Trigger<HolUser, UUID, HolUserRepository> {
    final static String QUALIFIER = "HolUserTrigger";

    private final UserRepository userRepository;

    private final UserService userService;
    private final MailService mailService;
    public HolUserTrigger(CacheManager cacheManager, HolUserRepository holUserRepository,
                          BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                          UserRepository userRepository, MailService mailService,
                          UserService userService) {
        super(cacheManager, HolUser.class, HolUserTrigger.class, holUserRepository, baseUserService, baseConfigurationService);
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userService = userService;
    }

    @Override
    public HolUser beforeInsert(@NotNull HolUser newEntity) throws Exception {
        return newEntity;
    }

    @Override
    public HolUser afterInsert(@NotNull HolUser newEntity) throws Exception {
        return newEntity;
    }
}
