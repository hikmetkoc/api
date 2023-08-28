package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Buy.class)
public abstract class Buy_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Buy, User> owner;
	public static volatile SingularAttribute<Buy, String> stcode;
	public static volatile SingularAttribute<Buy, BigDecimal> fuelTl;
	public static volatile SingularAttribute<Buy, Instant> okeyFirst;
	public static volatile SingularAttribute<Buy, Instant> endDate;
	public static volatile SingularAttribute<Buy, String> instanceName;
	public static volatile SingularAttribute<Buy, AttributeValue> moneyType;
	public static volatile SingularAttribute<Buy, User> assigner;
	public static volatile SingularAttribute<Buy, String> description;
	public static volatile SingularAttribute<Buy, BigDecimal> onayTl;
	public static volatile SingularAttribute<Buy, Store> store;
	public static volatile SingularAttribute<Buy, Boolean> suggest;
	public static volatile SingularAttribute<Buy, AttributeValue> quoteStatus;
	public static volatile SingularAttribute<Buy, AttributeValue> preparation;
	public static volatile SingularAttribute<Buy, Instant> maturityDate;
	public static volatile SingularAttribute<Buy, Boolean> islem;
	public static volatile SingularAttribute<Buy, String> name;
	public static volatile SingularAttribute<Buy, User> secondAssigner;
	public static volatile SingularAttribute<Buy, AttributeValue> paymentMethod;
	public static volatile SingularAttribute<Buy, Instant> startDate;
	public static volatile SingularAttribute<Buy, Instant> okeySecond;
	public static volatile SingularAttribute<Buy, Customer> customer;

	public static final String OWNER = "owner";
	public static final String STCODE = "stcode";
	public static final String FUEL_TL = "fuelTl";
	public static final String OKEY_FIRST = "okeyFirst";
	public static final String END_DATE = "endDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String MONEY_TYPE = "moneyType";
	public static final String ASSIGNER = "assigner";
	public static final String DESCRIPTION = "description";
	public static final String ONAY_TL = "onayTl";
	public static final String STORE = "store";
	public static final String SUGGEST = "suggest";
	public static final String QUOTE_STATUS = "quoteStatus";
	public static final String PREPARATION = "preparation";
	public static final String MATURITY_DATE = "maturityDate";
	public static final String ISLEM = "islem";
	public static final String NAME = "name";
	public static final String SECOND_ASSIGNER = "secondAssigner";
	public static final String PAYMENT_METHOD = "paymentMethod";
	public static final String START_DATE = "startDate";
	public static final String OKEY_SECOND = "okeySecond";
	public static final String CUSTOMER = "customer";

}

