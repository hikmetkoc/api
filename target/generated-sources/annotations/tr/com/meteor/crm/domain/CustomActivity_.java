package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomActivity.class)
public abstract class CustomActivity_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<CustomActivity, User> owner;
	public static volatile SingularAttribute<CustomActivity, CustomTask> customtask;
	public static volatile SingularAttribute<CustomActivity, String> subjdesc;
	public static volatile SingularAttribute<CustomActivity, Instant> checkOutTime;
	public static volatile SingularAttribute<CustomActivity, String> instanceName;
	public static volatile SingularAttribute<CustomActivity, String> subject;
	public static volatile SingularAttribute<CustomActivity, String> description;
	public static volatile SingularAttribute<CustomActivity, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String CUSTOMTASK = "customtask";
	public static final String SUBJDESC = "subjdesc";
	public static final String CHECK_OUT_TIME = "checkOutTime";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String SUBJECT = "subject";
	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";

}

