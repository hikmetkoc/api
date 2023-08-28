package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VocationDay.class)
public abstract class VocationDay_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<VocationDay, User> owner;
	public static volatile SingularAttribute<VocationDay, Instant> holEnd;
	public static volatile SingularAttribute<VocationDay, String> instanceName;
	public static volatile SingularAttribute<VocationDay, String> name;
	public static volatile SingularAttribute<VocationDay, String> description;
	public static volatile SingularAttribute<VocationDay, Instant> holStart;

	public static final String OWNER = "owner";
	public static final String HOL_END = "holEnd";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String HOL_START = "holStart";

}

