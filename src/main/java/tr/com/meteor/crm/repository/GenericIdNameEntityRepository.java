package tr.com.meteor.crm.repository;

import org.springframework.data.repository.NoRepositoryBean;
import tr.com.meteor.crm.domain.IdNameEntity;

import java.io.Serializable;

@NoRepositoryBean
public interface GenericIdNameEntityRepository<TEntity extends IdNameEntity<TIdType>, TIdType extends Serializable>
    extends GenericIdEntityRepository<TEntity, TIdType> {

}
