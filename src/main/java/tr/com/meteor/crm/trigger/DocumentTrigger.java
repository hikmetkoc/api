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
        //validateCheckInCheckOutDistance(newEntity);

        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }

        //newEntity.setSubject(newEntity.getTask().getType().getLabel());
        //newEntity.setSubjdesc(newEntity.getTask().getSubjectdesc());

        /*if (newEntity.getCustomer() == null) {
            newEntity.getCustomer();
        }*/
        // todo: Talebin durumu YENI ise işlem girildiğinde DEVAM EDİYOR olarak değiştir.
        /*
        if(newEntity.getTask().getStatus().getId().equals(TaskStatus.YENI.getId())) {
            Optional<Task> task = taskRepository.findById(newEntity.getTask().getId());
            if (task.isPresent()) {
                task.get().setStatus(TaskStatus.DEVAM_EDIYOR.getAttributeValue());
                taskRepository.save(task.get());
            }
        }*/

        //Yeni İşlem Yapıldığında, İşlemi Yapan Kişi Talebi giren kişi değilse Mail AT..
        /*if (newEntity.getOwner()!=newEntity.getTask().getOwner()) {
            Optional<Task> task = taskRepository.findById(newEntity.getTask().getId());
            if (task.isPresent()) {
                mailService.sendEmail(newEntity.getTask().getAssigner().getEmail(),
                    "MeteorPanel - Yeni İşlem", newEntity.getOwner().getFullName() + ",  " +
                    newEntity.getTask().getType().getLabel() + " konulu talebinize yeni bir işlem yaptı.", false, false);
            }
        }*/
        return newEntity;
    }
    @Override
    public Document beforeUpdate(@NotNull Document oldEntity, @NotNull Document newEntity) throws Exception {
        //validateCheckInCheckOutDistance(newEntity);
        //newEntity.setSubject(newEntity.getUser().getType().getLabel());
        //newEntity.setSubjdesc(newEntity.getUser().getSubjectdesc());
        return newEntity;
    }
    private void validateCheckInCheckOutDistance(Document activity) throws Exception {
        Integer checkInOutMax = baseConfigurationService.getConfigurationById(Configurations.CHECKIN_CHECKOUT_MAKSIMUM_MESAFE.getId()).getIntegerValue();
/*
        if (activity.getCheckInLatitude() != null && activity.getCheckInLongitude() != null
            && activity.getCheckOutLatitude() != null && activity.getCheckOutLongitude() != null) {
            if (checkInOutMax != null && LocationUtils.distanceInMeterBetweenEarthCoordinates(activity.getCheckInLatitude(),
                activity.getCheckInLongitude(), activity.getCheckOutLatitude(), activity.getCheckOutLongitude()) > checkInOutMax) {
                throw new Exception(checkInOutMax + " m den uzakta checkout yapılamaz.");
            }
        }*/
    }
}
