package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.*;
import tr.com.meteor.crm.utils.attributevalues.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Component(PaymentOrderTrigger.QUALIFIER)
public class PaymentOrderTrigger extends Trigger<PaymentOrder, UUID, PaymentOrderRepository> {
    final static String QUALIFIER = "PaymentOrderTrigger";

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    private final SpendRepository spendRepository;

    private final BuyLimitRepository limitRepository;

    private final PaymentOrderService service;

    private final MailService mailService;

    private final CorrectGroupRepository correctGroupRepository;

    private final ApprovalUserLimitRepository approvalUserLimitRepository;

    private final PostaGuverciniService postaGuverciniService;

    private final MobileNotificationService mobileNotificationService;

    public PaymentOrderTrigger(CacheManager cacheManager, PaymentOrderRepository paymentOrderRepository, BaseUserService baseUserService,
                               BaseConfigurationService baseConfigurationService, CustomerRepository customerRepository,
                               StoreRepository storeRepository, SpendRepository spendRepository, BuyLimitRepository limitRepository, PaymentOrderService service, MailService mailService, CorrectGroupRepository correctGroupRepository, ApprovalUserLimitRepository approvalUserLimitRepository, PostaGuverciniService postaGuverciniService, MobileNotificationService mobileNotificationService) {
        super(cacheManager, PaymentOrder.class, PaymentOrderTrigger.class, paymentOrderRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.spendRepository = spendRepository;
        this.limitRepository = limitRepository;
        this.service = service;
        this.mailService = mailService;
        this.correctGroupRepository = correctGroupRepository;
        this.approvalUserLimitRepository = approvalUserLimitRepository;
        this.postaGuverciniService = postaGuverciniService;
        this.mobileNotificationService = mobileNotificationService;
    }

    @Override
    public PaymentOrder beforeInsert(@NotNull PaymentOrder newEntity) throws Exception {
        // MÜKERRERLİK KONTROLÜ
        boolean faturakontrol = repository.existsByInvoiceNumAndCustomerNotPaymentOrderRed(newEntity.getInvoiceNum(), newEntity.getCustomer());
        if (faturakontrol) {
            throw new Exception("Mükerrer Fatura giremezsiniz!");
        }

        // VARSAYILAN ATAMALAR
        if(newEntity.getOwner() == null) {
            newEntity.setOwner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        }
        if (newEntity.getIban() != null) {
            newEntity.setStrIban(newEntity.getIban().getName());
        }
        if (newEntity.getAutopay() == null) { newEntity.setAutopay(false);}
        if (newEntity.getSuccess() == null) { newEntity.setSuccess(false);}
        if (newEntity.getKismi() == null) { newEntity.setKismi(false);}
        if (newEntity.getDekont() == null) { newEntity.setDekont(false);}
        if (newEntity.getPayTl() == null) {
            newEntity.setPayTl(BigDecimal.ZERO);
        }

        // ZORUNLU ALAN KONTROLLERİ
        if (newEntity.getCustomer() == null) {
            throw new Exception("Lütfen Ödeme yapılacak firmayı yazın!");
        }
        if (newEntity.getIban() == null && newEntity.getPaymentType().getId().equals("PaymentType_Havale") && newEntity.getSuccess().equals(false)) {
            throw new Exception("Havale ödemelerinde IBAN Bilgisi zorunlu alandır. Eğer listelenen bir IBAN yoksa Tedarikçiler bölümünden ilgili tedarikçeye IBAN ekleyiniz!");
        }
        if (newEntity.getSirket() == null) {
            throw new Exception("Fatura Sahibi Firma alanı boş bırakılamaz!");
        }
        if (newEntity.getOdemeYapanSirket() == null) {
            throw new Exception("Ödeme Yapan Firma alanı boş bırakılamaz!");
        }
        if (newEntity.getCost() == null) {
            throw new Exception("Maliyet yeri boş bırakılamaz!");
        }
        if (newEntity.getInvoiceDate() == null) {
            throw new Exception("Fatura tarihi boş bırakılamaz!");
        }
        if (newEntity.getMaturityDate() == null) {
            throw new Exception("Vade tarihi boş bırakılamaz!");
        }
        if (newEntity.getPaymentStyle() == null) {
            throw new Exception("Ödenecek Para Birimi boş bırakılamaz!");
        }
        if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId()) && !newEntity.getPaymentStyle().getId().equals("Payment_Style_Tl")) {
            throw new Exception("Tl olan fatura dolarla ödenemez!");
        }
        if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.DOLAR.getId()) && newEntity.getPaymentStyle().getId().equals("Payment_Style_Tl") && (newEntity.getSuccess().equals(true) || newEntity.getAutopay().equals(true)) && newEntity.getPayTl().equals(BigDecimal.ZERO)) {
            throw new Exception("Fatura ödemesi yapıldıysa, Ödenen TL Tutarı boş bırakılamaz!");
        }
        if(newEntity.getSuccess().equals(false) && newEntity.getAutopay().equals(false) && newEntity.getPayTl().compareTo(BigDecimal.ZERO)>0) {
            newEntity.setPayTl(BigDecimal.ZERO);
        }
        if (newEntity.getAutopay().equals(true) && newEntity.getSuccess().equals(true)) {
            throw new Exception("Lütfen ödendi veya otomatik ödemede seçeneklerinden sadece birini seçiniz!");
        }

        // 5'TEN FAZLA AÇIK ÖDEME KONTROLÜ
        if (newEntity.getPaymentSubject().getId().equals(PaymentSubjectStatus.ACIK.getId())) {
            if (repository.countByPaymentSubjectAndClosePdfAndOwner(newEntity.getPaymentSubject(), false, getCurrentUser()) > 4) {
                throw new Exception("Açıkta 5 tane talimatın görseli eklenmediği için yeni talimat oluşturamazsınız!");
            }
        }
        // STATUS AYARI
        if (newEntity.getSuccess().equals(false)) {
            newEntity.setStatus(PaymentStatus.ONAY1.getAttributeValue());
        } else {
            newEntity.setStatus(PaymentStatus.ODENDI.getAttributeValue());
        }
        if (newEntity.getAutopay().equals(true)) {
            newEntity.setStatus(PaymentStatus.OTO.getAttributeValue());
        }

        // VARSAYILAN ATAMALAR
        newEntity.setPayamount(BigDecimal.ZERO);
        newEntity.setNextamount(newEntity.getAmount());
        newEntity.setMuhasebeci(baseUserService.getUserFullFetched(1L).get());
        newEntity.setName(newEntity.getInvoiceNum() + " " + newEntity.getCustomer().getCommercialTitle());
        newEntity.setKaynak("MANUEL TALİMAT");

        if (newEntity.getSuccess().equals(true) || newEntity.getAutopay().equals(true)) {
            if (newEntity.getDekont().equals(true)) {
                throw new Exception("Ödendi veya Otomatik Ödeme olarak seçtiğiniz faturanın dekontonu talep edemezsiniz!");
            }
            if (newEntity.getKismi().equals(true)) {
                throw new Exception("Ödendi veya Otomatik Ödeme olarak seçitiğiniz fatura kısmi ödeme olarak girilemez!");
            }
            newEntity.setPayamount(newEntity.getAmount());
            newEntity.setNextamount(BigDecimal.ZERO);
        }

        // MANUEL TALİMATSA ONAYCILARI ATA
        if (newEntity.getAssigner() == null) {
            /* todo: Correct Group tan newEntity.approvalGroup kontrolü yapılacak. Eşleşen kullanıcılarla newEntity.getAmount yoluyla
             ApprovalUserLimit bakılarak assigner ve secondAssigner atanacak. */
            Optional<CorrectGroup> correctGroup = correctGroupRepository.findByApprovalGroupId(newEntity.getApprovalGroup().getId());
            if (correctGroup.isPresent()) {
                ApprovalUserLimit chief = approvalUserLimitRepository.findByUser(correctGroup.get().getChief());
                ApprovalUserLimit manager = approvalUserLimitRepository.findByUser(correctGroup.get().getManager());
                ApprovalUserLimit director = approvalUserLimitRepository.findByUser(correctGroup.get().getDirector());

                if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId())) {
                    if (chief.getTlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(chief.getUser())) {
                            newEntity.setAssigner(chief.getUser());
                            newEntity.setSecondAssigner(chief.getUser());
                        } else {
                            newEntity.setAssigner(getCurrentUser());
                            newEntity.setSecondAssigner(chief.getUser());
                        }
                    } else if (manager.getTlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(manager.getUser())) {
                            newEntity.setAssigner(manager.getUser());
                            newEntity.setSecondAssigner(manager.getUser());
                        } else {
                            newEntity.setAssigner(chief.getUser());
                            newEntity.setSecondAssigner(manager.getUser());
                        }
                    } else if (director.getTlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(director.getUser())) {
                            newEntity.setAssigner(director.getUser());
                            newEntity.setSecondAssigner(director.getUser());
                        } else {
                            newEntity.setAssigner(manager.getUser());
                            newEntity.setSecondAssigner(director.getUser());
                        }
                    } else {
                        newEntity.setAssigner(director.getUser());
                        newEntity.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                    }
                } else if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.DOLAR.getId())) {
                    if (chief.getDlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(chief.getUser())) {
                            newEntity.setAssigner(chief.getUser());
                            newEntity.setSecondAssigner(chief.getUser());
                        } else {
                            newEntity.setAssigner(getCurrentUser());
                            newEntity.setSecondAssigner(chief.getUser());
                        }
                    } else if (manager.getDlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(manager.getUser())) {
                            newEntity.setAssigner(manager.getUser());
                            newEntity.setSecondAssigner(manager.getUser());
                        } else {
                            newEntity.setAssigner(chief.getUser());
                            newEntity.setSecondAssigner(manager.getUser());
                        }
                    } else if (director.getDlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(director.getUser())) {
                            newEntity.setAssigner(director.getUser());
                            newEntity.setSecondAssigner(director.getUser());
                        } else {
                            newEntity.setAssigner(manager.getUser());
                            newEntity.setSecondAssigner(director.getUser());
                        }
                    } else {
                        newEntity.setAssigner(director.getUser());
                        newEntity.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                    }
                } else if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.EURO.getId())) {
                    if (chief.getEuroLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(chief.getUser())) {
                            newEntity.setAssigner(chief.getUser());
                            newEntity.setSecondAssigner(chief.getUser());
                        } else {
                            newEntity.setAssigner(getCurrentUser());
                            newEntity.setSecondAssigner(chief.getUser());
                        }
                    } else if (manager.getEuroLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(manager.getUser())) {
                            newEntity.setAssigner(manager.getUser());
                            newEntity.setSecondAssigner(manager.getUser());
                        } else {
                            newEntity.setAssigner(chief.getUser());
                            newEntity.setSecondAssigner(manager.getUser());
                        }
                    } else if (director.getEuroLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(director.getUser())) {
                            newEntity.setAssigner(director.getUser());
                            newEntity.setSecondAssigner(director.getUser());
                        } else {
                            newEntity.setAssigner(manager.getUser());
                            newEntity.setSecondAssigner(director.getUser());
                        }
                    } else {
                        newEntity.setAssigner(director.getUser());
                        newEntity.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                    }
                }
            }
        }
        if (newEntity.getAssigner() == null || newEntity.getSecondAssigner() == null) {
            throw new Exception("Bu maliyet yerine ait bir onay sisteminiz mevcut değildir. Lütfen IT ekibiyle iletişime geçiniz.");
        }
        if (newEntity.getOwner().getBirim().getId().equals("Birimler_Muh")) {
            newEntity.setMuhasebeGoruntusu(true);
        }
        return newEntity;
    }

    @Override
    public PaymentOrder afterInsert(@NotNull PaymentOrder newEntity) throws Exception {
        if (newEntity.getKismi().equals(false)) {
            if (newEntity.getSuccess().equals(false) && newEntity.getAutopay().equals(false)) {
                spendRepository.insertSpend(UUID.randomUUID(), getCurrentUserId(), SpendStatus.ODENMEDI.getId(), newEntity.getId(), newEntity.getAmount(), "1.Onay Bekleniyor", false, this.baseUserService.getUserFullFetched(1L).get().getId(), Instant.now(), newEntity.getMaturityDate(), "Tek Ödeme", newEntity.getPayTl(), newEntity.getCustomer().getId(), newEntity.getOdemeYapanSirket().getLabel());
            } else if (newEntity.getSuccess().equals(true) && newEntity.getAutopay().equals(false)) {
                spendRepository.insertSpend(UUID.randomUUID(), getCurrentUserId(), SpendStatus.ODENDI.getId(), newEntity.getId(), newEntity.getAmount(), "Ödendi", false, this.baseUserService.getUserFullFetched(1L).get().getId(), Instant.now(), newEntity.getMaturityDate(), "Tek Ödeme", newEntity.getPayTl(), newEntity.getCustomer().getId(), newEntity.getOdemeYapanSirket().getLabel());
            } else if (newEntity.getSuccess().equals(false) && newEntity.getAutopay().equals(true)) {
                spendRepository.insertSpend(UUID.randomUUID(), getCurrentUserId(), SpendStatus.OTO.getId(), newEntity.getId(), newEntity.getAmount(), "Otomatik Ödemede", false, this.baseUserService.getUserFullFetched(1L).get().getId(), Instant.now(), newEntity.getMaturityDate(), "Otomatik Ödeme", newEntity.getPayTl(), newEntity.getCustomer().getId(), newEntity.getOdemeYapanSirket().getLabel());
            }
        }
        newEntity.setPdf(false);
        if (!newEntity.getStatus().getId().equals(PaymentStatus.ODENDI.getId()) && !newEntity.getStatus().getId().equals(PaymentStatus.OTO.getId())) {
            String text = newEntity.getInvoiceNum() + " fatura numarali odeme talimati, " +
                "1.Onay Bekliyor durumundadir ve onayiniz beklenmektedir. meteorpanel.com/paymentorder adresinden ilgili talimata ulasabilirsiniz.";
            if (!newEntity.getOwner().getId().equals(newEntity.getAssigner().getId())) {
                if (newEntity.getAssigner().getUnvan().getId().equals("Unvanlar_Gen_Mud")
                    || newEntity.getAssigner().getUnvan().getId().equals("Unvanlar_Yon_Bas")
                    || newEntity.getAssigner().getUnvan().getId().equals("Unvanlar_San_Bas")
                    || newEntity.getAssigner().getUnvan().getId().equals("Unvanlar_Ins_Dir")
                ) {
                    postaGuverciniService.SendSmsService(newEntity.getAssigner().getPhone(), text);
                }
                mailService.sendEmail(newEntity.getAssigner().getEposta(), "Meteor Panel - Ödeme Talimatı", text, false, false);
            }
        }
        return newEntity;
    }

}
