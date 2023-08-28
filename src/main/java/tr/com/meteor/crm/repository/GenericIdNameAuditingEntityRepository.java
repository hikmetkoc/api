package tr.com.meteor.crm.repository;

import org.springframework.data.repository.NoRepositoryBean;
import tr.com.meteor.crm.domain.IdNameAuditingEntity;

import java.io.Serializable;

@NoRepositoryBean
public interface GenericIdNameAuditingEntityRepository<TEntity extends IdNameAuditingEntity<TIdType>, TIdType extends Serializable>
    extends GenericIdNameEntityRepository<TEntity, TIdType> {

}
