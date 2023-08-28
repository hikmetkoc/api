package tr.com.meteor.crm.service;

import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.IdNameAuditingEntity;
import tr.com.meteor.crm.repository.GenericIdNameAuditingEntityRepository;

import java.io.Serializable;

@Transactional(rollbackFor = Exception.class)
public abstract class GenericIdNameAuditingEntityService<TEntity extends IdNameAuditingEntity<TIdType>, TIdType extends Serializable, TRepository extends GenericIdNameAuditingEntityRepository<TEntity, TIdType>>
    extends GenericIdNameEntityService<TEntity, TIdType, TRepository> {

    public GenericIdNameAuditingEntityService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                              BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                              BaseConfigurationService baseConfigurationService,
                                              Class<TEntity> entityClass, TRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            entityClass, repository);
    }
}
