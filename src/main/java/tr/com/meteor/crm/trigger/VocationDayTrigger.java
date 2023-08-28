package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.VocationDay;
import tr.com.meteor.crm.repository.HolUserRepository;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.repository.VocationDayRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(VocationDayTrigger.QUALIFIER)
public class VocationDayTrigger extends Trigger<VocationDay, UUID, VocationDayRepository> {
    final static String QUALIFIER = "VocationDayTrigger";

    private final UserRepository userRepository;

    private final UserService userService;
    private final MailService mailService;
    public VocationDayTrigger(CacheManager cacheManager, VocationDayRepository vocationDayRepository,
                              BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                              UserRepository userRepository, MailService mailService,
                              UserService userService) {
        super(cacheManager, VocationDay.class, VocationDayTrigger.class, vocationDayRepository, baseUserService, baseConfigurationService);
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userService = userService;
    }

    @Override
    public VocationDay beforeInsert(@NotNull VocationDay newEntity) throws Exception {
        return newEntity;
    }

    @Override
    public VocationDay afterInsert(@NotNull VocationDay newEntity) throws Exception {
        newEntity.setOwner(getCurrentUser());
        return newEntity;
    }
}
