package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Offer.class)
public abstract class Offer_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Offer, User> owner;
	public static volatile SingularAttribute<Offer, BigDecimal> fuelTl;
	public static volatile SingularAttribute<Offer, Instant> endDate;
	public static volatile SingularAttribute<Offer, AttributeValue> offStat;
	public static volatile SingularAttribute<Offer, String> instanceName;
	public static volatile SingularAttribute<Offer, String> subject;
	public static volatile SingularAttribute<Offer, AttributeValue> moneyType;
	public static volatile SingularAttribute<Offer, String> description;
	public static volatile SingularAttribute<Offer, BigDecimal> eurRate;
	public static volatile SingularAttribute<Offer, AttributeValue> type;
	public static volatile SingularAttribute<Offer, BigDecimal> dolRate;
	public static volatile SingularAttribute<Offer, Integer> paymentDay;
	public static volatile SingularAttribute<Offer, AttributeValue> paymentMethod;
	public static volatile SingularAttribute<Offer, String> agreeTerms;
	public static volatile SingularAttribute<Offer, Instant> startDate;
	public static volatile SingularAttribute<Offer, Customer> customer;

	public static final String OWNER = "owner";
	public static final String FUEL_TL = "fuelTl";
	public static final String END_DATE = "endDate";
	public static final String OFF_STAT = "offStat";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String SUBJECT = "subject";
	public static final String MONEY_TYPE = "moneyType";
	public static final String DESCRIPTION = "description";
	public static final String EUR_RATE = "eurRate";
	public static final String TYPE = "type";
	public static final String DOL_RATE = "dolRate";
	public static final String PAYMENT_DAY = "paymentDay";
	public static final String PAYMENT_METHOD = "paymentMethod";
	public static final String AGREE_TERMS = "agreeTerms";
	public static final String START_DATE = "startDate";
	public static final String CUSTOMER = "customer";

}

