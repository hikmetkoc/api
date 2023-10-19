package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProjTaskOfficer.class)
public abstract class ProjTaskOfficer_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<ProjTaskOfficer, User> owner;
	public static volatile SingularAttribute<ProjTaskOfficer, String> instanceName;
	public static volatile SingularAttribute<ProjTaskOfficer, AttributeValue> authority;
	public static volatile SingularAttribute<ProjTaskOfficer, String> description;
	public static volatile SingularAttribute<ProjTaskOfficer, ProjTask> projtask;

	public static final String OWNER = "owner";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String AUTHORITY = "authority";
	public static final String DESCRIPTION = "description";
	public static final String PROJTASK = "projtask";

}

