package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Quote.class)
public abstract class Quote_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Quote, User> owner;
	public static volatile SingularAttribute<Quote, String> instanceName;
	public static volatile SingularAttribute<Quote, String> name;
	public static volatile SingularAttribute<Quote, User> assigner;
	public static volatile SingularAttribute<Quote, String> description;

	public static final String OWNER = "owner";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String ASSIGNER = "assigner";
	public static final String DESCRIPTION = "description";

}

