package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Contact;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.domain.PersonalContract;
import tr.com.meteor.crm.repository.ContactRepository;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.PersonalContractRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Component(PersonalContractTrigger.QUALIFIER)
public class PersonalContractTrigger extends Trigger<PersonalContract, UUID, PersonalContractRepository> {
    final static String QUALIFIER = "PersonalContractTrigger";

    private final CustomerRepository customerRepository;

    public PersonalContractTrigger(CacheManager cacheManager, PersonalContractRepository contactRepository, CustomerRepository customerRepository,
                                   BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, PersonalContract.class, PersonalContractTrigger.class, contactRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
    }

    @Override
    public PersonalContract beforeInsert(@NotNull PersonalContract newEntity) throws Exception {
        return newEntity;
    }

    @Override
    public PersonalContract beforeUpdate(@NotNull PersonalContract oldEntity, @NotNull PersonalContract newEntity) throws Exception {
        return newEntity;
    }


}
