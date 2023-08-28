package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Report.class)
public abstract class Report_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Report, String> mails;
	public static volatile SingularAttribute<Report, String> cronString;
	public static volatile SingularAttribute<Report, String> instanceName;
	public static volatile SingularAttribute<Report, String> name;
	public static volatile SingularAttribute<Report, String> queryJson;
	public static volatile SingularAttribute<Report, String> objectName;
	public static volatile SingularAttribute<Report, String> description;

	public static final String MAILS = "mails";
	public static final String CRON_STRING = "cronString";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String QUERY_JSON = "queryJson";
	public static final String OBJECT_NAME = "objectName";
	public static final String DESCRIPTION = "description";

}

