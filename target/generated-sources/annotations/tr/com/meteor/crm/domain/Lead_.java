package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Lead.class)
public abstract class Lead_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Lead, User> owner;
	public static volatile SingularAttribute<Lead, String> phone;
	public static volatile SingularAttribute<Lead, String> instanceName;
	public static volatile SingularAttribute<Lead, String> name;
	public static volatile SingularAttribute<Lead, String> description;
	public static volatile SingularAttribute<Lead, Customer> customer;
	public static volatile SingularAttribute<Lead, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String PHONE = "phone";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CUSTOMER = "customer";
	public static final String STATUS = "status";

}

