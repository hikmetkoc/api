package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Configuration.class)
public abstract class Configuration_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Configuration, String> instanceName;
	public static volatile SingularAttribute<Configuration, AttributeValue> valueType;
	public static volatile SingularAttribute<Configuration, String> storedValue;
	public static volatile SingularAttribute<Configuration, String> name;

	public static final String INSTANCE_NAME = "instanceName";
	public static final String VALUE_TYPE = "valueType";
	public static final String STORED_VALUE = "storedValue";
	public static final String NAME = "name";

}

