package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserAcceptance.class)
public abstract class UserAcceptance_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<UserAcceptance, Material> material;
	public static volatile SingularAttribute<UserAcceptance, String> instanceName;
	public static volatile SingularAttribute<UserAcceptance, String> name;
	public static volatile SingularAttribute<UserAcceptance, User> assigner;
	public static volatile SingularAttribute<UserAcceptance, Instant> submitDate;
	public static volatile SingularAttribute<UserAcceptance, Instant> receiveDate;
	public static volatile SingularAttribute<UserAcceptance, User> user;

	public static final String MATERIAL = "material";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String ASSIGNER = "assigner";
	public static final String SUBMIT_DATE = "submitDate";
	public static final String RECEIVE_DATE = "receiveDate";
	public static final String USER = "user";

}

