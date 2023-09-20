package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Document;
import tr.com.meteor.crm.domain.Document;
import tr.com.meteor.crm.repository.DocumentRepository;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.configuration.Configurations;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(DocumentTrigger.QUALIFIER)
public class DocumentTrigger extends Trigger<Document, UUID, DocumentRepository>{
    final static String QUALIFIER = "DocumentTrigger";
    private final UserRepository userRepository;

    private final MailService mailService;
    public DocumentTrigger(CacheManager cacheManager, DocumentRepository activityRepository,
                           BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                           UserRepository userRepository, MailService mailService) {
        super(cacheManager, Document.class, DocumentTrigger.class, activityRepository, baseUserService, baseConfigurationService);
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Override
    public Document beforeInsert(@NotNull Document newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        return newEntity;
    }
}
