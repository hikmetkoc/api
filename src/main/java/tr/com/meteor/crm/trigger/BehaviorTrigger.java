package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.Behavior;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.domain.MotionSums;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.utils.attributevalues.BehaviorStatus;
import tr.com.meteor.crm.utils.configuration.Configurations;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(BehaviorTrigger.QUALIFIER)
public class BehaviorTrigger extends Trigger<Behavior, UUID, BehaviorRepository> {
    final static String QUALIFIER = "BehaviorTrigger";

    private final MotionSumsRepository motionSumsRepository;

    private final CustomerRepository customerRepository;


    private final MailService mailService;
    public BehaviorTrigger(CacheManager cacheManager, BehaviorRepository activityRepository,
                           BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                           MotionSumsRepository motionSumsRepository, CustomerRepository customerRepository, MailService mailService) {
        super(cacheManager, Behavior.class, BehaviorTrigger.class, activityRepository, baseUserService, baseConfigurationService);
        this.motionSumsRepository = motionSumsRepository;
        this.customerRepository = customerRepository;
        this.mailService = mailService;
    }

    @Override
    public Behavior beforeInsert(@NotNull Behavior newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        // bakiye = borc (bize yapılan ödeme) - alacak ( bize kesilen fatura )
        Optional<MotionSums> motionSums = motionSumsRepository.findById(newEntity.getMotionsums().getId());
        if (newEntity.getType().getId().equals(BehaviorStatus.ALACAK.getId())) {
            motionSums.get().setReceive(motionSums.get().getReceive().add(newEntity.getFuelTl()));
        } else {
            motionSums.get().setLoan(motionSums.get().getLoan().add(newEntity.getFuelTl()));
        }
        motionSums.get().setBalance(motionSums.get().getLoan().subtract(motionSums.get().getReceive()));

        Optional<Customer> customer = customerRepository.findById(newEntity.getMotionsums().getCustomer().getId());
        if (newEntity.getType().getId().equals(BehaviorStatus.ALACAK.getId())) {
            customer.get().setReceive(customer.get().getReceive().add(newEntity.getFuelTl()));
        } else {
            customer.get().setLoan(customer.get().getLoan().add(newEntity.getFuelTl()));
        }
        customer.get().setBalance(customer.get().getLoan().subtract(customer.get().getReceive()));

        Optional<Customer> parent = customerRepository.findById(newEntity.getMotionsums().getParent().getId());
        if (newEntity.getType().getId().equals(BehaviorStatus.ALACAK.getId())) {
            parent.get().setReceive(parent.get().getReceive().add(newEntity.getFuelTl()));
        } else {
            parent.get().setLoan(parent.get().getLoan().add(newEntity.getFuelTl()));
        }
        parent.get().setBalance(parent.get().getLoan().subtract(parent.get().getReceive()));
        return newEntity;
    }

    @Override
    public Behavior beforeUpdate(@NotNull Behavior oldEntity, @NotNull Behavior newEntity) throws Exception {
        return newEntity;
    }
}
