package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Spend.class)
public abstract class Spend_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Spend, User> owner;
	public static volatile SingularAttribute<Spend, Instant> spendDate;
	public static volatile SingularAttribute<Spend, String> paymentNum;
	public static volatile SingularAttribute<Spend, BigDecimal> amount;
	public static volatile SingularAttribute<Spend, String> instanceName;
	public static volatile SingularAttribute<Spend, BigDecimal> exchangeMoney;
	public static volatile SingularAttribute<Spend, PaymentOrder> paymentorder;
	public static volatile SingularAttribute<Spend, String> description;
	public static volatile SingularAttribute<Spend, String> odemeYapanSirket;
	public static volatile SingularAttribute<Spend, Instant> maturityDate;
	public static volatile SingularAttribute<Spend, BigDecimal> payTl;
	public static volatile SingularAttribute<Spend, Boolean> lock;
	public static volatile SingularAttribute<Spend, User> finance;
	public static volatile SingularAttribute<Spend, String> paymentStatus;
	public static volatile SingularAttribute<Spend, Customer> customer;
	public static volatile SingularAttribute<Spend, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String SPEND_DATE = "spendDate";
	public static final String PAYMENT_NUM = "paymentNum";
	public static final String AMOUNT = "amount";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String EXCHANGE_MONEY = "exchangeMoney";
	public static final String PAYMENTORDER = "paymentorder";
	public static final String DESCRIPTION = "description";
	public static final String ODEME_YAPAN_SIRKET = "odemeYapanSirket";
	public static final String MATURITY_DATE = "maturityDate";
	public static final String PAY_TL = "payTl";
	public static final String LOCK = "lock";
	public static final String FINANCE = "finance";
	public static final String PAYMENT_STATUS = "paymentStatus";
	public static final String CUSTOMER = "customer";
	public static final String STATUS = "status";

}

