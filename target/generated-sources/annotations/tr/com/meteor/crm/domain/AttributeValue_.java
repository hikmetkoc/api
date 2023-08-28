package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AttributeValue.class)
public abstract class AttributeValue_ extends tr.com.meteor.crm.domain.IdNameEntity_ {

	public static volatile SingularAttribute<AttributeValue, Boolean> isStatic;
	public static volatile SingularAttribute<AttributeValue, String> instanceName;
	public static volatile SingularAttribute<AttributeValue, Integer> weight;
	public static volatile SingularAttribute<AttributeValue, String> label;
	public static volatile SingularAttribute<AttributeValue, Attribute> attribute;
	public static volatile SingularAttribute<AttributeValue, String> ilgilibirim;

	public static final String IS_STATIC = "isStatic";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String WEIGHT = "weight";
	public static final String LABEL = "label";
	public static final String ATTRIBUTE = "attribute";
	public static final String ILGILIBIRIM = "ilgilibirim";

}

