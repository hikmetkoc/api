package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.OpetSale;
import tr.com.meteor.crm.repository.OpetSaleRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class OpetSaleService extends GenericIdEntityService<OpetSale, UUID, OpetSaleRepository> {

    public OpetSaleService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService,
                           OpetSaleRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            OpetSale.class, repository);
    }
}
