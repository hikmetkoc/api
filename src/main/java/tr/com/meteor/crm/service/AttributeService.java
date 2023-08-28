package tr.com.meteor.crm.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Attribute;
import tr.com.meteor.crm.repository.AttributeRepository;


@Service
@Transactional(rollbackFor = Exception.class)
public class AttributeService extends GenericIdNameEntityService<Attribute, String, AttributeRepository> {

    public AttributeService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                            BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                            BaseConfigurationService baseConfigurationService,
                            AttributeRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Attribute.class, repository);
    }
}
