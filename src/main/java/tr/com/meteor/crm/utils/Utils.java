package tr.com.meteor.crm.utils;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.domain.AttributeValue;

public final class Utils {
    public static boolean isChanged(AttributeValue oldValue, AttributeValue newValue) {
        return (oldValue == null && newValue != null)
            || (oldValue != null && newValue == null)
            || (oldValue != null && !oldValue.getId().equals(newValue.getId()));
    }

    public static boolean isChanged(IdEntity oldValue, IdEntity newValue) {
        return (oldValue == null && newValue != null)
            || (oldValue != null && newValue == null)
            || (oldValue != null && !oldValue.getId().equals(newValue.getId()));
    }

    public static boolean isChanged(Integer oldValue, Integer newValue) {
        return (oldValue == null && newValue != null)
            || (oldValue != null && newValue == null)
            || (oldValue != null && !oldValue.equals(newValue));
    }

    public static <T> T extractHibernateProxy(T entity) {
        if (entity == null) {
            throw new
                NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                .getImplementation();
        }
        return entity;
    }
}
