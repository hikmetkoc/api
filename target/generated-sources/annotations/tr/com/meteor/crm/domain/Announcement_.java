package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Announcement.class)
public abstract class Announcement_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Announcement, String> instanceName;
	public static volatile SingularAttribute<Announcement, String> description;
	public static volatile SingularAttribute<Announcement, Boolean> active;
	public static volatile SingularAttribute<Announcement, String> title;
	public static volatile SingularAttribute<Announcement, AttributeValue> type;

	public static final String INSTANCE_NAME = "instanceName";
	public static final String DESCRIPTION = "description";
	public static final String ACTIVE = "active";
	public static final String TITLE = "title";
	public static final String TYPE = "type";

}

