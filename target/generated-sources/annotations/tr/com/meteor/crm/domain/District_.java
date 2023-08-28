package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(District.class)
public abstract class District_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<District, City> city;
	public static volatile SingularAttribute<District, String> instanceName;
	public static volatile SingularAttribute<District, String> name;

	public static final String CITY = "city";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";

}

