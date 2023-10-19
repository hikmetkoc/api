package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MotionSums.class)
public abstract class MotionSums_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<MotionSums, User> owner;
	public static volatile SingularAttribute<MotionSums, Customer> parent;
	public static volatile SingularAttribute<MotionSums, BigDecimal> receive;
	public static volatile SingularAttribute<MotionSums, BigDecimal> loan;
	public static volatile SingularAttribute<MotionSums, AttributeValue> cost;
	public static volatile SingularAttribute<MotionSums, BigDecimal> balance;
	public static volatile SingularAttribute<MotionSums, String> instanceName;
	public static volatile SingularAttribute<MotionSums, String> description;
	public static volatile SingularAttribute<MotionSums, Customer> customer;

	public static final String OWNER = "owner";
	public static final String PARENT = "parent";
	public static final String RECEIVE = "receive";
	public static final String LOAN = "loan";
	public static final String COST = "cost";
	public static final String BALANCE = "balance";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String DESCRIPTION = "description";
	public static final String CUSTOMER = "customer";

}

