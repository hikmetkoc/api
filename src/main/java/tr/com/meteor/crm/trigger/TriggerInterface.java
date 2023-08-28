package tr.com.meteor.crm.trigger;

import tr.com.meteor.crm.domain.IdEntity;

public interface TriggerInterface<TEntity extends IdEntity> {

    TEntity beforeInsertAbs(TEntity newEntity) throws Exception;

    TEntity afterInsertAbs(TEntity newEntity) throws Exception;

    TEntity beforeUpdateAbs(TEntity newEntity) throws Exception;

    TEntity afterUpdateAbs(TEntity newEntity) throws Exception;

    void beforeDeleteAbs(TEntity tEntity) throws Exception;

    void afterDeleteAbs(TEntity tEntity) throws Exception;

    default TEntity beforeInsert(TEntity newEntity) throws Exception {
        return newEntity;
    }

    default TEntity afterInsert(TEntity newEntity) throws Exception {
        return newEntity;
    }

    default TEntity beforeUpdate(TEntity oldEntity, TEntity newEntity) throws Exception {
        return newEntity;
    }

    default TEntity afterUpdate(TEntity oldEntity, TEntity newEntity) throws Exception {
        return newEntity;
    }

    default void beforeDelete(TEntity entity) throws Exception {
    }

    default void afterDelete(TEntity entity) throws Exception {
    }

    default void onClearCache(TEntity entity) {

    }
}
