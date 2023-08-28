package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.CustomTask;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.repository.CustomTaskRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.utils.attributevalues.CustomTaskStatus;
import tr.com.meteor.crm.utils.attributevalues.CustomTaskType;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(CustomTaskTrigger.QUALIFIER)
public class CustomTaskTrigger extends Trigger<CustomTask, UUID, CustomTaskRepository> {
    final static String QUALIFIER = "CustomTaskTrigger";

    private final CustomerRepository customerRepository;

    private final MailService mailService;

    private final MobileNotificationService mobileNotificationService;

    public CustomTaskTrigger(CacheManager cacheManager, CustomTaskRepository customTaskRepository, BaseUserService baseUserService,
                             BaseConfigurationService baseConfigurationService, CustomerRepository customerRepository,
                             MailService mailService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, CustomTask.class, CustomTaskTrigger.class, customTaskRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
        this.mailService = mailService;
        this.mobileNotificationService = mobileNotificationService;
    }

    @Override
    public CustomTask beforeInsert(@NotNull CustomTask newEntity) throws Exception {
        if(newEntity.getOwner()==null) {
            newEntity.setOwner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        }
        if (newEntity.getType() == null) {
            throw new Exception("Konu seçimi zorunludur.");
        }
        if (!newEntity.getStatus().getId().equals(CustomTaskStatus.YENI.getId())) {
            throw new Exception("Talep oluştururken durumu YENİ olmak zorundadır.");
        }
        if (newEntity.getDueTime() == null) {
            throw new Exception("Yapılacak zaman bilgisi girilmelidir.");
        }
        if (newEntity.getDueTime().compareTo(newEntity.getCreatedDate())<0) {
            throw new Exception("Yapılacak Zaman Geçmiş Tarihe Atanamaz!.");
        }
        if (newEntity.getCustomer() == null) {
            Optional<Customer> customer = customerRepository.findById(newEntity.getCustomer().getId());
            if (customer.isPresent()) {
                newEntity.setCustomer(customer.get());
            }
        }

       /*mailService.sendEmail(newEntity.getOwner().getEmail(),
            "MeteorPanel - Yeni Talep",newEntity.getAssigner().getFullName() + ", " +
            newEntity.getType().getLabel() + " konulu yeni bir talepte bulundu.",false,false);
*/
        return newEntity;
    }

    @Override
    public CustomTask beforeUpdate(CustomTask oldEntity, CustomTask newEntity) throws Exception {
        if(newEntity.getOwner()==null) {
            newEntity.setOwner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        }
        //Durum Tamamlandıya çevrilirse Tamamlanma Tarihini Şuan olarak gir.
        /*if(newEntity.getStatus().getId().equals(CustomTaskStatus.TAMAMLANDI.getId())) {
            newEntity.setOktime(Instant.now());
            mailService.sendEmail(newEntity.getAssigner().getEmail(),"MeteorPanel - Tamamlanan Talep",
                newEntity.getAssigner().getFullName() + ", " +
                newEntity.getType().getLabel() + " konulu talebiniz TAMAMLANDI.",false,false);
        }

        //Talep Reddedilirse Mail At.
        if(newEntity.getStatus().getId().equals(CustomTaskStatus.YAPILAMADI.getId())) {
            mailService.sendEmail(newEntity.getAssigner().getEmail(),"MeteorPanel - Reddedilen Talep",newEntity.getAssigner().getFullName() + ", " +
                newEntity.getType().getLabel() + " konulu talebiniz REDDEDİLDİ.",false,false);
        }
        //Talep başka bir kullanıcıya atandıysa Mail At.
        if(newEntity.getStatus().getId().equals(CustomTaskStatus.YENI.getId()) && !newEntity.getOwner().getId().equals(newEntity.getAssigner().getId())) {
            mailService.sendEmail(newEntity.getOwner().getEmail(),
                "MeteorPanel - Atanan Talep", newEntity.getAssigner().getFullName() + " tarafından " +
                    newEntity.getCreatedDate().toString() +
                    " tarihinde oluşturulan " +
                    newEntity.getType().getLabel() + " konulu talep birim amiriniz tarafından size atandı.", false, false);
        }*/

        return newEntity;
    }

   /* @Override
    public CustomTask afterInsert(CustomTask newEntity) throws Exception {
        mobileNotificationService.sendTaskNotification(newEntity);

        return newEntity;
    }*/
}
