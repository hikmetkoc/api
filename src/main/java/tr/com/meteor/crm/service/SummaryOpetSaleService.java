package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.SummaryOpetSale;
import tr.com.meteor.crm.repository.SummaryOpetSaleRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class SummaryOpetSaleService extends GenericIdEntityService<SummaryOpetSale, UUID, SummaryOpetSaleRepository> {

    public SummaryOpetSaleService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                  BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                  BaseConfigurationService baseConfigurationService,
                                  SummaryOpetSaleRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            SummaryOpetSale.class, repository);
    }
}
