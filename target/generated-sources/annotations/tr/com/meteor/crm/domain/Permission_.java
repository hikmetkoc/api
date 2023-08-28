package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Permission.class)
public abstract class Permission_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Permission, Role> role;
	public static volatile SingularAttribute<Permission, Boolean> read;
	public static volatile SingularAttribute<Permission, String> instanceName;
	public static volatile SingularAttribute<Permission, String> objectName;
	public static volatile SingularAttribute<Permission, Boolean> update;
	public static volatile SingularAttribute<Permission, Boolean> delete;
	public static volatile SingularAttribute<Permission, Boolean> isHierarchical;

	public static final String ROLE = "role";
	public static final String READ = "read";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String OBJECT_NAME = "objectName";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	public static final String IS_HIERARCHICAL = "isHierarchical";

}

