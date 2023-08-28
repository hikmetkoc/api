package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.domain.Iban;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.IbanRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(IbanTrigger.QUALIFIER)
public class IbanTrigger extends Trigger<Iban, UUID, IbanRepository> {
    final static String QUALIFIER = "IbanTrigger";

    private final CustomerRepository customerRepository;

    public IbanTrigger(CacheManager cacheManager, IbanRepository ibanRepository, CustomerRepository customerRepository,
                       BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, Iban.class, IbanTrigger.class, ibanRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
    }

    @Override
    public Iban beforeInsert(@NotNull Iban newEntity) throws Exception {
        if (newEntity.getCustomer() == null) {
            throw new Exception("Tedarikçi YOK");
        }
        Optional<Customer> customer = customerRepository.findById(newEntity.getCustomer().getId());
        if (customer.isPresent()) {
            newEntity.setCustomer(customer.get());
        }
        return newEntity;
    }

    @Override
    public Iban beforeUpdate(@NotNull Iban oldEntity, @NotNull Iban newEntity) throws Exception {
        Optional<Customer> customer = customerRepository.findById(newEntity.getCustomer().getId());
        if (customer.isPresent()) {
            newEntity.setCustomer(customer.get());
        }
        return newEntity;
    }


}
