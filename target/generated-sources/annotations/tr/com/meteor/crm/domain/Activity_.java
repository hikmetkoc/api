package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Activity.class)
public abstract class Activity_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Activity, User> owner;
	public static volatile SingularAttribute<Activity, Task> task;
	public static volatile SingularAttribute<Activity, String> subjdesc;
	public static volatile SingularAttribute<Activity, Instant> checkOutTime;
	public static volatile SingularAttribute<Activity, String> instanceName;
	public static volatile SingularAttribute<Activity, String> subject;
	public static volatile SingularAttribute<Activity, String> description;
	public static volatile SingularAttribute<Activity, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String TASK = "task";
	public static final String SUBJDESC = "subjdesc";
	public static final String CHECK_OUT_TIME = "checkOutTime";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String SUBJECT = "subject";
	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";

}

