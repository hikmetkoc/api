package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Project.class)
public abstract class Project_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Project, User> owner;
	public static volatile SingularAttribute<Project, Instant> endDate;
	public static volatile SingularAttribute<Project, String> instanceName;
	public static volatile SingularAttribute<Project, String> subject;
	public static volatile SingularAttribute<Project, AttributeValue> importance;
	public static volatile SingularAttribute<Project, String> description;
	public static volatile SingularAttribute<Project, AttributeValue> birim;
	public static volatile SingularAttribute<Project, Instant> okTime;
	public static volatile SingularAttribute<Project, Instant> startDate;
	public static volatile SingularAttribute<Project, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String END_DATE = "endDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String SUBJECT = "subject";
	public static final String IMPORTANCE = "importance";
	public static final String DESCRIPTION = "description";
	public static final String BIRIM = "birim";
	public static final String OK_TIME = "okTime";
	public static final String START_DATE = "startDate";
	public static final String STATUS = "status";

}

