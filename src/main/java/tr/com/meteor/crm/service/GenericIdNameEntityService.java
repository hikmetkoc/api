package tr.com.meteor.crm.service;

import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.IdNameEntity;
import tr.com.meteor.crm.repository.GenericIdNameEntityRepository;

import java.io.Serializable;

@Transactional(rollbackFor = Exception.class)
public abstract class GenericIdNameEntityService<TEntity extends IdNameEntity<TIdType>, TIdType extends Serializable, TRepository extends GenericIdNameEntityRepository<TEntity, TIdType>>
    extends GenericIdEntityService<TEntity, TIdType, TRepository> {

    public GenericIdNameEntityService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                      BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                      BaseConfigurationService baseConfigurationService, Class<TEntity> entityClass, TRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            entityClass, repository);
    }
}
