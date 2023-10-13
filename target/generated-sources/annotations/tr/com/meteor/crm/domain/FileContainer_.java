package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FileContainer.class)
public abstract class FileContainer_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<FileContainer, User> owner;
	public static volatile SingularAttribute<FileContainer, String> code;
	public static volatile SingularAttribute<FileContainer, String> name;
	public static volatile SingularAttribute<FileContainer, String> locName;
	public static volatile SingularAttribute<FileContainer, String> location;

	public static final String OWNER = "owner";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String LOC_NAME = "locName";
	public static final String LOCATION = "location";

}

