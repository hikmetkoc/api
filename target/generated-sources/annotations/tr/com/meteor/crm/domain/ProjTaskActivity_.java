package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProjTaskActivity.class)
public abstract class ProjTaskActivity_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<ProjTaskActivity, Instant> endDate;
	public static volatile SingularAttribute<ProjTaskActivity, String> instanceName;
	public static volatile SingularAttribute<ProjTaskActivity, String> subject;
	public static volatile SingularAttribute<ProjTaskActivity, AttributeValue> importance;
	public static volatile SingularAttribute<ProjTaskActivity, String> description;
	public static volatile SingularAttribute<ProjTaskActivity, Instant> okTime;
	public static volatile SingularAttribute<ProjTaskActivity, ProjTask> projtask;
	public static volatile SingularAttribute<ProjTaskActivity, Instant> startDate;
	public static volatile SingularAttribute<ProjTaskActivity, AttributeValue> status;

	public static final String END_DATE = "endDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String SUBJECT = "subject";
	public static final String IMPORTANCE = "importance";
	public static final String DESCRIPTION = "description";
	public static final String OK_TIME = "okTime";
	public static final String PROJTASK = "projtask";
	public static final String START_DATE = "startDate";
	public static final String STATUS = "status";

}

