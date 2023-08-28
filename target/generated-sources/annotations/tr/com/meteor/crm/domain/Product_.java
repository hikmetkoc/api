package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Product, Product> parent;
	public static volatile SingularAttribute<Product, AttributeValue> unitOfMeasure;
	public static volatile SingularAttribute<Product, String> instanceName;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, String> description;

	public static final String PARENT = "parent";
	public static final String UNIT_OF_MEASURE = "unitOfMeasure";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";

}

