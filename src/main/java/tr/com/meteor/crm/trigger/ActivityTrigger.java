package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Contact;
import tr.com.meteor.crm.domain.Quote;
import tr.com.meteor.crm.domain.Task;
import tr.com.meteor.crm.repository.TaskRepository;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.Utils;
import tr.com.meteor.crm.utils.attributevalues.*;
import tr.com.meteor.crm.utils.configuration.Configurations;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(ActivityTrigger.QUALIFIER)
public class ActivityTrigger extends Trigger<Activity, UUID, ActivityRepository>{
    final static String QUALIFIER = "ActivityTrigger";
    private final TaskRepository taskRepository;

    private final MailService mailService;
    public ActivityTrigger(CacheManager cacheManager, ActivityRepository activityRepository,
                           BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                           TaskRepository taskRepository, MailService mailService) {
        super(cacheManager, Activity.class, ActivityTrigger.class, activityRepository, baseUserService, baseConfigurationService);
        this.taskRepository = taskRepository;
        this.mailService = mailService;
    }

    @Override
    public Activity beforeInsert(@NotNull Activity newEntity) throws Exception {
        //validateCheckInCheckOutDistance(newEntity);

        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        newEntity.setSubject(newEntity.getTask().getType().getLabel());
        newEntity.setSubjdesc(newEntity.getTask().getSubjectdesc());

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
        if (newEntity.getOwner()!=newEntity.getTask().getOwner()) {
            Optional<Task> task = taskRepository.findById(newEntity.getTask().getId());
            if (task.isPresent()) {
                mailService.sendEmail(newEntity.getTask().getAssigner().getEposta(),
                    "MeteorPanel - Yeni İşlem", newEntity.getOwner().getFullName() + ",  " +
                    newEntity.getTask().getType().getLabel() + " konulu talebinize yeni bir işlem yaptı.", false, false);
            }
        }
        return newEntity;
    }
    @Override
    public Activity beforeUpdate(@NotNull Activity oldEntity, @NotNull Activity newEntity) throws Exception {
        validateCheckInCheckOutDistance(newEntity);
        newEntity.setSubject(newEntity.getTask().getType().getLabel());
        newEntity.setSubjdesc(newEntity.getTask().getSubjectdesc());
        return newEntity;
    }
    private void validateCheckInCheckOutDistance(Activity activity) throws Exception {
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
