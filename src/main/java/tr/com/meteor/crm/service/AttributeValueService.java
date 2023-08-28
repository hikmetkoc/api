package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.utils.jasper.rest.errors.ObjectAccessDeniedException;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.AttributeValueRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AttributeValueService extends GenericIdNameEntityService<AttributeValue, String, AttributeValueRepository> {

    public AttributeValueService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                 BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                 BaseConfigurationService baseConfigurationService,
                                 AttributeValueRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            AttributeValue.class, repository);
    }

    public List<AttributeValue> getAll() {
        return repository.findAll();
    }

    @Override
    public AttributeValue update(User user, AttributeValue attributeValue) throws Exception {
        Optional<AttributeValue> attributeValue1 = repository.findById(attributeValue.getId());

        if (attributeValue1.get().getIsStatic()) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.UPDATE, entityClass.getSimpleName());
        }

        return super.update(user, attributeValue);
    }

    @Override
    public AttributeValue add(User user, AttributeValue attributeValue) throws Exception {

        if (attributeValue.getIsStatic()) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.UPDATE, entityClass.getSimpleName());
        }

        return super.add(user, attributeValue);
    }

    @Override
    public void delete(User user, String id) throws Exception {
        Optional<AttributeValue> attributeValue = repository.findById(id);
        if (attributeValue.get().getIsStatic()) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.DELETE, entityClass.getSimpleName());
        }

        super.delete(user, id);
    }
}
