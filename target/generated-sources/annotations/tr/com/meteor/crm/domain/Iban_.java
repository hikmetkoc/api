package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Iban.class)
public abstract class Iban_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Iban, AttributeValue> bank;
	public static volatile SingularAttribute<Iban, String> instanceName;
	public static volatile SingularAttribute<Iban, String> name;
	public static volatile SingularAttribute<Iban, Customer> customer;

	public static final String BANK = "bank";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String CUSTOMER = "customer";

}

