package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.Contact;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.repository.ContactRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(AddressTrigger.QUALIFIER)
public class AddressTrigger extends Trigger<Address, UUID, AddressRepository> {
    final static String QUALIFIER = "AddressTrigger";

    private final CustomerRepository customerRepository;

    public AddressTrigger(CacheManager cacheManager, AddressRepository addressRepository, CustomerRepository customerRepository,
                          BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, Address.class, AddressTrigger.class, addressRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
    }

    @Override
    public Address beforeInsert(@NotNull Address newEntity) throws Exception {
        Optional<Customer> customer = customerRepository.findById(newEntity.getCustomer().getId());
        if (customer.isPresent()) {
            newEntity.setCustomer(customer.get());
        }
        return newEntity;
    }

    @Override
    public Address beforeUpdate(@NotNull Address oldEntity, @NotNull Address newEntity) throws Exception {
        Optional<Customer> customer = customerRepository.findById(newEntity.getCustomer().getId());
        if (customer.isPresent()) {
            newEntity.setCustomer(customer.get());
        }
        return newEntity;
    }


}
