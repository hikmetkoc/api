package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.FileContainer;
import tr.com.meteor.crm.repository.FileContainerRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.MobileNotificationService;

import java.util.UUID;

@Component(FileContainerTrigger.QUALIFIER)
public class FileContainerTrigger extends Trigger<FileContainer, UUID, FileContainerRepository> {
    final static String QUALIFIER = "FileContainerTrigger";

    private final CustomerRepository customerRepository;

    private final MailService mailService;

    private final MobileNotificationService mobileNotificationService;

    public FileContainerTrigger(CacheManager cacheManager, FileContainerRepository repository, BaseUserService baseUserService,
                                BaseConfigurationService baseConfigurationService, CustomerRepository customerRepository,
                                MailService mailService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, FileContainer.class, FileContainerTrigger.class, repository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
        this.mailService = mailService;
        this.mobileNotificationService = mobileNotificationService;
    }
}
