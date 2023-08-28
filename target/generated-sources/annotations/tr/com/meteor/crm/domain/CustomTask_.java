package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomTask.class)
public abstract class CustomTask_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<CustomTask, User> owner;
	public static volatile SingularAttribute<CustomTask, String> instanceName;
	public static volatile SingularAttribute<CustomTask, AttributeValue> importance;
	public static volatile SingularAttribute<CustomTask, String> description;
	public static volatile SingularAttribute<CustomTask, Instant> dueTime;
	public static volatile SingularAttribute<CustomTask, AttributeValue> type;
	public static volatile SingularAttribute<CustomTask, Customer> customer;
	public static volatile SingularAttribute<CustomTask, String> subjectdesc;
	public static volatile SingularAttribute<CustomTask, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String IMPORTANCE = "importance";
	public static final String DESCRIPTION = "description";
	public static final String DUE_TIME = "dueTime";
	public static final String TYPE = "type";
	public static final String CUSTOMER = "customer";
	public static final String SUBJECTDESC = "subjectdesc";
	public static final String STATUS = "status";

}

