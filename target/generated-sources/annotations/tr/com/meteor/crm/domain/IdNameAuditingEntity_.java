package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IdNameAuditingEntity.class)
public abstract class IdNameAuditingEntity_ extends tr.com.meteor.crm.domain.IdNameEntity_ {

	public static volatile SingularAttribute<IdNameAuditingEntity, Instant> createdDate;
	public static volatile SingularAttribute<IdNameAuditingEntity, User> createdBy;
	public static volatile SingularAttribute<IdNameAuditingEntity, Instant> lastModifiedDate;
	public static volatile SingularAttribute<IdNameAuditingEntity, User> lastModifiedBy;

	public static final String CREATED_DATE = "createdDate";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";

}

