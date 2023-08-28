package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(HolManager.class)
public abstract class HolManager_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<HolManager, User> manager;
	public static volatile SingularAttribute<HolManager, String> instanceName;
	public static volatile SingularAttribute<HolManager, User> chief;
	public static volatile SingularAttribute<HolManager, User> director;
	public static volatile SingularAttribute<HolManager, String> name;
	public static volatile SingularAttribute<HolManager, User> user;

	public static final String MANAGER = "manager";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String CHIEF = "chief";
	public static final String DIRECTOR = "director";
	public static final String NAME = "name";
	public static final String USER = "user";

}

