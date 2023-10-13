package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.CompanyMaterial;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.repository.CompanyMaterialRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyMaterialService extends GenericIdNameAuditingEntityService<CompanyMaterial, UUID, CompanyMaterialRepository> {

    public CompanyMaterialService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                  BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                  BaseConfigurationService baseConfigurationService,
                                  CompanyMaterialRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            CompanyMaterial.class, repository);
    }
}
