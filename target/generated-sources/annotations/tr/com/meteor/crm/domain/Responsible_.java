package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Responsible.class)
public abstract class Responsible_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Responsible, User> owner;
	public static volatile SingularAttribute<Responsible, String> instanceName;
	public static volatile SingularAttribute<Responsible, AttributeValue> oncelik;
	public static volatile SingularAttribute<Responsible, User> assigner;
	public static volatile SingularAttribute<Responsible, String> description;
	public static volatile SingularAttribute<Responsible, Customer> customer;

	public static final String OWNER = "owner";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String ONCELIK = "oncelik";
	public static final String ASSIGNER = "assigner";
	public static final String DESCRIPTION = "description";
	public static final String CUSTOMER = "customer";

}

