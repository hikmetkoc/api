package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Operation.class)
public abstract class Operation_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Operation, Operation> parent;
	public static volatile SingularAttribute<Operation, String> instanceName;
	public static volatile SingularAttribute<Operation, String> name;

	public static final String PARENT = "parent";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";

}

