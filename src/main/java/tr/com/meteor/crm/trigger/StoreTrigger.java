package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.utils.attributevalues.BuyStatus;
import tr.com.meteor.crm.utils.attributevalues.ContractStatus;
import tr.com.meteor.crm.utils.attributevalues.TaskType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Component(StoreTrigger.QUALIFIER)
public class StoreTrigger extends Trigger<Store, UUID, StoreRepository> {
    final static String QUALIFIER = "StoreTrigger";
    private final BuyRepository buyRepository;

    private final BuyLimitRepository buyLimitRepository;

    private final MailService mailService;

    private final MobileNotificationService mobileNotificationService;

    public StoreTrigger(CacheManager cacheManager, StoreRepository storeRepository, BaseUserService baseUserService,
                        BaseConfigurationService baseConfigurationService,
                        BuyRepository buyRepository, BuyLimitRepository buyLimitRepository, MailService mailService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, Store.class, StoreTrigger.class, storeRepository, baseUserService, baseConfigurationService);
        this.buyRepository = buyRepository;
        this.buyLimitRepository = buyLimitRepository;
        this.mailService = mailService;
        this.mobileNotificationService = mobileNotificationService;
    }

    @Override
    public Store beforeInsert(@NotNull Store newEntity) throws Exception {
        if (newEntity.getAssigner() == null) {
            newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        }
        if (newEntity.getOwner() == null) {
            if (newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_IK.getId()) ||
                newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Muh.getId()) ||
                (newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Imim.getId()) &&
                    !newEntity.getAssigner().getId().equals(baseUserService.getUserFullFetched(90L).get().getId())) ||
                newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Kasa.getId()) ||
                newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_IcDenetim.getId()) ||
                newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Risk.getId()) ||
                newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Paz.getId()) ||
                newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Web.getId()) ||
                newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_IT.getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(90L).get());     //CEMAL ELİTAŞ
            } else if (newEntity.getAssigner().getId().equals(baseUserService.getUserFullFetched(103L).get().getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(102L).get());    //MUSTAFA KARAMAN -> ZAFER AYDIN
            } else if (newEntity.getAssigner().getId().equals(baseUserService.getUserFullFetched(133L).get().getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(102L).get());    //MUSTAFA ÖCAL -> ZAFER AYDIN
            } else if (newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Loher.getId()) &&
                !newEntity.getAssigner().getId().equals(baseUserService.getUserFullFetched(103L).get().getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(103L).get());    //MUSTAFA KARAMAN
            } else if (newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Avelice.getId()) &&
                !newEntity.getAssigner().getId().equals(baseUserService.getUserFullFetched(133L).get().getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(133L).get());    //MUSTAFA ÖCAL
            } else if ((newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Satin.getId()) &&
                !newEntity.getAssigner().getId().equals(baseUserService.getUserFullFetched(91L).get().getId())) ||
                newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Kafe.getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(91L).get());    //MURAT GEDİK
            } else if (newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Ins.getId()) &&
                !newEntity.getAssigner().getId().equals(baseUserService.getUserFullFetched(94L).get().getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(94L).get());    //MURAT CAN
            } else if (newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Fin.getId()) &&
                !newEntity.getAssigner().getId().equals(baseUserService.getUserFullFetched(35L).get().getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(35L).get());    //FUNDA BUGÜN
            } else if (newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Satis.getId()) &&
                !newEntity.getAssigner().getId().equals(baseUserService.getUserFullFetched(93L).get().getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(93L).get());    //YILMAZ KARAMAN
            } else if (newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Gnl.getId()) ||
                newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Yonetim.getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(101L).get());    //ÖZGÜR CEM CAN
            } else if (newEntity.getAssigner().getBirim().getId().equals(TaskType.TaskBirim.BIRIM_SanayiSatis.getId())) {
                newEntity.setOwner(baseUserService.getUserFullFetched(103L).get());    //MUSTAFA KARAMAN
            } else {
                newEntity.setOwner(baseUserService.getUserFullFetched(99L).get());      //ÜMİT YAŞAR
            }
            if (newEntity.getOwner() == null) {
                throw new Exception("Onaycı atanamadı");
            }
        }

        if (newEntity.getBuyowner() != null) {
            throw new Exception("Satın Alma Sorumlusu boş girilmelidir!");
        }
        if (newEntity.getStatus().getId().equals(ContractStatus.AKTIF.getId()) && !newEntity.getOwner().getId().equals(getCurrentUserId())) {
            throw new Exception("Yeni talepler Onay Bekliyor aşamasında olmalıdır!");
        }
        if (newEntity.getStatus().getId().equals(ContractStatus.RED.getId()) && !newEntity.getOwner().getId().equals(getCurrentUserId())) {
            throw new Exception("Yeni talepler Onay Bekliyor aşamasında olmalıdır!");
        }
        if (newEntity.getStatus().getId().equals(ContractStatus.IPTAL.getId()) && !newEntity.getOwner().getId().equals(getCurrentUserId())) {
            throw new Exception("Yeni talepler Onay Bekliyor aşamasında olmalıdır!");
        }
        if (newEntity.getStatus().getId().equals(ContractStatus.MERKEZDEN.getId()) && !newEntity.getOwner().getId().equals(getCurrentUserId())) {
            throw new Exception("Yeni talepler Onay Bekliyor aşamasında olmalıdır!");
        }
        if (newEntity.getEndDate() != null && newEntity.getEndDate().compareTo(newEntity.getCreatedDate()) < 0) {
            throw new Exception("Son Tarih geçmiş Tarihe Atanamaz!.");
        }
        newEntity.setIslem(false);

        Integer sayac = 1;
        List<Store> str = repository.findAll();
        for (Store str1 : str) {
            if (str1.getStcode() != null) {
                sayac++;
            }
        }
        newEntity.setStcode("TALEP-" + sayac);
        return newEntity;
    }

    @Override
    public Store afterInsert(Store newEntity) throws Exception {
        LocalDateTime olusturmatarihi = LocalDateTime.ofInstant(newEntity.getCreatedDate(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String olt = olusturmatarihi.format(formatter);
        if (newEntity.getEndDate() != null) {
            LocalDateTime sontarih = LocalDateTime.ofInstant(newEntity.getEndDate(), ZoneId.systemDefault());
            String sta = sontarih.format(formatter);
            mailService.sendEmail(newEntity.getOwner().getEposta(),
                "MeteorPanel - Yeni Satın Alma Talebi", newEntity.getAssigner().getFullName() + ", " +
                    newEntity.getSirket().getLabel() + " adına " + newEntity.getMaliyet().getLabel() + " maliyet yeri için " +
                    newEntity.getRequest() + " talebinde bulundu.\nGerekçesi : " + newEntity.getDescription() +
                    "\nİlgili talep " + sta + " tarihine kadar talep edilmektedir." +
                    "\nEğer uygunsa talebi onaylamanız ve SATIN ALMA SORUMLUSU seçmeniz gerekiyor.",
                false, false);

        } else {
            mailService.sendEmail(newEntity.getOwner().getEposta(),
                "MeteorPanel - Yeni Satın Alma Talebi", newEntity.getAssigner().getFullName() + ", " +
                    newEntity.getSirket().getLabel() + " adına " + newEntity.getMaliyet().getLabel() + " maliyet yeri için " +
                    newEntity.getRequest() + " talebinde bulundu.\nGerekçesi : " + newEntity.getDescription() +
                    "\nİlgili talep için bir Son Talep tarihi girilmemiştir." +
                    "\nEğer uygunsa talebi onaylamanız ve SATIN ALMA SORUMLUSU seçmeniz gerekiyor.",
                false, false);
        }
        return newEntity;
    }

    @Override
    public Store beforeUpdate(Store oldEntity, Store newEntity) throws Exception {
        if (getCurrentUser().getId().equals(newEntity.getBuyowner().getId())) {
            newEntity.setBuyowner(null);
            mailService.sendEmail(newEntity.getOwner().getEposta(),
                "MeteorPanel - Satın Alma Talebi", "Sayın " + newEntity.getOwner().getFullName() + ",\n" +
                    newEntity.getMaliyet().getLabel() + " adına " +
                    newEntity.getRequest() + " ürünü için seçtiğiniz SATIN ALMA SORUMLUSU, bu ürün için doğru Satın Alma/Tedarik sorumlusu olmadığından," +
                    " doğru satın alma sorumlusunu belirleyerek sürece devam edilmesini rica ederiz.. \nİlgili talebe yeni bir SATIN ALMA SORUMLUSU atamanız gerekmektedir.",
                false, false);
        } else {
            if (newEntity.getOwner() == null) {
                newEntity.setOwner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
            }
            if (!newEntity.getBuyKey()) {
                if (newEntity.getBuyowner() == null && newEntity.getStatus().getId().equals(ContractStatus.AKTIF.getId())) {
                    throw new Exception("Satın Alma Sorumlusu seçmek zorundasınız!");
                }
                if (newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getBuyowner() != null) {
                    if (!newEntity.getBuyowner().getId().equals(baseUserService.getUserFullFetched(80L).get().getId()) &&
                        !newEntity.getBuyowner().getId().equals(baseUserService.getUserFullFetched(32L).get().getId()) &&
                        !newEntity.getBuyowner().getId().equals(baseUserService.getUserFullFetched(131L).get().getId()) &&
                        !newEntity.getBuyowner().getId().equals(baseUserService.getUserFullFetched(119L).get().getId()) &&
                        !newEntity.getBuyowner().getId().equals(baseUserService.getUserFullFetched(130L).get().getId()) &&
                        !newEntity.getBuyowner().getId().equals(baseUserService.getUserFullFetched(18L).get().getId()) &&
                        !newEntity.getBuyowner().getId().equals(baseUserService.getUserFullFetched(8L).get().getId())) {
                        throw new Exception("Satın Alma Sorumlusu sadece aşağıdaki personeller olabilir : \n" +
                            "Mete ÖCAL, \n" + // Cemal Elitaş , Ümit Yaşar, Özgür Cem CAN
                            "Mine HATİPOĞLU, \n" + // Cemal Elitaş , Ümit Yaşar, Özgür Cem CAN
                            "Kaan OKTAY, \n" + // Mustafa Karaman , Zafer Aydın, Özgür Cem CAN
                            "Samet DEMİRHAN, \n" + // Mustafa ÖCAL, Zafer Aydın, Özgür Cem CAN
                            "Gönül ÖZBAKIR, \n" + // Ünal Toplu, Mustafa Karaman, Zafer Aydın
                            "Öykü Su ÖZARSLAN, \n" + // Murat Can, Özgür Cem CAN
                            "Musa KARA\n" // Murat Gedik, Ümit Yaşar, Özgür Cem CAN
                        );
                    }
                }
                Boolean maliyetsorgulama = false;
                List<BuyLimit> buyLimits = buyLimitRepository.findByUserId(newEntity.getBuyowner().getId());
                for (BuyLimit buyLimit : buyLimits) {
                    if (buyLimit.getMaliyet().getId().equals(newEntity.getMaliyet().getId())) {
                        maliyetsorgulama = true;
                    }
                }
                if (newEntity.getOwner().getId().equals(getCurrentUserId()) && !maliyetsorgulama) {
                    throw new Exception("Seçtiğiniz satın alma sorumlusu ise Maliyet Yeri uyuşmamaktadır.");
                }
                if (!newEntity.getOwner().getId().equals(getCurrentUserId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId()) && !newEntity.getBuyowner().getId().equals(getCurrentUserId())) {
                    throw new Exception("Sadece görüntüleme yetkiniz var, onaycı ve talep giren hariç kimse işlem yapamaz!");
                }
                if (newEntity.getStatus().getId().equals(ContractStatus.AKTIF.getId()) && !newEntity.getOwner().getId().equals(getCurrentUserId())) {
                    throw new Exception("Bu talebe sadece Onaycınız onay verebilir!");
                }
                if (newEntity.getStatus().getId().equals(ContractStatus.RED.getId()) && !newEntity.getOwner().getId().equals(getCurrentUserId())) {
                    throw new Exception("Bu talebi sadece onaycınız Reddedebilir, talebinizden vazgeçmek için durumu İptal olarak güncelleyebilirsiniz...");
                }
                if (newEntity.getStatus().getId().equals(ContractStatus.MERKEZDEN.getId()) && !newEntity.getBuyowner().getId().equals(getCurrentUserId())) {
                    throw new Exception("Merkez durumunu sadece satın alma sorumlusu seçebilir!");
                }
                if (newEntity.getStatus().getId().equals(ContractStatus.MERKEZDEN.getId())) {
                    newEntity.setBuyStatus(BuyStatus.ONAY.getAttributeValue());
                }

                if ((newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getIslem().equals(true)) || (newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getStatus().getId().equals(ContractStatus.PASIF.getId()))) {
                    throw new Exception("Onay verdiğiniz veya reddettiğiniz işlemi güncelleyemezsiniz, talebi oluşturan, durumu Onay Bekliyor olarak güncellemeli.");
                }

                if (newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getStatus().getId().equals(ContractStatus.IPTAL.getId())) {
                    throw new Exception("Bir talebi sadece oluşturan iptal edebilir!");
                }

                if (!newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getBuyowner() != null && newEntity.getStatus().getId().equals(ContractStatus.PASIF.getId())) {
                    throw new Exception("Satın alma sorumlusunu sadece onaycınız atayabilir!");
                }

                if (newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getIslem().equals(false)) {
                    newEntity.setIslem(true);
                }
                if (newEntity.getAssigner().getId().equals(getCurrentUserId()) && newEntity.getStatus().getId().equals(ContractStatus.PASIF.getId())) {
                    newEntity.setIslem(false);
                }
                if (!newEntity.getBuyowner().getId().equals(getCurrentUserId()) && newEntity.getAssigner().getId().equals(getCurrentUserId()) && newEntity.getBuyStatus().getId().equals(BuyStatus.ONAY.getId()) || newEntity.getBuyStatus().getId().equals(BuyStatus.RED.getId())) {
                    throw new Exception("Teklifi onaylanan veya reddedilen talebin durumu değiştirilemez!");
                }
                if (newEntity.getAssigner().getId().equals(getCurrentUserId()) && newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getBuyowner() != null) {
                    throw new Exception("Onaylanan talepte değişiklik yapamnazsınız!");
                }
            }
        }
        return newEntity;
    }

    @Override
    public Store afterUpdate(Store oldEntity, Store newEntity) throws Exception {
        if (newEntity.getStatus().getId().equals(ContractStatus.AKTIF.getId())) {
            mailService.sendEmail(newEntity.getAssigner().getEposta(),
                "MeteorPanel - Satın Alma Talebi", newEntity.getOwner().getFullName() + ", " +
                    newEntity.getMaliyet().getLabel() + " adına " +
                    newEntity.getRequest() + " ürünü için yapmış olduğunuz talebi onayladı." +
                    "\nİlgili talebe bir SATIN ALMA SORUMLUSU atanmıştır.\nSATIN ALMA SORUMLUSU, taleple ilgili teklifleri ekledikten sonra " +
                    "onaycıların onayı ile birlikte satın alma süreci tamamlanacaktır.",
                false, false);
            mailService.sendEmail(newEntity.getBuyowner().getEposta(),
                "MeteorPanel - Satın Alma Talebi", newEntity.getAssigner().getFullName() + ", " +
                    newEntity.getMaliyet().getLabel() + " adına " +
                    newEntity.getRequest() + " ürünü için bir talepte bulundu ve talep " + newEntity.getOwner().getFullName() + " tarafından onayladı." +
                    "\nİlgili talebin SATIN ALMA SORUMLUSU sizsiniz. En az 3 teklif oluşturup proforma faturaları ekledikten sonra her bir teklif için Teklifi Tamamla butonuna basın.\n" +
                    "Eğer isterseniz önerdiğiniz teklif için Öner butonuna da basabilirsiniz. Onaycının onayı ile birlikte satın alma süreci tamamlanacaktır.\n" +
                    "Onaylanan teklifteki ürünü satın alıp talep eden kişiye ulaştırabilirsiniz.",
                false, false);
        }
        if (newEntity.getStatus().getId().equals(ContractStatus.RED.getId())) {
            mailService.sendEmail(newEntity.getAssigner().getEposta(),
                "MeteorPanel - Satın Alma Talebi", newEntity.getOwner().getFullName() + ", " +
                    newEntity.getMaliyet().getLabel() + " adına " +
                    newEntity.getRequest() + " ürünü için yapmış olduğunuz talebi reddetti.",
                false, false);
        }
        return newEntity;
    }
}
