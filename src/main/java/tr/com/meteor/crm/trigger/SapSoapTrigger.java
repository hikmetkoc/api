package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.SapSoap;
import tr.com.meteor.crm.domain.Task;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.SapSoapRepository;
import tr.com.meteor.crm.repository.TaskRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.configuration.Configurations;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(SapSoapTrigger.QUALIFIER)
public class SapSoapTrigger extends Trigger<SapSoap, UUID, SapSoapRepository>{
    final static String QUALIFIER = "SapSoapTrigger";
    private final MailService mailService;

    public SapSoapTrigger(CacheManager cacheManager, SapSoapRepository sapSoapRepository,
                          BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                          TaskRepository taskRepository, MailService mailService) {
        super(cacheManager, SapSoap.class, SapSoapTrigger.class, sapSoapRepository, baseUserService, baseConfigurationService);
        this.mailService = mailService;
    }

    @Override
    public SapSoap beforeInsert(@NotNull SapSoap newEntity) throws Exception {
        return newEntity;
    }
    @Override
    public SapSoap beforeUpdate(@NotNull SapSoap oldEntity, @NotNull SapSoap newEntity) throws Exception {
        return newEntity;
    }
}
