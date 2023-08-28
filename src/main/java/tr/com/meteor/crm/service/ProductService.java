package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Product;
import tr.com.meteor.crm.repository.ProductRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductService extends GenericIdNameAuditingEntityService<Product, UUID, ProductRepository> {

    public ProductService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                          BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                          BaseConfigurationService baseConfigurationService,
                          ProductRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Product.class, repository);
    }
}
