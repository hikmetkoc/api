package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProjTask.class)
public abstract class ProjTask_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<ProjTask, Instant> endDate;
	public static volatile SingularAttribute<ProjTask, String> instanceName;
	public static volatile SingularAttribute<ProjTask, String> subject;
	public static volatile SingularAttribute<ProjTask, AttributeValue> importance;
	public static volatile SingularAttribute<ProjTask, Project> project;
	public static volatile SingularAttribute<ProjTask, String> description;
	public static volatile SingularAttribute<ProjTask, Instant> okTime;
	public static volatile SingularAttribute<ProjTask, Instant> startDate;
	public static volatile SingularAttribute<ProjTask, AttributeValue> status;

	public static final String END_DATE = "endDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String SUBJECT = "subject";
	public static final String IMPORTANCE = "importance";
	public static final String PROJECT = "project";
	public static final String DESCRIPTION = "description";
	public static final String OK_TIME = "okTime";
	public static final String START_DATE = "startDate";
	public static final String STATUS = "status";

}

