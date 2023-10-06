package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.domain.Iban;
import tr.com.meteor.crm.repository.AttributeValueRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.IbanRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class IbanService extends GenericIdNameAuditingEntityService<Iban, UUID, IbanRepository> {

    private final AttributeValueRepository attributeValueRepository;

    private final CustomerRepository customerRepository;
    public IbanService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                       BaseConfigurationService baseConfigurationService,
                       IbanRepository repository, AttributeValueRepository attributeValueRepository, CustomerRepository customerRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Iban.class, repository);
        this.attributeValueRepository = attributeValueRepository;
        this.customerRepository = customerRepository;
    }
    public void newIban(String bank, String moneyType, String customer, String name) throws Exception {
        AttributeValue attrBank = new AttributeValue();
        AttributeValue attrMoneyType = new AttributeValue();

        List<AttributeValue> attributeValues = attributeValueRepository.findAll();
        for (AttributeValue attributeValue : attributeValues) {
            if (attributeValue.getId().equals(bank)) attrBank = attributeValue;
            if (attributeValue.getId().equals(moneyType)) attrMoneyType = attributeValue;
        }

        if (repository.existsByName(name)) {
            throw new Exception("Bu iban zaten kayıtlıdır!");
        }
        UUID uuid = UUID.fromString(customer);
        Customer customer1 = customerRepository.findById(uuid).get();

        Iban iban = new Iban();
        iban.setBank(attrBank);
        iban.setMoneyType(attrMoneyType);
        iban.setName(name);
        iban.setCustomer(customer1);

        repository.save(iban);
    }
}
