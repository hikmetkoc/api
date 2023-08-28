package tr.com.meteor.crm.aop.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.trigger.TriggerInterface;
import tr.com.meteor.crm.utils.Utils;
import tr.com.meteor.crm.utils.metadata.EntityMetadataFull;
import tr.com.meteor.crm.utils.metadata.MetadataReader;

import java.io.Serializable;

@Aspect
@Component
public class TriggerAspect {
    private final BeanFactory beanFactory;

    public TriggerAspect(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Around("execution(* tr.com.meteor.crm.repository.GenericIdEntityRepository+.insert(..)) && args(tEntity)")
    public <TIdType extends Serializable, TEntity extends IdEntity<TIdType>> TEntity aroundInsert(ProceedingJoinPoint joinPoint, TEntity tEntity) throws Throwable {
        TriggerInterface<TEntity> trigger = getTrigger(Utils.extractHibernateProxy(tEntity));

        if (trigger != null) {
            trigger.beforeInsertAbs(tEntity);
        }

        tEntity = (TEntity) joinPoint.proceed();

        if (trigger != null) {
            trigger.afterInsertAbs(tEntity);
        }

        return tEntity;
    }

    @Around("execution(* tr.com.meteor.crm.repository.GenericIdEntityRepository+.save(..)) && args(tEntity)")
    public <TIdType extends Serializable, TEntity extends IdEntity<TIdType>> TEntity aroundSave(ProceedingJoinPoint joinPoint, TEntity tEntity) throws Throwable {
        TriggerInterface<TEntity> trigger = getTrigger(Utils.extractHibernateProxy(tEntity));

        if (trigger != null) {
            trigger.onClearCache(tEntity);
        }

        tEntity = (TEntity) joinPoint.proceed();

        return tEntity;
    }

    @Around("execution(* tr.com.meteor.crm.repository.GenericIdEntityRepository+.update(..)) && args(newEntity)")
    public <TEntity extends IdEntity> TEntity aroundUpdate(ProceedingJoinPoint joinPoint, TEntity newEntity) throws Throwable {
        TriggerInterface trigger = getTrigger(Utils.extractHibernateProxy(newEntity));

        if (trigger != null) {
            trigger.beforeUpdateAbs(newEntity);
        }

        newEntity = (TEntity) joinPoint.proceed();

        if (trigger != null) {
            trigger.afterUpdateAbs(newEntity);
        }

        return newEntity;
    }

    @Around("execution(* tr.com.meteor.crm.repository.GenericIdEntityRepository+.deleteSoft(..)) && args(tEntity)")
    public <TEntity extends IdEntity> void aroundDelete(ProceedingJoinPoint joinPoint, TEntity tEntity) throws Throwable {
        TriggerInterface trigger = getTrigger(Utils.extractHibernateProxy(tEntity));

        if (trigger != null) {
            trigger.beforeDeleteAbs(tEntity);
        }

        joinPoint.proceed();

        if (trigger != null) {
            trigger.afterDeleteAbs(tEntity);
        }
    }

    private <TIdType extends Serializable, TEntity extends IdEntity<TIdType>> TriggerInterface getTrigger(TEntity tEntity) {
        if (tEntity != null) {
            return getTrigger(tEntity.getClass());
        }

        return null;
    }

    private <TIdType extends Serializable, TEntity extends IdEntity<TIdType>> TriggerInterface<TEntity> getTrigger(Class<TEntity> tEntityClass) {
        EntityMetadataFull entityMetadataFull = MetadataReader.getClassMetadataList().getOrDefault(tEntityClass.getName(), null);
        if (entityMetadataFull != null && entityMetadataFull.getTriggerClass() != null) {
            return (TriggerInterface<TEntity>) beanFactory.getBean(entityMetadataFull.getTriggerClass().getSimpleName());
        }

        return null;
    }
}
