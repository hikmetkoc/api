package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Announcement;
import tr.com.meteor.crm.repository.AnnouncementRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.service.PostaGuverciniService;

import java.util.UUID;

@Component(AnnouncementTrigger.QUALIFIER)
public class AnnouncementTrigger extends Trigger<Announcement, UUID, AnnouncementRepository> {
    final static String QUALIFIER = "AnnouncementTrigger";
    private final PostaGuverciniService postaGuverciniService;

    private final MobileNotificationService notificationService;

    public AnnouncementTrigger(CacheManager cacheManager, AnnouncementRepository repository,
                               BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                               PostaGuverciniService postaGuverciniService, MobileNotificationService notificationService) {
        super(cacheManager, Announcement.class, AnnouncementTrigger.class, repository, baseUserService, baseConfigurationService);
        this.postaGuverciniService = postaGuverciniService;
        this.notificationService = notificationService;
    }

    @Override
    public Announcement afterInsert(Announcement newEntity) throws Exception {
        //notificationService.sendAnnouncementNotification(newEntity);
        postaGuverciniService.SendSmsService("5442458391", "DENEME");

        return newEntity;
    }
}
