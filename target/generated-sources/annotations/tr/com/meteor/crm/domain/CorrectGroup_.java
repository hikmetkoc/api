package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CorrectGroup.class)
public abstract class CorrectGroup_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<CorrectGroup, AttributeValue> approvalGroup;
	public static volatile SingularAttribute<CorrectGroup, User> manager;
	public static volatile SingularAttribute<CorrectGroup, User> chief;
	public static volatile SingularAttribute<CorrectGroup, User> director;
	public static volatile SingularAttribute<CorrectGroup, String> name;

	public static final String APPROVAL_GROUP = "approvalGroup";
	public static final String MANAGER = "manager";
	public static final String CHIEF = "chief";
	public static final String DIRECTOR = "director";
	public static final String NAME = "name";

}

