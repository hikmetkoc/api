package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.Responsible;
import tr.com.meteor.crm.domain.Task;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.ResponsibleRepository;
import tr.com.meteor.crm.repository.TaskRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.configuration.Configurations;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component(ResponsibleTrigger.QUALIFIER)
public class ResponsibleTrigger extends Trigger<Responsible, UUID, ResponsibleRepository>{
    final static String QUALIFIER = "ResponsibleTrigger";
    private final TaskRepository taskRepository;

    private final MailService mailService;
    public ResponsibleTrigger(CacheManager cacheManager, ResponsibleRepository responsibleRepository,
                              BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                              TaskRepository taskRepository, MailService mailService) {
        super(cacheManager, Responsible.class, ResponsibleTrigger.class, responsibleRepository, baseUserService, baseConfigurationService);
        this.taskRepository = taskRepository;
        this.mailService = mailService;
    }

    @Override
    public Responsible beforeInsert(@NotNull Responsible newEntity) throws Exception {
        if (newEntity.getAssigner() == null) {
            newEntity.setAssigner(getCurrentUser());
        }
        String sirket = newEntity.getOwner().getSirket().getLabel();
        List<Responsible> responsibles = repository.findByCustomerId(newEntity.getCustomer().getId());
        for (Responsible responsible : responsibles) {
            if (sirket.equals(responsible.getOwner().getSirket().getLabel()) && responsible.getOncelik().getLabel().equals(newEntity.getOncelik().getLabel())) {
                throw new Exception("Aynı şirkette bulunan personellerin öncelikleri farklı olmalıdır.");
            }
        }
        return newEntity;
    }
    @Override
    public Responsible beforeUpdate(@NotNull Responsible oldEntity, @NotNull Responsible newEntity) throws Exception {
        String sirket = newEntity.getOwner().getSirket().getLabel(); // Sorumlunun Şirketi
        List<Responsible> responsibles = repository.findByCustomerId(newEntity.getCustomer().getId()); // Aynı tedarikçinin sorumluları
        for (Responsible responsible : responsibles) { // Eğer sorumlunun şirketi aynı tedarikçi sorumlularından birinin şirketi ile aynı ise ve o sorumlu ile düzenleme yapılan sorumlunun önceliği aynı ise hata ver.
            if (sirket.equals(responsible.getOwner().getSirket().getLabel()) && responsible.getOncelik().getLabel().equals(newEntity.getOncelik().getLabel()) && !responsible.getOwner().equals(newEntity.getOwner())) {
                throw new Exception("Aynı şirkette bulunan personellerin öncelikleri farklı olmalıdır.");
            }
        }
        return newEntity;
    }
}
