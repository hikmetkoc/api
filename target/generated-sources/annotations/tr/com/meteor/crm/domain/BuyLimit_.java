package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BuyLimit.class)
public abstract class BuyLimit_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<BuyLimit, User> manager;
	public static volatile SingularAttribute<BuyLimit, BigDecimal> chiefTl;
	public static volatile SingularAttribute<BuyLimit, String> instanceName;
	public static volatile SingularAttribute<BuyLimit, User> chief;
	public static volatile SingularAttribute<BuyLimit, User> director;
	public static volatile SingularAttribute<BuyLimit, BigDecimal> managerTl;
	public static volatile SingularAttribute<BuyLimit, AttributeValue> maliyet;
	public static volatile SingularAttribute<BuyLimit, BigDecimal> directorTl;
	public static volatile SingularAttribute<BuyLimit, BigDecimal> userTl;
	public static volatile SingularAttribute<BuyLimit, BigDecimal> managerDl;
	public static volatile SingularAttribute<BuyLimit, BigDecimal> directorDl;
	public static volatile SingularAttribute<BuyLimit, String> name;
	public static volatile SingularAttribute<BuyLimit, BigDecimal> userDl;
	public static volatile SingularAttribute<BuyLimit, BigDecimal> chiefDl;
	public static volatile SingularAttribute<BuyLimit, User> user;

	public static final String MANAGER = "manager";
	public static final String CHIEF_TL = "chiefTl";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String CHIEF = "chief";
	public static final String DIRECTOR = "director";
	public static final String MANAGER_TL = "managerTl";
	public static final String MALIYET = "maliyet";
	public static final String DIRECTOR_TL = "directorTl";
	public static final String USER_TL = "userTl";
	public static final String MANAGER_DL = "managerDl";
	public static final String DIRECTOR_DL = "directorDl";
	public static final String NAME = "name";
	public static final String USER_DL = "userDl";
	public static final String CHIEF_DL = "chiefDl";
	public static final String USER = "user";

}

