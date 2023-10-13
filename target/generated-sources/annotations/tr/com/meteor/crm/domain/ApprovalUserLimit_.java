package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ApprovalUserLimit.class)
public abstract class ApprovalUserLimit_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<ApprovalUserLimit, BigDecimal> euroLimit;
	public static volatile SingularAttribute<ApprovalUserLimit, String> name;
	public static volatile SingularAttribute<ApprovalUserLimit, BigDecimal> dlLimit;
	public static volatile SingularAttribute<ApprovalUserLimit, User> user;
	public static volatile SingularAttribute<ApprovalUserLimit, BigDecimal> tlLimit;

	public static final String EURO_LIMIT = "euroLimit";
	public static final String NAME = "name";
	public static final String DL_LIMIT = "dlLimit";
	public static final String USER = "user";
	public static final String TL_LIMIT = "tlLimit";

}

