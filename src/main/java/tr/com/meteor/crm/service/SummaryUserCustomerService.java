package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.SummaryUserCustomer;
import tr.com.meteor.crm.repository.SummaryUserCustomerRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class SummaryUserCustomerService extends GenericIdEntityService<SummaryUserCustomer, UUID, SummaryUserCustomerRepository> {

    public SummaryUserCustomerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                      BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                      BaseConfigurationService baseConfigurationService,
                                      SummaryUserCustomerRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            SummaryUserCustomer.class, repository);
    }
}
