package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Task;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.TaskRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.utils.attributevalues.TaskStatus;
import tr.com.meteor.crm.utils.attributevalues.TaskType;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component(TaskTrigger.QUALIFIER)
public class TaskTrigger extends Trigger<Task, UUID, TaskRepository> {
    final static String QUALIFIER = "TaskTrigger";

    private final CustomerRepository customerRepository;

    private final MailService mailService;

    private final MobileNotificationService mobileNotificationService;

    public TaskTrigger(CacheManager cacheManager, TaskRepository taskRepository, BaseUserService baseUserService,
                       BaseConfigurationService baseConfigurationService, CustomerRepository customerRepository,
                       MailService mailService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, Task.class, TaskTrigger.class, taskRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
        this.mailService = mailService;
        this.mobileNotificationService = mobileNotificationService;
    }

    @Override
    public Task beforeInsert(@NotNull Task newEntity) throws Exception {
        if(newEntity.getOwner() != null) {    //Yeni talepte atanan kısmı doldurulduysa sadece o birimin amiri atama yapabilsin.
            long amir = Long.parseLong(newEntity.getBirim().getIlgilibirim());
            if (!getCurrentUserId().equals(amir)) {
                throw new Exception("Sadece ilgili birimin amiri atama yapabilir! Talep oluştururken Yetkili alanını boş bırakınız!");
            }
            if(!newEntity.getOwner().getBirim().getId().equals(newEntity.getBirim().getId())) {
                throw new Exception("Bu personel atamak istediğiniz birimde değildir.");
            }
        }
        else {
            //Yeni talepte yetkili bölümü boş bırakıldıysa ilgili birimin amirine atama yapılsın.
            long amir = Long.parseLong(newEntity.getBirim().getIlgilibirim());
            newEntity.setOwner(baseUserService.getUserFullFetched(amir).get());
        }
        //Birime Göre Konu Kontrolü
        if(!newEntity.getTaskType().getIlgilibirim().equals(newEntity.getBirim().getId())) {
            throw new Exception("Lütfen Birime göre Konu seçiniz!");
        }

        //Diğer Kontroller
        if (newEntity.getAssigner() == null) {
            newEntity.setAssigner(getCurrentUser());
        }
        if (!newEntity.getStatus().getId().equals(TaskStatus.YENI.getId())) {
            throw new Exception("Talep oluştururken durumu YENİ olmak zorundadır.");
        }
        if (newEntity.getDueTime().compareTo(newEntity.getCreatedDate())<0) {
            throw new Exception("Yapılacak Zaman Geçmiş Tarihe Atanamaz!.");
        }
        mailService.sendEmail(newEntity.getOwner().getEposta(),
            "MeteorPanel - Yeni Talep",newEntity.getAssigner().getFullName() + ", " +
            newEntity.getTaskType().getLabel() + " konulu yeni bir talepte bulundu.",false,false);
        return newEntity;
    }

    @Override
    public Task beforeUpdate(Task oldEntity, Task newEntity) throws Exception {
        long amir = Long.parseLong(newEntity.getBirim().getIlgilibirim());
        if (newEntity.getOwner() != null){
            if (!getCurrentUserId().equals(amir)) {
                throw new Exception("İlgili birimin amiri dışında kimsenin düzenleme yetkisi yoktur!");
            }
            if(!newEntity.getOwner().getBirim().getId().equals(newEntity.getBirim().getId())) {
                throw new Exception("Başka birimden birine görev atayamazsınız!");
            }
        }
        else {
            //Talep güncellerken yetkili bölümü boş bırakıldıysa ilgili birimin amirine atama yapılsın.
            if (!getCurrentUserId().equals(amir) && oldEntity.getOwner() != null) {
                throw new Exception("Yetkili düzenlemesi yapamazsınız!");
            } else {
                newEntity.setOwner(baseUserService.getUserFullFetched(amir).get());
            }
        }
        //Birime Göre Konu Kontrolü
        if(!newEntity.getTaskType().getIlgilibirim().equals(newEntity.getBirim().getId())) {
            throw new Exception("Lütfen Birime göre Konu seçiniz!");
        }
        //Durum Tamamlandıya çevrilirse Tamamlanma Tarihini Şuan olarak gir.
        if(newEntity.getStatus().getId().equals(TaskStatus.TAMAMLANDI.getId())) {
            newEntity.setOktime(Instant.now());
            if (newEntity.getComplateDate() == null) {
                throw new Exception("Talep Tamamlanma Tarihi girilmek zorundadır!");
            }
            mailService.sendEmail(newEntity.getAssigner().getEposta(),"MeteorPanel - Tamamlanan Talep",
                newEntity.getAssigner().getFullName() + ", " +
                newEntity.getTaskType().getLabel() + " konulu talebiniz TAMAMLANDI.",false,false);
        }
        if (!newEntity.getStatus().getId().equals(TaskStatus.TAMAMLANDI.getId()) && newEntity.getComplateDate() != null) {
            throw new Exception("Talebin durumu Tamamlandı değilse Talep Tamamlanma Tarihi giremezsiniz!");
        }

        //Talep Reddedilirse Mail At.
        if(newEntity.getStatus().getId().equals(TaskStatus.YAPILAMADI.getId())) {
            mailService.sendEmail(newEntity.getAssigner().getEposta(),"MeteorPanel - Reddedilen Talep",newEntity.getAssigner().getFullName() + ", " +
                newEntity.getTaskType().getLabel() + " konulu talebiniz REDDEDİLDİ.",false,false);
        }
        //Talep başka bir kullanıcıya atandıysa Mail At.
        if(newEntity.getStatus().getId().equals(TaskStatus.YENI.getId()) && !newEntity.getOwner().getId().equals(newEntity.getAssigner().getId())) {
            mailService.sendEmail(newEntity.getOwner().getEposta(),
                "MeteorPanel - Atanan Talep", newEntity.getAssigner().getFullName() + " tarafından " +
                    newEntity.getCreatedDate().toString() +
                    " tarihinde oluşturulan " +
                    newEntity.getTaskType().getLabel() + " konulu talep birim amiriniz tarafından size atandı.", false, false);
        }
        return newEntity;
    }

    @Override
    public Task afterInsert(Task newEntity) throws Exception {
        mobileNotificationService.sendTaskNotification(newEntity);

        return newEntity;
    }
}
