package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Contact.class)
public abstract class Contact_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Contact, String> firstName;
	public static volatile SingularAttribute<Contact, String> lastName;
	public static volatile SingularAttribute<Contact, String> phone;
	public static volatile SingularAttribute<Contact, String> instanceName;
	public static volatile SingularAttribute<Contact, Boolean> active;
	public static volatile SingularAttribute<Contact, String> description;
	public static volatile SingularAttribute<Contact, String> email;
	public static volatile SingularAttribute<Contact, Customer> customer;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PHONE = "phone";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String ACTIVE = "active";
	public static final String DESCRIPTION = "description";
	public static final String EMAIL = "email";
	public static final String CUSTOMER = "customer";

}

