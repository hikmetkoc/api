package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProjOfficer.class)
public abstract class ProjOfficer_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<ProjOfficer, User> owner;
	public static volatile SingularAttribute<ProjOfficer, String> instanceName;
	public static volatile SingularAttribute<ProjOfficer, AttributeValue> authority;
	public static volatile SingularAttribute<ProjOfficer, String> description;
	public static volatile SingularAttribute<ProjOfficer, Project> project;

	public static final String OWNER = "owner";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String AUTHORITY = "authority";
	public static final String DESCRIPTION = "description";
	public static final String PROJECT = "project";

}

