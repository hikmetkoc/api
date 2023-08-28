package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Customer.class)
public abstract class Customer_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Customer, User> owner;
	public static volatile SingularAttribute<Customer, String> website;
	public static volatile ListAttribute<Customer, Address> addresses;
	public static volatile SingularAttribute<Customer, String> commercialTitle;
	public static volatile SingularAttribute<Customer, String> instanceName;
	public static volatile SingularAttribute<Customer, String> description;
	public static volatile SingularAttribute<Customer, String> taxNumber;
	public static volatile SingularAttribute<Customer, String> phone;
	public static volatile SingularAttribute<Customer, String> name;
	public static volatile SingularAttribute<Customer, String> taxOffice;
	public static volatile SingularAttribute<Customer, AttributeValue> sector;
	public static volatile SingularAttribute<Customer, String> email;
	public static volatile SingularAttribute<Customer, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String WEBSITE = "website";
	public static final String ADDRESSES = "addresses";
	public static final String COMMERCIAL_TITLE = "commercialTitle";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String DESCRIPTION = "description";
	public static final String TAX_NUMBER = "taxNumber";
	public static final String PHONE = "phone";
	public static final String NAME = "name";
	public static final String TAX_OFFICE = "taxOffice";
	public static final String SECTOR = "sector";
	public static final String EMAIL = "email";
	public static final String STATUS = "status";

}

