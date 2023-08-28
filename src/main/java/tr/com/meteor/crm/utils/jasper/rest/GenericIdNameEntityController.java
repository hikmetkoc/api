package tr.com.meteor.crm.utils.jasper.rest;

import tr.com.meteor.crm.domain.IdNameEntity;
import tr.com.meteor.crm.repository.GenericIdNameEntityRepository;
import tr.com.meteor.crm.service.GenericIdNameEntityService;

import java.io.Serializable;

public abstract class GenericIdNameEntityController<TEntity extends IdNameEntity<TIdType>, TIdType extends Serializable, TRepository extends GenericIdNameEntityRepository<TEntity, TIdType>, TService extends GenericIdNameEntityService<TEntity, TIdType, TRepository>>
    extends GenericIdEntityController<TEntity, TIdType, TRepository, TService> {

    public GenericIdNameEntityController(TService service) {
        super(service);
    }
}
