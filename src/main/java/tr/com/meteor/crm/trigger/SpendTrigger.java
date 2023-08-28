package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.CustomActivityRepository;
import tr.com.meteor.crm.repository.CustomTaskRepository;
import tr.com.meteor.crm.repository.PaymentOrderRepository;
import tr.com.meteor.crm.repository.SpendRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.attributevalues.SpendStatus;
import tr.com.meteor.crm.utils.configuration.Configurations;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component(SpendTrigger.QUALIFIER)
public class SpendTrigger extends Trigger<Spend, UUID, SpendRepository> {
    final static String QUALIFIER = "SpendTrigger";

    private final PaymentOrderRepository paymentOrderRepository;

    private final MailService mailService;
    public SpendTrigger(CacheManager cacheManager, SpendRepository spendRepository,
                        BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                        PaymentOrderRepository paymentOrderRepository, MailService mailService) {
        super(cacheManager, Spend.class, SpendTrigger.class, spendRepository, baseUserService, baseConfigurationService);
        this.paymentOrderRepository = paymentOrderRepository;
        this.mailService = mailService;
    }

    @Override
    public Spend beforeInsert(@NotNull Spend newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        return newEntity;
    }

    @Override
    public Spend afterInsert(@NotNull Spend newEntity) throws Exception {
        BigDecimal totalSpend = BigDecimal.ZERO;
        List<Spend> spendList = repository.findAll();
        for (Spend spend: spendList) {
            if (spend.getPaymentorder().getId().equals(newEntity.getPaymentorder().getId()) && !spend.getStatus().getId().equals("Spend_Status_Red")) {
                totalSpend = totalSpend.add(spend.getAmount());
            }
        }
        if (newEntity.getPaymentorder().getAmount().compareTo(totalSpend) < 0) {
            throw new Exception("Talep ettiğiniz ödeme tutarı Toplam Tutarı aşmaktadır! TALEP EDİLEN ÖDEME: " + totalSpend + ", TALİMAT TUTARI: " + newEntity.getPaymentorder().getAmount());
        }
        if (newEntity.getMaturityDate() == null) {
            throw new Exception("Vade Tarihi boş girilemez");
        }
        Integer sayac = 1;
        List<Spend> str = repository.findByPaymentorderId(newEntity.getPaymentorder().getId());
        for (Spend spend : str) {
            if (spend.getPaymentNum() != null) {
                sayac++;
            }
        }
        newEntity.setPayTl(BigDecimal.ZERO);
        newEntity.setCustomer(newEntity.getPaymentorder().getCustomer());
        newEntity.setPaymentNum(sayac + ".Ödeme");
        newEntity.setPaymentStatus(newEntity.getPaymentorder().getStatus().getLabel());
        newEntity.setFinance(baseUserService.getUserFullFetched(1L).get());
        return newEntity;
    }

    @Override
    public Spend beforeUpdate(@NotNull Spend oldEntity, @NotNull Spend newEntity) throws Exception {
        if (newEntity.getStatus().getId().equals(SpendStatus.ODENDI.getId()) && newEntity.getLock().equals(true)){
            throw new Exception("Ödeme tamamlandıktan sonra değişiklik yapamazsınız!");
        }

        if (newEntity.getStatus().getId().equals(SpendStatus.ODENDI.getId())) {
            newEntity.setLock(true);
            newEntity.setSpendDate(Instant.now());
        }
        return newEntity;
    }
}
