package tr.com.meteor.crm.utils.idgenerator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import tr.com.meteor.crm.domain.IdEntity;

import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Logger;

public class GenericIdGenerator implements IdentifierGenerator {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getCanonicalName());

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        System.out.println();
        if (o instanceof IdEntity && ((IdEntity) o).getId() == null) {
            IdType idType = o.getClass().getAnnotation(IdType.class);

            if (idType == null) {
                LOGGER.info("IdType annotation not found on entity: " + o.getClass().getName());
            } else {
                LOGGER.info(MessageFormat.format("Entity: {0}, IdType: {1}, Sequence Name: {2}",
                    o.getClass().getName(), idType.idType(), idType.sequenceName()));
            }

            switch (idType.idType()) {
                case Long:
                    Query q = sharedSessionContractImplementor.getPersistenceContext()
                        .getSession().createNativeQuery("select nextval('" + idType.sequenceName() + "')");

                    Long generatedId = ((BigInteger) q.getSingleResult()).longValue();

                    LOGGER.info(MessageFormat.format("Sequence Name: {0}, Generated Id: {1}",
                        idType.sequenceName(), generatedId));

                    return generatedId;
                case Integer:
                case String:
                    return null;
                case UUID:
                    return UUID.randomUUID();
            }
        } else if(o instanceof IdEntity && ((IdEntity) o).getId() != null) {
            LOGGER.info(MessageFormat.format("Entity already have an Id. Returning the existing id -> {0}.",
                ((IdEntity) o).getId()));
            return ((IdEntity) o).getId();
        } else {
            LOGGER.info(MessageFormat.format("Not supported entity. Entity must be inherited from {0}",
                IdEntity.class.getName()));
        }

        return null;
    }
}
