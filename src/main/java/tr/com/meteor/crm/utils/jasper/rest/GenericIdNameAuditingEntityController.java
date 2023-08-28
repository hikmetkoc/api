package tr.com.meteor.crm.utils.jasper.rest;

import tr.com.meteor.crm.domain.IdNameAuditingEntity;
import tr.com.meteor.crm.repository.GenericIdNameAuditingEntityRepository;
import tr.com.meteor.crm.service.GenericIdNameAuditingEntityService;

import java.io.Serializable;

public abstract class GenericIdNameAuditingEntityController<TEntity extends IdNameAuditingEntity<TIdType>, TIdType extends Serializable, TRepository extends GenericIdNameAuditingEntityRepository<TEntity, TIdType>, TService extends GenericIdNameAuditingEntityService<TEntity, TIdType, TRepository>>
    extends GenericIdNameEntityController<TEntity, TIdType, TRepository, TService> {

    public GenericIdNameAuditingEntityController(TService service) {
        super(service);
    }
}
