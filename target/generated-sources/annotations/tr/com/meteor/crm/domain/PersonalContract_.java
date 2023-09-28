package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersonalContract.class)
public abstract class PersonalContract_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<PersonalContract, User> owner;
	public static volatile SingularAttribute<PersonalContract, String> belge;
	public static volatile SingularAttribute<PersonalContract, AttributeValue> sirket;
	public static volatile SingularAttribute<PersonalContract, String> instanceName;
	public static volatile SingularAttribute<PersonalContract, String> name;
	public static volatile SingularAttribute<PersonalContract, String> description;
	public static volatile SingularAttribute<PersonalContract, String> sozlesme;

	public static final String OWNER = "owner";
	public static final String BELGE = "belge";
	public static final String SIRKET = "sirket";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String SOZLESME = "sozlesme";

}

