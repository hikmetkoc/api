package tr.com.meteor.crm.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.security.SecurityUtils;
import tr.com.meteor.crm.utils.Utils;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

@NoRepositoryBean
public interface GenericIdEntityRepository<TEntity extends IdEntity<TIdType>, TIdType extends Serializable>
    extends JpaRepository<TEntity, TIdType>, JpaSpecificationExecutor<TEntity> {
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    default void deleteSoft(TEntity tEntity) {
        tEntity.setDeletedDate(Instant.now());
        tEntity.setDeletedBy(SecurityUtils.getCurrentUserReference().orElse(null));
        save(tEntity);
    }

    default TEntity update(TEntity newEntity) throws Exception {
        return save(newEntity);
    }

    default TEntity insert(TEntity newEntity) throws Exception {
        return save(newEntity);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    default String getSerializedOldEntity(TEntity entity) throws JsonProcessingException {
        Optional<TEntity> oldEntity = findById(entity.getId());

        if (oldEntity.isPresent()) {
            return objectMapper.writeValueAsString(Utils.extractHibernateProxy(oldEntity.get()));
        } else {
            return null;
        }
    }
}
