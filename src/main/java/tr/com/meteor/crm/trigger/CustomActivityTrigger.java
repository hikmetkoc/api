package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.CustomActivity;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.CustomActivityRepository;
import tr.com.meteor.crm.repository.CustomTaskRepository;
import tr.com.meteor.crm.repository.TaskRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.configuration.Configurations;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component(CustomActivityTrigger.QUALIFIER)
public class CustomActivityTrigger extends Trigger<CustomActivity, UUID, CustomActivityRepository> {
    final static String QUALIFIER = "CustomActivityTrigger";

    private final CustomTaskRepository taskRepository;

    private final MailService mailService;
    public CustomActivityTrigger(CacheManager cacheManager, CustomActivityRepository activityRepository,
                                 BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                                 CustomTaskRepository taskRepository, MailService mailService) {
        super(cacheManager, CustomActivity.class, CustomActivityTrigger.class, activityRepository, baseUserService, baseConfigurationService);
        this.taskRepository = taskRepository;
        this.mailService = mailService;
    }

    @Override
    public CustomActivity beforeInsert(@NotNull CustomActivity newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        newEntity.setSubject(newEntity.getCustomtask().getType().getLabel());
        newEntity.setSubjdesc(newEntity.getCustomtask().getSubjectdesc());

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
    public CustomActivity beforeUpdate(@NotNull CustomActivity oldEntity, @NotNull CustomActivity newEntity) throws Exception {
        newEntity.setSubject(newEntity.getCustomtask().getType().getLabel());
        newEntity.setSubjdesc(newEntity.getCustomtask().getSubjectdesc());
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
