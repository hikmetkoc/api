package tr.com.meteor.crm.aop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class SoftDeleteAspect {
    public static final String SOFT_DELETE_FILTER = "softDeleteFilter";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager em;

    @Before("this(org.springframework.data.repository.Repository) && target(repository)")
    public void applySoftDeleteFilter(JoinPoint pjp, JpaRepository repository) {
        try {
            org.hibernate.Filter filter = em.unwrap(Session.class).enableFilter(SOFT_DELETE_FILTER);
            log.info("Soft Delete Filter Enabled");
            filter.validate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
