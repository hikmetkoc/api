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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component(InvoiceListTrigger.QUALIFIER)
public class InvoiceListTrigger extends Trigger<InvoiceList, UUID, InvoiceListRepository> {
    final static String QUALIFIER = "InvoiceListTrigger";

    private final CustomerRepository customerRepository;

    private final SpendRepository spendRepository;

    private final InvoiceListService invoiceListService;

    private final PaymentOrderRepository paymentOrderRepository;

    private final AttributeValueRepository attributeValueRepository;

    private final AttributeValueRepository attRepository;
    private final ResponsibleRepository responsibleRepository;
    private final StoreRepository storeRepository;

    private final BuyLimitRepository limitRepository;

    private final MailService mailService;

    private final PostaGuverciniService postaGuverciniService;

    private final MobileNotificationService mobileNotificationService;

    private final FileContainerService fileContainerService;

    private final CorrectGroupRepository correctGroupRepository;

    private final ApprovalUserLimitRepository approvalUserLimitRepository;

    public InvoiceListTrigger(CacheManager cacheManager, InvoiceListRepository paymentOrderRepository, BaseUserService baseUserService,
                              BaseConfigurationService baseConfigurationService, CustomerRepository customerRepository,
                              SpendRepository spendRepository, InvoiceListService invoiceListService, PaymentOrderRepository paymentOrderRepository1, AttributeValueRepository attributeValueRepository, AttributeValueRepository attRepository, ResponsibleRepository responsibleRepository, StoreRepository storeRepository, BuyLimitRepository limitRepository, MailService mailService, PostaGuverciniService postaGuverciniService, MobileNotificationService mobileNotificationService, FileContainerService fileContainerService, CorrectGroupRepository correctGroupRepository, ApprovalUserLimitRepository approvalUserLimitRepository) {
        super(cacheManager, InvoiceList.class, InvoiceListTrigger.class, paymentOrderRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
        this.spendRepository = spendRepository;
        this.invoiceListService = invoiceListService;
        this.paymentOrderRepository = paymentOrderRepository1;
        this.attributeValueRepository = attributeValueRepository;
        this.attRepository = attRepository;
        this.responsibleRepository = responsibleRepository;
        this.storeRepository = storeRepository;
        this.limitRepository = limitRepository;
        this.mailService = mailService;
        this.postaGuverciniService = postaGuverciniService;
        this.mobileNotificationService = mobileNotificationService;
        this.fileContainerService = fileContainerService;
        this.correctGroupRepository = correctGroupRepository;
        this.approvalUserLimitRepository = approvalUserLimitRepository;
    }

    @Override
    public InvoiceList beforeInsert(@NotNull InvoiceList newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            List<Responsible> responsibles = responsibleRepository.findByCustomerId(newEntity.getCustomer().getId());
            if (!responsibles.isEmpty()) {
                if (responsibles.size() == 1) {
                    newEntity.setOwner(responsibles.get(0).getOwner());
                    newEntity.setInvoiceStatus(InvoiceStatus.ATANDI.getAttributeValue());
                } else {
                    for (Responsible responsible:responsibles) {
                        if (responsible.getOwner().getSirket().getLabel().equals(newEntity.getSirket().getLabel()) &&
                            responsible.getOncelik().getLabel().equals("1.Sorumlu")) {
                                newEntity.setOwner(responsible.getOwner());
                                newEntity.setInvoiceStatus(InvoiceStatus.ATANDI.getAttributeValue());
                        }
                    }
                }
            }
        }
        return newEntity;
    }

    @Override
    public InvoiceList beforeUpdate(InvoiceList oldEntity, InvoiceList newEntity) throws Exception {
        // Fatura Listesinden Ödeme Talimatı Oluşturma
        if (getCurrentUserId().equals(baseUserService.getUserFullFetched(26800L).get().getId())) {
            newEntity.setSuccessDate(Instant.now());
        }
        if (!getCurrentUserId().equals(baseUserService.getUserFullFetched(26800L).get().getId()) && newEntity.getOwner() != null) {
            // ZORUNLU ALAN KONTROLLERİ
            if (newEntity.getIban() == null && newEntity.getPaymentType().getId().equals("PaymentType_Havale") && newEntity.getSuccess().equals(false)) {
                throw new Exception("Havale ödemelerinde IBAN Bilgisi zorunlu alandır. Eğer listelenen bir IBAN yoksa Tedarikçiler bölümünden ilgili tedarikçeye IBAN ekleyiniz!");
            }
            if (newEntity.getOdemeYapanSirket() == null) {
                throw new Exception("Ödeme Yapan Firma alanı boş bırakılamaz!");
            }
            if (newEntity.getCost() == null) {
                throw new Exception("Maliyet yeri boş bırakılamaz!");
            }
            if (newEntity.getMaturityDate() == null) {
                throw new Exception("Vade tarihi boş bırakılamaz!");
            }
            if (newEntity.getPaymentStyle() == null) {
                throw new Exception("Ödenecek Para Birimi boş bırakılamaz!");
            }
            // VARSAYILAN ATAMALAR
            if (newEntity.getAutopay() == null) { newEntity.setAutopay(false);}
            if (newEntity.getSuccess() == null) { newEntity.setSuccess(false);}
            if (newEntity.getKismi() == null) { newEntity.setKismi(false);}
            if (newEntity.getDekont() == null) { newEntity.setDekont(false);}
            if (newEntity.getPayTl() == null) { newEntity.setPayTl(BigDecimal.ZERO);}

            //ÖDENDİ VE OTOMATİK ÖDENDİ İLE İLGİLİ KONTROLLER
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
                throw new Exception("Lütfen ödendi veya otomatik ödendi seçeneklerinden sadece birini seçiniz!");
            }

            // MÜKERRER FATURA KONTROLÜ
            List<PaymentOrder> paymentOrders = paymentOrderRepository.findAll();
            for (PaymentOrder paymentOrder : paymentOrders) {
                if (paymentOrder.getInvoiceNum().equals(newEntity.getInvoiceNum()) && !paymentOrder.getStatus().getId().equals(PaymentStatus.RED.getId()) && paymentOrder.getCustomer().equals(newEntity.getCustomer())) {
                    throw new Exception("Bu fatura numarasına ait bir fatura Ödeme Talimatlarında mevcut! Lütfen kontrol ediniz!");
                }
            }

            // ÖDEME TALİMATI OLUŞTURMA
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setId(UUID.randomUUID());
            paymentOrder.setOwner(newEntity.getOwner());

            Optional<CorrectGroup> correctGroup = correctGroupRepository.findByApprovalGroupId(newEntity.getApprovalGroup().getId());
            if (correctGroup.isPresent()) {
                ApprovalUserLimit chief = approvalUserLimitRepository.findByUser(correctGroup.get().getChief());
                ApprovalUserLimit manager = approvalUserLimitRepository.findByUser(correctGroup.get().getManager());
                ApprovalUserLimit director = approvalUserLimitRepository.findByUser(correctGroup.get().getDirector());

                if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.TL.getId())) {
                    if (chief.getTlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(chief.getUser())) {
                            paymentOrder.setAssigner(chief.getUser());
                            paymentOrder.setSecondAssigner(chief.getUser());
                        } else {
                            paymentOrder.setAssigner(getCurrentUser());
                            paymentOrder.setSecondAssigner(chief.getUser());
                        }
                    } else if (manager.getTlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(manager.getUser())) {
                            paymentOrder.setAssigner(manager.getUser());
                            paymentOrder.setSecondAssigner(manager.getUser());
                        } else {
                            paymentOrder.setAssigner(chief.getUser());
                            paymentOrder.setSecondAssigner(manager.getUser());
                        }
                    } else if (director.getTlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(director.getUser())) {
                            paymentOrder.setAssigner(director.getUser());
                            paymentOrder.setSecondAssigner(director.getUser());
                        } else {
                            paymentOrder.setAssigner(manager.getUser());
                            paymentOrder.setSecondAssigner(director.getUser());
                        }
                    } else {
                        paymentOrder.setAssigner(director.getUser());
                        paymentOrder.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                    }
                } else if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.DOLAR.getId())) {
                    if (chief.getDlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(chief.getUser())) {
                            paymentOrder.setAssigner(chief.getUser());
                            paymentOrder.setSecondAssigner(chief.getUser());
                        } else {
                            paymentOrder.setAssigner(getCurrentUser());
                            paymentOrder.setSecondAssigner(chief.getUser());
                        }
                    } else if (manager.getDlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(manager.getUser())) {
                            paymentOrder.setAssigner(manager.getUser());
                            paymentOrder.setSecondAssigner(manager.getUser());
                        } else {
                            paymentOrder.setAssigner(chief.getUser());
                            paymentOrder.setSecondAssigner(manager.getUser());
                        }
                    } else if (director.getDlLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(director.getUser())) {
                            paymentOrder.setAssigner(director.getUser());
                            paymentOrder.setSecondAssigner(director.getUser());
                        } else {
                            paymentOrder.setAssigner(manager.getUser());
                            paymentOrder.setSecondAssigner(director.getUser());
                        }
                    } else {
                        paymentOrder.setAssigner(director.getUser());
                        paymentOrder.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                    }
                } else if (newEntity.getMoneyType().getId().equals(MoneyTypeStatus.EURO.getId())) {
                    if (chief.getEuroLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(chief.getUser())) {
                            paymentOrder.setAssigner(chief.getUser());
                            paymentOrder.setSecondAssigner(chief.getUser());
                        } else {
                            paymentOrder.setAssigner(getCurrentUser());
                            paymentOrder.setSecondAssigner(chief.getUser());
                        }
                    } else if (manager.getEuroLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(manager.getUser())) {
                            paymentOrder.setAssigner(manager.getUser());
                            paymentOrder.setSecondAssigner(manager.getUser());
                        } else {
                            paymentOrder.setAssigner(chief.getUser());
                            paymentOrder.setSecondAssigner(manager.getUser());
                        }
                    } else if (director.getEuroLimit().compareTo(newEntity.getAmount()) > 0) {
                        if (newEntity.getOwner().equals(director.getUser())) {
                            paymentOrder.setAssigner(director.getUser());
                            paymentOrder.setSecondAssigner(director.getUser());
                        } else {
                            paymentOrder.setAssigner(manager.getUser());
                            paymentOrder.setSecondAssigner(director.getUser());
                        }
                    } else {
                        paymentOrder.setAssigner(director.getUser());
                        paymentOrder.setSecondAssigner(baseUserService.getUserFullFetched(101L).get());
                    }
                }
            }

            if (paymentOrder.getAssigner() == null || paymentOrder.getSecondAssigner() == null) {
                throw new Exception("Bu maliyet yerine ait bir onay sisteminiz mevcut değildir. Lütfen IT ekibiyle iletişime geçiniz.");
            }
            paymentOrder.setInvoiceDate(newEntity.getInvoiceDate());
            paymentOrder.setMaturityDate(newEntity.getMaturityDate());
            paymentOrder.setInvoiceNum(newEntity.getInvoiceNum());
            paymentOrder.setCustomer(newEntity.getCustomer());
            paymentOrder.setSirket(newEntity.getSirket());
            paymentOrder.setOdemeYapanSirket(newEntity.getOdemeYapanSirket());
            paymentOrder.setCost(newEntity.getCost());
            paymentOrder.setApprovalGroup(newEntity.getApprovalGroup());
            paymentOrder.setPaymentSubject(newEntity.getPaymentSubject());
            paymentOrder.setAmount(newEntity.getAmount());
            paymentOrder.setMoneyType(newEntity.getMoneyType());
            paymentOrder.setIban(newEntity.getIban());
            paymentOrder.setDescription(newEntity.getDescription());
            paymentOrder.setMuhasebeci(baseUserService.getUserFullFetched(1L).get());
            paymentOrder.setDekont(newEntity.getDekont());
            paymentOrder.setExchange(newEntity.getExchange());
            paymentOrder.setSuccess(newEntity.getSuccess());
            paymentOrder.setKismi(newEntity.getKismi());
            paymentOrder.setAutopay(newEntity.getAutopay());
            paymentOrder.setPaymentStyle(newEntity.getPaymentStyle());
            paymentOrder.setName(newEntity.getInvoiceNum() + " " + newEntity.getCustomer().getCommercialTitle());
            paymentOrder.setPayTl(newEntity.getPayTl());
            paymentOrder.setKaynak("FATURA LİSTESİ");
            paymentOrder.setPayamount(BigDecimal.ZERO);
            paymentOrder.setNextamount(paymentOrder.getAmount());

            // TALİMAT OLUŞTURMA --> STATUS AYARI
            if (newEntity.getSuccess().equals(false)) {
                paymentOrder.setStatus(PaymentStatus.ONAY1.getAttributeValue());
            } else {
                paymentOrder.setStatus(PaymentStatus.ODENDI.getAttributeValue());
            }
            if (!newEntity.getAutopay().equals(false)) {
                paymentOrder.setStatus(PaymentStatus.OTO.getAttributeValue());
            }

            // FATURA ÖDENDİ VEYA OTOMATİK ÖDENDİ DURUMUNDAYSA ÖDENEN TUTAR => TOPLAM TUTAR, KALAN TUTAR => 0, DEĞİLSE TERSİ.
            if (newEntity.getSuccess().equals(true) || newEntity.getAutopay().equals(true)) {
                if (newEntity.getDekont().equals(true)) {
                    throw new Exception("Ödendi veya Otomatik Ödeme olarak seçtiğiniz faturanın dekontonu talep edemezsiniz!");
                }
                if (newEntity.getKismi().equals(true)) {
                    throw new Exception("Ödendi veya Otomatik Ödeme olarak seçitiğiniz fatura kısmi ödeme olarak girilemez!");
                }
                paymentOrder.setPayamount(paymentOrder.getAmount());
                paymentOrder.setNextamount(BigDecimal.ZERO);
            }

            // TEK ÖDEME OLUŞTURMA
            if (newEntity.getKismi().equals(false)) {
                if (newEntity.getSuccess().equals(false) && newEntity.getAutopay().equals(false)) { // ÖDENMEYEN FATURA
                    spendRepository.insertSpend(UUID.randomUUID(), getCurrentUserId(), SpendStatus.ODENMEDI.getId(), paymentOrder.getId(), newEntity.getAmount(), "1.Onay Bekleniyor", false, this.baseUserService.getUserFullFetched(1L).get().getId(), Instant.now(), newEntity.getMaturityDate(), "Tek Ödeme", newEntity.getPayTl(), newEntity.getCustomer().getId(), newEntity.getOdemeYapanSirket().getLabel());
                } else if (newEntity.getSuccess().equals(true) && newEntity.getAutopay().equals(false)) { // ÖDENDİ TİKLİ FATURA
                    spendRepository.insertSpend(UUID.randomUUID(), getCurrentUserId(), SpendStatus.ODENDI.getId(), paymentOrder.getId(), newEntity.getAmount(), "Ödendi", false, this.baseUserService.getUserFullFetched(1L).get().getId(), Instant.now(), newEntity.getMaturityDate(), "Tek Ödeme", newEntity.getPayTl(), newEntity.getCustomer().getId(), newEntity.getOdemeYapanSirket().getLabel());
                } else if (newEntity.getSuccess().equals(false) && newEntity.getAutopay().equals(true)) { // OTOMATİK ÖDENDİ TİKLİ FATURA
                    spendRepository.insertSpend(UUID.randomUUID(), getCurrentUserId(), SpendStatus.ODENDI.getId(), paymentOrder.getId(), newEntity.getAmount(), "Otomatik Ödendi", false, this.baseUserService.getUserFullFetched(1L).get().getId(), Instant.now(), newEntity.getMaturityDate(), "Tek Ödeme", newEntity.getPayTl(), newEntity.getCustomer().getId(), newEntity.getOdemeYapanSirket().getLabel());
                }
            }
            if (newEntity.getIban() != null) {
                paymentOrder.setStrIban(newEntity.getIban().getName());
            }
            if (newEntity.getOwner().getBirim().getId().equals("Birimler_Muh")) {
                paymentOrder.setMuhasebeGoruntusu(true);
            }
            paymentOrderRepository.save(paymentOrder);

            //TALİMATA DÖNÜŞTÜRÜLDÜ STATUS DEĞİŞİMİ
            List<AttributeValue> attributeValues = attributeValueRepository.findAll();
            newEntity.setInvoiceStatus(getAttributeValueById(attributeValues, "Fatura_Durumlari_Donus"));

            //POSTA GÜVERCİNİ
            //postaGuverciniService.SendSmsService(newEntity.getOwner().getPhone(), "Merhaba " + newEntity.getOwner().getFullName() + " , faturanız başarılı bir şekilde talimata dönüştürüldü. [METEORPANEL SMS SERVICE]");
        }
        return newEntity;
    }

    @Override
    public InvoiceList afterUpdate(InvoiceList newEntity, InvoiceList oldEntity) throws Exception {
        if (newEntity.getInvoiceStatus() == null) {
            newEntity.setInvoiceStatus(InvoiceStatus.MUHASEBE.getAttributeValue());
        }
        return newEntity;
    }

    private AttributeValue getAttributeValueById(List<AttributeValue> attributeValues, String id) {
        for (AttributeValue attributeValue : attributeValues) {
            if (attributeValue.getId().equals(id)) {
                return attributeValue;
            }
        }
        return null;
    }
}
