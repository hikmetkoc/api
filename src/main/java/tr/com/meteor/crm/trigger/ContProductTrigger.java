package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.Buy;
import tr.com.meteor.crm.domain.ContProduct;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.repository.BuyRepository;
import tr.com.meteor.crm.repository.ContProductRepository;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.BuyService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.attributevalues.ContractStatus;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component(ContProductTrigger.QUALIFIER)
public class ContProductTrigger extends Trigger<ContProduct, UUID, ContProductRepository> {
    final static String QUALIFIER = "ContProductTrigger";

    private final BuyRepository buyRepository;

    private final BuyService buyService;
    private final MailService mailService;
    public ContProductTrigger(CacheManager cacheManager, ContProductRepository contProductRepository,
                              BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                              BuyRepository buyRepository, MailService mailService,
                              BuyService buyService) {
        super(cacheManager, ContProduct.class, ContProductTrigger.class, contProductRepository, baseUserService, baseConfigurationService);
        this.buyRepository = buyRepository;
        this.mailService = mailService;
        this.buyService = buyService;
    }

    @Override
    public ContProduct beforeInsert(@NotNull ContProduct newEntity) throws Exception {
        if (newEntity.getAssigner() == null) {
            newEntity.setAssigner(getCurrentUser());
        }
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(newEntity.getBuy().getSecondAssigner());
        }
        if(!newEntity.getAssigner().getId().equals(newEntity.getBuy().getOwner().getId())){
            throw new Exception("Sadece talebi oluşturan kişi ürün ekleyebilir!");
        }
        newEntity.setStatus(false);
        if(newEntity.getStatus() && !newEntity.getOwner().getId().equals(getCurrentUserId())){
            throw new Exception("Bu ürüne sadece Onaycınız onay verebilir!");
        }
        return newEntity;
    }

    /*@Override
    public ContProduct afterInsert(@NotNull ContProduct newEntity) throws Exception {
        //updateProductStatus(newEntity.getBuy());
        return newEntity;
    }*/
    @Override
    public ContProduct beforeUpdate(@NotNull ContProduct oldEntity, @NotNull ContProduct newEntity) throws Exception {
        newEntity.setStatus(false);
        /*if(newEntity.getStatus() && !newEntity.getOwner().getId().equals(getCurrentUserId())){
            throw new Exception("Bu ürüne sadece Onaycınız onay verebilir!");
        }
        if(!newEntity.getAssigner().getId().equals(newEntity.getBuy().getOwner().getId()) || !newEntity.getOwner().getId().equals(newEntity.getBuy().getAssigner().getId())){
            throw new Exception("Sadece talebi oluşturan kişi ve onaycı kişi düzenleme yapabilir!");
        }*/
        return newEntity;
    }

    /*@Override
    public ContProduct afterUpdate(@NotNull ContProduct oldEntity, @NotNull ContProduct newEntity) throws Exception {
        //updateProductStatus(newEntity.getBuy());
        return newEntity;
    }*/

    private void updateProductStatus(Buy buy) throws Exception {
        List<ContProduct> contProducts = repository.findByBuyId(buy.getId());
        int approvedCount = 0;
        int allCount = contProducts.size();
        BigDecimal totalBuy = BigDecimal.ZERO;
        for (ContProduct cp : contProducts) {
            totalBuy = totalBuy.add(cp.getFuelTl());
            if (cp.getStatus()) {
                approvedCount++;
            }
        }
        System.out.println(approvedCount + "/" + allCount + " - " + totalBuy);
        /*Optional<Buy> buys = buyRepository.findById(buy.getId());
        Buy updatedBuy = buys.orElseThrow(() -> new Exception("Buy not found!"));
        updatedBuy.setProductStatus(approvedCount + "/" + allCount);
        updatedBuy.setFuelTl(totalBuy);

        if (updatedBuy.getSirket() == null) {
            throw new Exception("Şirket kaydı yok!");
        }*/
        Buy updatedBuy = buy;
        //updatedBuy.setProductStatus(approvedCount + "/" + allCount);
        updatedBuy.setFuelTl(totalBuy);
        buyRepository.save(updatedBuy);
    }
}
