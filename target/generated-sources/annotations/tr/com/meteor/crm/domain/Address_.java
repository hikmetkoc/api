package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Address.class)
public abstract class Address_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Address, Country> country;
	public static volatile SingularAttribute<Address, City> city;
	public static volatile SingularAttribute<Address, String> instanceName;
	public static volatile SingularAttribute<Address, District> district;
	public static volatile SingularAttribute<Address, Double> latitude;
	public static volatile SingularAttribute<Address, String> name;
	public static volatile SingularAttribute<Address, String> detail;
	public static volatile SingularAttribute<Address, Double> longitude;
	public static volatile SingularAttribute<Address, Customer> customer;

	public static final String COUNTRY = "country";
	public static final String CITY = "city";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String DISTRICT = "district";
	public static final String LATITUDE = "latitude";
	public static final String NAME = "name";
	public static final String DETAIL = "detail";
	public static final String LONGITUDE = "longitude";
	public static final String CUSTOMER = "customer";

}

