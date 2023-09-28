package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.FuelLimit;
import tr.com.meteor.crm.domain.BuyLimit;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.utils.attributevalues.CostPlace;
import tr.com.meteor.crm.utils.attributevalues.FuelStatus;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component(FuelLimitTrigger.QUALIFIER)
public class FuelLimitTrigger extends Trigger<FuelLimit, UUID, FuelLimitRepository> {
    final static String QUALIFIER = "FuelLimitTrigger";

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    private final BuyLimitRepository limitRepository;

    private final MailService mailService;

    private final MobileNotificationService mobileNotificationService;

    public FuelLimitTrigger(CacheManager cacheManager, FuelLimitRepository fuellimitRepository, BaseUserService baseUserService,
                            BaseConfigurationService baseConfigurationService, CustomerRepository customerRepository,
                            StoreRepository storeRepository,  BuyLimitRepository limitRepository, MailService mailService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, FuelLimit.class, FuelLimitTrigger.class, fuellimitRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.limitRepository = limitRepository;
        this.mailService = mailService;
        this.mobileNotificationService = mobileNotificationService;
    }

    @Override
    public FuelLimit beforeInsert(@NotNull FuelLimit newEntity) throws Exception {
        //1. VE 2.ONAYCIYI PARA BİRİMİ VE LİMİTE GÖRE AYARLA
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        if(newEntity.getAssigner() == null) {
            List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getOwner().getId(), CostPlace.OTOBIL.getAttributeValue());
            for (BuyLimit limits : limit) {
                if (newEntity.getFuelTl().compareTo(limits.getUserTl()) < 0) {
                    newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                } else if (newEntity.getFuelTl().compareTo(limits.getUserTl()) > 0 && newEntity.getFuelTl().compareTo(limits.getChiefTl()) < 0) {
                    newEntity.setAssigner(limits.getChief());
                } else if (newEntity.getFuelTl().compareTo(limits.getChiefTl()) > 0 && newEntity.getFuelTl().compareTo(limits.getManagerTl()) < 0) {
                    newEntity.setAssigner(limits.getManager());
                } else if (newEntity.getFuelTl().compareTo(limits.getManagerTl()) > 0) {
                    newEntity.setAssigner(limits.getDirector());
                }
            }
        }
        if(!newEntity.getStatus().getId().equals(FuelStatus.BEKLIYOR.getId())){
            throw new Exception("Yeni teklifler Onay Bekliyor aşamasında olmalıdır!");
        }

        if (newEntity.getStartDate() == null) {
            throw new Exception("Başlangıç Tarihi boş bırakılmamalıdır!");
        }
        if (newEntity.getEndDate() == null) {
            throw new Exception("Bitiş Tarihi boş bırakılmamalıdır!");
        }
        newEntity.setIslem(false);
        /*mailService.sendEmail(newEntity.getAssigner().getEposta(),
            "MeteorPanel - Ek Limit Talebi Hk.",newEntity.getOwner().getFullName() + ", " +
                newEntity.getCurcode() + " cari kodlu müşterisine " + newEntity.getStartDate().toString() + "  -  " + newEntity.getEndDate() + " tarihleri için " +
                newEntity.getFuelTl().toString() + " TL lik ek limit talep ediyor.",
            false,false);*/
        return newEntity;
    }

    @Override
    public FuelLimit beforeUpdate(FuelLimit oldEntity, FuelLimit newEntity) throws Exception {
        if (newEntity.getIslem().equals(true)) {
            throw new Exception("Onaylanan veya Reddedilen talepte değişiklik yapılamaz!");
        }
        if (newEntity.getAssigner().getId().equals(getCurrentUserId())) { // DÜZENLEME YAPAN ONAYCIYSA
            if (newEntity.getStatus().getId().equals(FuelStatus.BEKLIYOR.getId())) {
                // ONAY DURUMUNU ONAY BEKLİYOR OLARAK KAYDEDERSE ONAYCILAR GÜNCELLENSİN
                List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getOwner().getId(), CostPlace.OTOBIL.getAttributeValue());
                for (BuyLimit limits : limit) {
                    if (newEntity.getFuelTl().compareTo(limits.getUserTl()) < 0) {
                        newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                    } else if (newEntity.getFuelTl().compareTo(limits.getUserTl()) > 0 && newEntity.getFuelTl().compareTo(limits.getChiefTl()) < 0) {
                        newEntity.setAssigner(limits.getChief());
                    } else if (newEntity.getFuelTl().compareTo(limits.getChiefTl()) > 0 && newEntity.getFuelTl().compareTo(limits.getManagerTl()) < 0) {
                        newEntity.setAssigner(limits.getManager());
                    } else if (newEntity.getFuelTl().compareTo(limits.getManagerTl()) > 0) {
                        newEntity.setAssigner(limits.getDirector());
                    }
                }
            }
            if (newEntity.getStatus().getId().equals(FuelStatus.ONAY.getId())) {   // ONAYLANDIYSA
                List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getAssigner().getId(), CostPlace.OTOBIL.getAttributeValue());
                for (BuyLimit limits : limit) {
                    if (newEntity.getFuelTl().compareTo(limits.getUserTl()) > 0) {
                        throw new Exception("Bu limit onaylayabileceğinizin dışındadır!");
                    }
                }
                newEntity.setIslem(true);
                newEntity.setOkeyFirst(Instant.now());
                /*mailService.sendEmail(newEntity.getOwner().getEposta(),
                            "MeteorPanel - Ek Limit Talebi Hk.",newEntity.getOwner().getFullName() + ", " +
                                newEntity.getCurcode() + " cari kodlu müşteriniz için " + newEntity.getStartDate().toString() + "  -  " + newEntity.getEndDate() + " tarihleri için " +
                                " yapmış olduğunuz " + newEntity.getFuelTl().toString() + " TL lik ek limit talebiniz ONAYLANMIŞTIR!",
                            false,false);*/
            }
            if (newEntity.getStatus().getId().equals(FuelStatus.RED.getId())) {   // REDDEDİLDİYSE
                List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getAssigner().getId(), CostPlace.OTOBIL.getAttributeValue());
                for (BuyLimit limits : limit) {
                    if (newEntity.getFuelTl().compareTo(limits.getUserTl()) > 0) {
                        throw new Exception("Bu limit reddedebileceğinizin dışındadır!");
                    }
                }
                newEntity.setIslem(true);
                /*mailService.sendEmail(newEntity.getOwner().getEposta(),
                    "MeteorPanel - Ek Limit Talebi Hk.",newEntity.getOwner().getFullName() + ", " +
                        newEntity.getCurcode() + " cari kodlu müşteriniz için " + newEntity.getStartDate().toString() + "  -  " + newEntity.getEndDate() + " tarihleri için " +
                        " yapmış olduğunuz " + newEntity.getFuelTl().toString() + " TL lik ek limit talebiniz REDDEDİLMİŞTİR!",
                    false,false);*/
            }
        } else { // DÜZENLEME YAPAN 1.ONAYCI DEĞİLSE
            if (newEntity.getStatus().getId().equals(FuelStatus.BEKLIYOR.getId())) {
                // ONAY DURUMUNU ONAY BEKLİYOR OLARAK KAYDEDERSE ONAYCILAR GÜNCELLENSİN
                List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getOwner().getId(), CostPlace.OTOBIL.getAttributeValue());
                for (BuyLimit limits : limit) {
                    if (newEntity.getFuelTl().compareTo(limits.getUserTl()) < 0) {
                        newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                    } else if (newEntity.getFuelTl().compareTo(limits.getUserTl()) > 0 && newEntity.getFuelTl().compareTo(limits.getChiefTl()) < 0) {
                        newEntity.setAssigner(limits.getChief());
                    } else if (newEntity.getFuelTl().compareTo(limits.getChiefTl()) > 0 && newEntity.getFuelTl().compareTo(limits.getManagerTl()) < 0) {
                        newEntity.setAssigner(limits.getManager());
                    } else if (newEntity.getFuelTl().compareTo(limits.getManagerTl()) > 0) {
                        newEntity.setAssigner(limits.getDirector());
                    }
                }
            }
            if (newEntity.getStatus().getId().equals(FuelStatus.ONAY.getId()) || newEntity.getStatus().getId().equals(FuelStatus.RED.getId())) {   // ONAYLANDIYSA
                throw new Exception("Onaycı siz değilsiniz!");
            }

        }
        return newEntity;
    }
}
