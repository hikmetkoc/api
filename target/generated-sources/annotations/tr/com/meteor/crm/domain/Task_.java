package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Task.class)
public abstract class Task_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Task, User> owner;
	public static volatile SingularAttribute<Task, AttributeValue> taskType;
	public static volatile SingularAttribute<Task, String> instanceName;
	public static volatile SingularAttribute<Task, AttributeValue> importance;
	public static volatile SingularAttribute<Task, User> assigner;
	public static volatile SingularAttribute<Task, String> description;
	public static volatile SingularAttribute<Task, AttributeValue> birim;
	public static volatile SingularAttribute<Task, Instant> dueTime;
	public static volatile SingularAttribute<Task, Instant> oktime;
	public static volatile SingularAttribute<Task, String> subjectdesc;
	public static volatile SingularAttribute<Task, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String TASK_TYPE = "taskType";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String IMPORTANCE = "importance";
	public static final String ASSIGNER = "assigner";
	public static final String DESCRIPTION = "description";
	public static final String BIRIM = "birim";
	public static final String DUE_TIME = "dueTime";
	public static final String OKTIME = "oktime";
	public static final String SUBJECTDESC = "subjectdesc";
	public static final String STATUS = "status";

}

