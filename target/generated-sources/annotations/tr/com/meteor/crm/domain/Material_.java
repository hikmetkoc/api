package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Material.class)
public abstract class Material_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Material, String> serialNumber;
	public static volatile SingularAttribute<Material, AttributeValue> sirket;
	public static volatile SingularAttribute<Material, String> instanceName;
	public static volatile SingularAttribute<Material, String> name;
	public static volatile SingularAttribute<Material, String> description;
	public static volatile SingularAttribute<Material, String> model;
	public static volatile SingularAttribute<Material, AttributeValue> type;
	public static volatile SingularAttribute<Material, String> ayirt;
	public static volatile SingularAttribute<Material, User> user;
	public static volatile SingularAttribute<Material, AttributeValue> brand;
	public static volatile SingularAttribute<Material, AttributeValue> status;

	public static final String SERIAL_NUMBER = "serialNumber";
	public static final String SIRKET = "sirket";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String MODEL = "model";
	public static final String TYPE = "type";
	public static final String AYIRT = "ayirt";
	public static final String USER = "user";
	public static final String BRAND = "brand";
	public static final String STATUS = "status";

}

