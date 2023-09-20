package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.utils.attributevalues.BuyQuoteStatus;
import tr.com.meteor.crm.utils.attributevalues.BuyStatus;
import tr.com.meteor.crm.utils.attributevalues.MoneyTypeStatus;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component(BuyTrigger.QUALIFIER)
public class BuyTrigger extends Trigger<Buy, UUID, BuyRepository> {
    final static String QUALIFIER = "BuyTrigger";

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    private final FileDescriptorRepository fileDescriptorRepository;

    private final BuyLimitRepository limitRepository;

    private final MailService mailService;

    private final MobileNotificationService mobileNotificationService;

    public BuyTrigger(CacheManager cacheManager, BuyRepository buyRepository, BaseUserService baseUserService,
                      BaseConfigurationService baseConfigurationService, CustomerRepository customerRepository,
                      StoreRepository storeRepository, FileDescriptorRepository fileDescriptorRepository, BuyLimitRepository limitRepository, MailService mailService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, Buy.class, BuyTrigger.class, buyRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.fileDescriptorRepository = fileDescriptorRepository;
        this.limitRepository = limitRepository;
        this.mailService = mailService;
        this.mobileNotificationService = mobileNotificationService;
    }

    @Override
    public Buy beforeInsert(@NotNull Buy newEntity) throws Exception {
        /*List<Buy> buysay = repository.findByStoreId(newEntity.getStore().getId());
        Integer teklifsay = 0;
        for (Buy buy2 : buysay) {
            teklifsay++;
        }
        if (teklifsay >=5) {
            throw new Exception("5'den fazla teklif giremezsiniz!");
        }*/
        if(newEntity.getOwner() == null) {
            newEntity.setOwner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        }
        if (newEntity.getCustomer() == null) {
            Optional<Customer> customer = customerRepository.findById(newEntity.getCustomer().getId());
            if (customer.isPresent()) {
                newEntity.setCustomer(customer.get());
            }
        }

        List<Buy> buys = repository.findByStoreId(newEntity.getStore().getId());
        for (Buy buy : buys) {
            if (buy.getQuoteStatus().getId().equals(BuyStatus.ONAY.getId())) {
                throw new Exception("Onaylanan başka bir teklif var, yeni teklif giremezsiniz!");
            }
        }

        //1. VE 2.ONAYCIYI PARA BİRİMİ VE LİMİTE GÖRE AYARLA
        if(newEntity.getAssigner() == null) {
            //List<BuyLimit> limit = limitRepository.findByUserId(newEntity.getOwner().getId());
            List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getOwner().getId(), newEntity.getStore().getMaliyet());
            for (BuyLimit limits : limit) {
                if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId())) {
                    if (limits.getChiefTl().compareTo(newEntity.getFuelTl()) > 0) {
                        newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                        newEntity.setSecondAssigner(limits.getChief());
                    } else if (limits.getManagerTl().compareTo(newEntity.getFuelTl()) > 0) {
                        newEntity.setAssigner(limits.getChief());
                        newEntity.setSecondAssigner(limits.getManager());
                    } else if (limits.getDirectorTl().compareTo(newEntity.getFuelTl()) > 0) {
                        newEntity.setAssigner(limits.getManager());
                        newEntity.setSecondAssigner(limits.getDirector());
                    } else if (limits.getDirectorTl().compareTo(newEntity.getFuelTl()) < 0) {
                        newEntity.setAssigner(limits.getDirector());
                        newEntity.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                    }
                } else {
                    if (limits.getChiefDl().compareTo(newEntity.getFuelTl()) > 0) {
                        newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                        newEntity.setSecondAssigner(limits.getChief());
                    } else if (limits.getManagerDl().compareTo(newEntity.getFuelTl()) > 0) {
                        newEntity.setAssigner(limits.getChief());
                        newEntity.setSecondAssigner(limits.getManager());
                    } else if (limits.getDirectorDl().compareTo(newEntity.getFuelTl()) > 0) {
                        newEntity.setAssigner(limits.getManager());
                        newEntity.setSecondAssigner(limits.getDirector());
                    } else if (limits.getDirectorDl().compareTo(newEntity.getFuelTl()) < 0) {
                        newEntity.setAssigner(limits.getDirector());
                        newEntity.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                    }
                }
            }
        }
        if(!newEntity.getOwner().getId().equals(newEntity.getStore().getBuyowner().getId())){
            throw new Exception("Sadece SATIN ALMA SORUMLUSU teklif oluşturabilir!");
        }

        newEntity.setIslem(false);

        newEntity.setStcode(newEntity.getStore().getStcode());

        newEntity.setQuoteStatus(BuyStatus.BEKLE.getAttributeValue());
            mailService.sendEmail(newEntity.getSecondAssigner().getEposta(),
                "MeteorPanel - Satın Alma Talebi",newEntity.getStcode() + " kodlu talebe, " + newEntity.getOwner().getFullName() + ", " +
                    newEntity.getStore().getRequest() + " ürünü ile ilgili bir teklif girişi yapmıştır.\n" +
                    "3 teklif oluşturulup proforma faturaları eklendikten sonra, teklifler onayınıza açık hale gelecektir.",
                false,false);
        return newEntity;
    }

    @Override
    public Buy beforeUpdate(Buy oldEntity, Buy newEntity) throws Exception {
        if (newEntity.getQuoteStatus().getId().equals(BuyStatus.IPTAL.getId()) && !newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getOkeySecond() != null)  {
            throw new Exception("Teklifi sadece oluşturan kişi, Onay Bekliyor aşamasından İPTAL edebilir!");
        }
        else if (newEntity.getQuoteStatus().getId().equals(BuyStatus.BEKLE.getId())) {   // TEKLİF DURUMU : 1.ONAY BEKLENİYOR
            if (newEntity.getOwner().getId().equals(getCurrentUserId())) {  // DÜZENLEME YAPAN TEKLİFİ OLUŞTURAN KİŞİYSE
                if (newEntity.getSecondAssigner().getId().equals(getCurrentUserId())) { // DÜZENLEME YAPAN ONAYCIYSA
                    // ONAY DURUMUNU ONAY BEKLİYOR OLARAK KAYDEDERSE ONAYCILAR GÜNCELLENSİN
                    //List<BuyLimit> limit = limitRepository.findByUserId(newEntity.getOwner().getId());
                    List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getOwner().getId(), newEntity.getStore().getMaliyet());
                    for (BuyLimit limits : limit) {
                        if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId())) {
                            if (limits.getChiefTl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                                newEntity.setSecondAssigner(limits.getChief());
                            } else if (limits.getManagerTl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(limits.getChief());
                                newEntity.setSecondAssigner(limits.getManager());
                            } else if (limits.getDirectorTl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(limits.getManager());
                                newEntity.setSecondAssigner(limits.getDirector());
                            } else if (limits.getDirectorTl().compareTo(newEntity.getFuelTl()) < 0) {
                                newEntity.setAssigner(limits.getDirector());
                                newEntity.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                            }
                        } else {
                            if (limits.getChiefDl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                                newEntity.setSecondAssigner(limits.getChief());
                            } else if (limits.getManagerDl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(limits.getChief());
                                newEntity.setSecondAssigner(limits.getManager());
                            } else if (limits.getDirectorDl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(limits.getManager());
                                newEntity.setSecondAssigner(limits.getDirector());
                            } else if (limits.getDirectorDl().compareTo(newEntity.getFuelTl()) < 0) {
                                newEntity.setAssigner(limits.getDirector());
                                newEntity.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                            }
                        }
                    }
                } else { // DÜZENLEME YAPAN 1.ONAYCI DEĞİLSE
                    // ONAY DURUMUNU ONAY BEKLİYOR OLARAK KAYDEDERSE ONAYCILAR GÜNCELLENSİN
                    //List<BuyLimit> limit = limitRepository.findByUserId(newEntity.getOwner().getId());
                    List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getOwner().getId(), newEntity.getStore().getMaliyet());
                    for (BuyLimit limits : limit) {
                        if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId())) {
                            if (limits.getChiefTl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                                newEntity.setSecondAssigner(limits.getChief());
                            } else if (limits.getManagerTl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(limits.getChief());
                                newEntity.setSecondAssigner(limits.getManager());
                            } else if (limits.getDirectorTl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(limits.getManager());
                                newEntity.setSecondAssigner(limits.getDirector());
                            } else if (limits.getDirectorTl().compareTo(newEntity.getFuelTl()) < 0) {
                                newEntity.setAssigner(limits.getDirector());
                                newEntity.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                            }
                        } else {
                            if (limits.getChiefDl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                                newEntity.setSecondAssigner(limits.getChief());
                            } else if (limits.getManagerDl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(limits.getChief());
                                newEntity.setSecondAssigner(limits.getManager());
                            } else if (limits.getDirectorDl().compareTo(newEntity.getFuelTl()) > 0) {
                                newEntity.setAssigner(limits.getManager());
                                newEntity.setSecondAssigner(limits.getDirector());
                            } else if (limits.getDirectorDl().compareTo(newEntity.getFuelTl()) < 0) {
                                newEntity.setAssigner(limits.getDirector());
                                newEntity.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                            }
                        }
                    }
                }
                if (newEntity.getPreparation().getId().equals(BuyQuoteStatus.ONAY.getId())) {
                    List<FileDescriptor> fileDescriptors = fileDescriptorRepository.findAllByEntityId(newEntity.getId().toString());
                    if (fileDescriptors.isEmpty()) {
                        newEntity.getPreparation().setId(BuyQuoteStatus.BEKLE.getId());
                        throw new Exception("Tamamlamak istediğiniz teklife proforma fatura eklenmemiştir!");
                    }

                    Integer teklifsay = 0;
                    List<Buy> buys = repository.findByStoreId(newEntity.getStore().getId());
                    for (Buy buy : buys) {
                        teklifsay++;
                    }
                    if(teklifsay < 3) {
                        throw new Exception("En az 3 teklif oluşturmadan hiçbir teklifi tamamlayamazsınız!");
                    }
                }
            }
            if (!newEntity.getOwner().getId().equals(getCurrentUserId())) {
                throw new Exception("Bu teklifin Öneri Yetkilisi siz değilsiniz!");
            }
            if (newEntity.getSuggest().equals(true) && newEntity.getOwner().getId().equals(getCurrentUserId())) {
                newEntity.setOkeyFirst(Instant.now());
            }
            if (newEntity.getSuggest().equals(false) && newEntity.getOwner().getId().equals(getCurrentUserId())) {
                newEntity.setOkeyFirst(null);
            }
        }
        else if (newEntity.getQuoteStatus().getId().equals(BuyStatus.ONAY.getId()) && newEntity.getSecondAssigner().getId().equals(getCurrentUserId())) {   // TEKLİF DURUMU : ONAYLANDI
            if (newEntity.getSecondAssigner().getId().equals(getCurrentUserId())) { // DÜZENLEME YAPAN 2.ONAYCIYSA
                //List<BuyLimit> limit = limitRepository.findByUserId(newEntity.getSecondAssigner().getId());
                List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getSecondAssigner().getId(), newEntity.getStore().getMaliyet());
                for (BuyLimit limits : limit) {
                    if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId()) && newEntity.getFuelTl().compareTo(limits.getUserTl()) > 0) {
                        throw new Exception("Bu limit onaylayabileceğinizin dışındadır!");
                    }
                    if (!newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId()) && newEntity.getFuelTl().compareTo(limits.getUserDl()) > 0) {
                        throw new Exception("Bu limit onaylayabileceğinizin dışındadır!");
                    }
                }

                if (newEntity.getPreparation().getId().equals(BuyQuoteStatus.BEKLE.getId())) {
                    throw new Exception("Bu teklif henüz hazırlık aşamasındadır!");
                }
                List<Buy> buys = repository.findByStoreId(newEntity.getStore().getId());
                for (Buy buy : buys) {
                    if (!buy.getQuoteStatus().getId().equals(BuyStatus.ONAY.getId())) {
                        buy.setQuoteStatus(BuyStatus.RED.getAttributeValue());
                    }
                }
                repository.saveAll(buys);

                newEntity.setOkeySecond(Instant.now());
                Store store = newEntity.getStore();
                store.setBuyKey(true);
                store.setBuyStatus(BuyStatus.ONAY.getAttributeValue());
                storeRepository.update(store);

                // SATIN ALMA SORUMLUSUNA
                mailService.sendEmail(newEntity.getOwner().getEposta(),
                    "MeteorPanel - Satın Alma Talebi",newEntity.getOwner().getFullName() + ", " +
                        newEntity.getStore().getRequest() + " ürünü için yapılan taleple ilgili bir teklifiniz onaylanmıştır.\n" +
                        "Talebin SATIN ALMA DOSYASINI İNDİR butonuna tıklayıp formu ilgili kişilere imzalatın ve talebin DOSYA YÖNETİCİSİ bölümüne ekleyin.\n" +
                        "Daha sonra İlgili ürünü satın alabilirsiniz.",
                    false,false);

                // SATIN ALMA TALEBİ OLUŞTURANA
                mailService.sendEmail(newEntity.getStore().getAssigner().getEposta(),
                    "MeteorPanel - Satın Alma Talebi",newEntity.getStore().getAssigner().getFullName() + ", " +
                        newEntity.getStore().getRequest() + " ürünü için yapmış olduğunuz talebin teklifi onaylanmıştır.\n" +
                        "İlgili ürünle ilgili bir form oluşturuldu ve bu formda Talep Eden bölümüne imza atmanız gerekecektir.\n" +
                        "Daha sonra satın alınınca tarafınıza teslim edilecektir.",
                    false,false);

                // SATIN ALMA TALEBİNİN ONAYCISINA
                mailService.sendEmail(newEntity.getStore().getOwner().getEposta(),
                    "MeteorPanel - Satın Alma Talebi",newEntity.getStore().getOwner().getFullName() + ", " +
                        newEntity.getStore().getRequest() + " ürünü için yapılan talebin teklifi onaylanmıştır.\n" +
                        "İlgili ürünle ilgili bir form oluşturuldu ve bu formda Talep Onaycısı bölümüne imza atmanız gerekecektir.\n" +
                        " İlgili ürün satın alındıktan sonra talebi yapan kişiye teslim edilecektir.",
                    false,false);
            } else { // DÜZENLEME YAPAN 1.ONAYCI ve TEKLİF OLUŞTURAN DEĞİLSE
                throw new Exception("Onaycı siz değilsiniz!");
            }
        }
        else if (newEntity.getQuoteStatus().getId().equals(BuyStatus.RED.getId()) && newEntity.getSecondAssigner().getId().equals(getCurrentUserId())) {   // TEKLİF DURUMU : 2.ONAY BEKLENİYOR
            //List<BuyLimit> limit = limitRepository.findByUserId(newEntity.getSecondAssigner().getId());
            List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getSecondAssigner().getId(), newEntity.getStore().getMaliyet());
            for (BuyLimit limits : limit) {
                if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId()) && newEntity.getFuelTl().compareTo(limits.getUserTl()) > 0) {
                    throw new Exception("Bu limit reddedebileceğinizin dışındadır!");
                }
                if (!newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId()) && newEntity.getFuelTl().compareTo(limits.getUserDl()) > 0) {
                    throw new Exception("Bu limit reddedebileceğinizin dışındadır!");
                }
                if (newEntity.getPreparation().getId().equals(BuyQuoteStatus.BEKLE.getId())) {
                    throw new Exception("Hazırlık aşamasındaki bir teklifi reddemezsiniz!");
                }
            }

            if (oldEntity.getQuoteStatus().getId().equals(BuyStatus.ONAY.getId())) {
                throw new Exception("Onaylanan işlemde değişiklik yapamazsınız!");
            }
            newEntity.setOnayTl(BigDecimal.ZERO);
            Store store = newEntity.getStore();
            store.setBuyKey(true);
            store.setBuyStatus(BuyStatus.RED.getAttributeValue());
            storeRepository.update(store);
        }
        else if (!newEntity.getSecondAssigner().getId().equals(getCurrentUserId()) && (newEntity.getQuoteStatus().getId().equals(BuyStatus.ONAY.getId()) || newEntity.getQuoteStatus().getId().equals(BuyStatus.RED.getId()) )) {
            throw new Exception("Onaycı değilsiniz, ONAY VEYA RED işlemi yapamazsınız!");
        }


        Boolean onayara = false;
        if (newEntity.getQuoteStatus().getId().equals(BuyStatus.ONAY.getId())) {
            List<Buy> buys = repository.findByStoreId(newEntity.getStore().getId());
            for (Buy buy : buys) {
                if (buy.getQuoteStatus().getId().equals(BuyStatus.ONAY.getId())) {
                    onayara = true;
                    if (newEntity.getId().equals(buy.getId())) {
                        throw new Exception("Bu teklif zaten onaylanmış!");
                    }
                    throw new Exception("Onaylanan başka bir teklif var!");
                }
            }
        }
        if (newEntity.getQuoteStatus().getId().equals(BuyStatus.RED.getId())) {
            List<Buy> buys = repository.findByStoreId(newEntity.getStore().getId());
            for (Buy buy : buys) {
                if (buy.getQuoteStatus().getId().equals(BuyStatus.ONAY.getId())) {
                    throw new Exception("Onaylanan başka bir teklif var!");
                }
            }
        }
        return newEntity;
    }

    @Override
    public Buy afterUpdate(Buy newEntity, Buy oldEntity) throws Exception {

        return newEntity;
    }
}
