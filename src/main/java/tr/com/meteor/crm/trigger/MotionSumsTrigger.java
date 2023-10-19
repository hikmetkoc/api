package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.MotionSums;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.repository.MotionSumsRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.utils.attributevalues.MotionSumsStatus;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(MotionSumsTrigger.QUALIFIER)
public class MotionSumsTrigger extends Trigger<MotionSums, UUID, MotionSumsRepository> {
    final static String QUALIFIER = "MotionSumsTrigger";

    private final CustomerRepository customerRepository;

    private final MailService mailService;

    private final MobileNotificationService mobileNotificationService;

    public MotionSumsTrigger(CacheManager cacheManager, MotionSumsRepository MotionSumsRepository, BaseUserService baseUserService,
                             BaseConfigurationService baseConfigurationService, CustomerRepository customerRepository,
                             MailService mailService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, MotionSums.class, MotionSumsTrigger.class, MotionSumsRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
        this.mailService = mailService;
        this.mobileNotificationService = mobileNotificationService;
    }

    @Override
    public MotionSums beforeInsert(@NotNull MotionSums newEntity) throws Exception {
        if(newEntity.getOwner() == null) {
            newEntity.setOwner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        }
        /*if (newEntity.getType() == null) {
            throw new Exception("Konu seçimi zorunludur.");
        }
        if (!newEntity.getStatus().getId().equals(MotionSumsStatus.YENI.getId())) {
            throw new Exception("Talep oluştururken durumu YENİ olmak zorundadır.");
        }*/
        /*if (newEntity.getDueTime() == null) {
            throw new Exception("Yapılacak zaman bilgisi girilmelidir.");
        }
        if (newEntity.getDueTime().compareTo(newEntity.getCreatedDate())<0) {
            throw new Exception("Yapılacak Zaman Geçmiş Tarihe Atanamaz!.");
        }*/
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
    public MotionSums beforeUpdate(MotionSums oldEntity, MotionSums newEntity) throws Exception {
        if(newEntity.getOwner()==null) {
            newEntity.setOwner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        }
        //Durum Tamamlandıya çevrilirse Tamamlanma Tarihini Şuan olarak gir.
        /*if(newEntity.getStatus().getId().equals(MotionSumsStatus.TAMAMLANDI.getId())) {
            newEntity.setOktime(Instant.now());
            mailService.sendEmail(newEntity.getAssigner().getEmail(),"MeteorPanel - Tamamlanan Talep",
                newEntity.getAssigner().getFullName() + ", " +
                newEntity.getType().getLabel() + " konulu talebiniz TAMAMLANDI.",false,false);
        }

        //Talep Reddedilirse Mail At.
        if(newEntity.getStatus().getId().equals(MotionSumsStatus.YAPILAMADI.getId())) {
            mailService.sendEmail(newEntity.getAssigner().getEmail(),"MeteorPanel - Reddedilen Talep",newEntity.getAssigner().getFullName() + ", " +
                newEntity.getType().getLabel() + " konulu talebiniz REDDEDİLDİ.",false,false);
        }
        //Talep başka bir kullanıcıya atandıysa Mail At.
        if(newEntity.getStatus().getId().equals(MotionSumsStatus.YENI.getId()) && !newEntity.getOwner().getId().equals(newEntity.getAssigner().getId())) {
            mailService.sendEmail(newEntity.getOwner().getEmail(),
                "MeteorPanel - Atanan Talep", newEntity.getAssigner().getFullName() + " tarafından " +
                    newEntity.getCreatedDate().toString() +
                    " tarihinde oluşturulan " +
                    newEntity.getType().getLabel() + " konulu talep birim amiriniz tarafından size atandı.", false, false);
        }*/

        return newEntity;
    }

   /* @Override
    public MotionSums afterInsert(MotionSums newEntity) throws Exception {
        mobileNotificationService.sendMotionSumsNotification(newEntity);

        return newEntity;
    }*/
}
