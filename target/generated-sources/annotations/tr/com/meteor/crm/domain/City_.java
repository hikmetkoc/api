package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(City.class)
public abstract class City_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<City, Country> country;
	public static volatile SingularAttribute<City, String> instanceName;
	public static volatile SingularAttribute<City, String> name;

	public static final String COUNTRY = "country";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";

}

