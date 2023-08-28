package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Contract;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.QuoteRepository;
import tr.com.meteor.crm.utils.attributevalues.ContactType;
import tr.com.meteor.crm.domain.Contact;
import tr.com.meteor.crm.repository.ContactRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component(ContactTrigger.QUALIFIER)
public class ContactTrigger extends Trigger<Contact, UUID, ContactRepository> {
    final static String QUALIFIER = "ContactTrigger";

    private final CustomerRepository customerRepository;

    public ContactTrigger(CacheManager cacheManager, ContactRepository contactRepository,CustomerRepository customerRepository,
                          BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, Contact.class, ContactTrigger.class, contactRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
    }

    @Override
    public Contact beforeInsert(@NotNull Contact newEntity) throws Exception {
        Optional<Customer> customer = customerRepository.findById(newEntity.getCustomer().getId());
        if (customer.isPresent()) {
            newEntity.setCustomer(customer.get());
        }
        return newEntity;
    }

    @Override
    public Contact beforeUpdate(@NotNull Contact oldEntity, @NotNull Contact newEntity) throws Exception {
        Optional<Customer> customer = customerRepository.findById(newEntity.getCustomer().getId());
        if (customer.isPresent()) {
            newEntity.setCustomer(customer.get());
        }
        return newEntity;
    }


}
