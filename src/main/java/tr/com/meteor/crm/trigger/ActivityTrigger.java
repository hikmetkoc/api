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

        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        newEntity.setSubject(newEntity.getTask().getType().getLabel());
        newEntity.setSubjdesc(newEntity.getTask().getSubjectdesc());

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
        newEntity.setSubject(newEntity.getTask().getType().getLabel());
        newEntity.setSubjdesc(newEntity.getTask().getSubjectdesc());
        return newEntity;
    }
}
