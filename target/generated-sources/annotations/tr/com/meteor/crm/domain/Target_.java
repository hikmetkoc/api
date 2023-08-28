package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Target.class)
public abstract class Target_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Target, User> owner;
	public static volatile SingularAttribute<Target, Instant> termStart;
	public static volatile SingularAttribute<Target, Double> amount;
	public static volatile SingularAttribute<Target, String> instanceName;
	public static volatile SingularAttribute<Target, Double> realizedAmount;
	public static volatile SingularAttribute<Target, AttributeValue> type;

	public static final String OWNER = "owner";
	public static final String TERM_START = "termStart";
	public static final String AMOUNT = "amount";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String REALIZED_AMOUNT = "realizedAmount";
	public static final String TYPE = "type";

}

