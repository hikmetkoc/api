package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Store.class)
public abstract class Store_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Store, String> stcode;
	public static volatile SingularAttribute<Store, User> owner;
	public static volatile SingularAttribute<Store, String> request;
	public static volatile SingularAttribute<Store, AttributeValue> sirket;
	public static volatile SingularAttribute<Store, Instant> endDate;
	public static volatile SingularAttribute<Store, String> instanceName;
	public static volatile SingularAttribute<Store, AttributeValue> maliyet;
	public static volatile SingularAttribute<Store, User> assigner;
	public static volatile SingularAttribute<Store, String> description;
	public static volatile SingularAttribute<Store, User> buyowner;
	public static volatile SingularAttribute<Store, Boolean> islem;
	public static volatile SingularAttribute<Store, String> name;
	public static volatile SingularAttribute<Store, AttributeValue> buyStatus;
	public static volatile SingularAttribute<Store, Boolean> buyKey;
	public static volatile SingularAttribute<Store, AttributeValue> status;

	public static final String STCODE = "stcode";
	public static final String OWNER = "owner";
	public static final String REQUEST = "request";
	public static final String SIRKET = "sirket";
	public static final String END_DATE = "endDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String MALIYET = "maliyet";
	public static final String ASSIGNER = "assigner";
	public static final String DESCRIPTION = "description";
	public static final String BUYOWNER = "buyowner";
	public static final String ISLEM = "islem";
	public static final String NAME = "name";
	public static final String BUY_STATUS = "buyStatus";
	public static final String BUY_KEY = "buyKey";
	public static final String STATUS = "status";

}

