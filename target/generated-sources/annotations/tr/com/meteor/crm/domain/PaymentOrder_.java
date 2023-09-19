package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PaymentOrder.class)
public abstract class PaymentOrder_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<PaymentOrder, String> base64File;
	public static volatile SingularAttribute<PaymentOrder, Boolean> muhasebeGoruntusu;
	public static volatile SingularAttribute<PaymentOrder, Instant> cancelDate;
	public static volatile SingularAttribute<PaymentOrder, AttributeValue> sirket;
	public static volatile SingularAttribute<PaymentOrder, AttributeValue> moneyType;
	public static volatile SingularAttribute<PaymentOrder, String> description;
	public static volatile SingularAttribute<PaymentOrder, Boolean> dekont;
	public static volatile SingularAttribute<PaymentOrder, BigDecimal> payamount;
	public static volatile SingularAttribute<PaymentOrder, AttributeValue> paymentType;
	public static volatile SingularAttribute<PaymentOrder, String> strIban;
	public static volatile SingularAttribute<PaymentOrder, Instant> maturityDate;
	public static volatile SingularAttribute<PaymentOrder, AttributeValue> paymentSubject;
	public static volatile SingularAttribute<PaymentOrder, BigDecimal> payTl;
	public static volatile SingularAttribute<PaymentOrder, Instant> okeySecond;
	public static volatile SingularAttribute<PaymentOrder, User> owner;
	public static volatile SingularAttribute<PaymentOrder, User> muhasebeci;
	public static volatile SingularAttribute<PaymentOrder, Instant> okeyMuh;
	public static volatile SingularAttribute<PaymentOrder, BigDecimal> amount;
	public static volatile SingularAttribute<PaymentOrder, AttributeValue> cost;
	public static volatile SingularAttribute<PaymentOrder, BigDecimal> nextamount;
	public static volatile SingularAttribute<PaymentOrder, Boolean> autopay;
	public static volatile SingularAttribute<PaymentOrder, Boolean> kismi;
	public static volatile SingularAttribute<PaymentOrder, Instant> okeyFirst;
	public static volatile SingularAttribute<PaymentOrder, User> assigner;
	public static volatile SingularAttribute<PaymentOrder, Store> store;
	public static volatile SingularAttribute<PaymentOrder, Instant> invoiceDate;
	public static volatile SingularAttribute<PaymentOrder, String> invoiceNum;
	public static volatile SingularAttribute<PaymentOrder, AttributeValue> odemeYapanSirket;
	public static volatile SingularAttribute<PaymentOrder, String> kaynak;
	public static volatile SingularAttribute<PaymentOrder, AttributeValue> paymentStyle;
	public static volatile SingularAttribute<PaymentOrder, Boolean> success;
	public static volatile SingularAttribute<PaymentOrder, Iban> iban;
	public static volatile SingularAttribute<PaymentOrder, User> cancelUser;
	public static volatile SingularAttribute<PaymentOrder, String> name;
	public static volatile SingularAttribute<PaymentOrder, User> secondAssigner;
	public static volatile SingularAttribute<PaymentOrder, AttributeValue> exchange;
	public static volatile SingularAttribute<PaymentOrder, Customer> customer;
	public static volatile SingularAttribute<PaymentOrder, AttributeValue> status;

	public static final String BASE64_FILE = "base64File";
	public static final String MUHASEBE_GORUNTUSU = "muhasebeGoruntusu";
	public static final String CANCEL_DATE = "cancelDate";
	public static final String SIRKET = "sirket";
	public static final String MONEY_TYPE = "moneyType";
	public static final String DESCRIPTION = "description";
	public static final String DEKONT = "dekont";
	public static final String PAYAMOUNT = "payamount";
	public static final String PAYMENT_TYPE = "paymentType";
	public static final String STR_IBAN = "strIban";
	public static final String MATURITY_DATE = "maturityDate";
	public static final String PAYMENT_SUBJECT = "paymentSubject";
	public static final String PAY_TL = "payTl";
	public static final String OKEY_SECOND = "okeySecond";
	public static final String OWNER = "owner";
	public static final String MUHASEBECI = "muhasebeci";
	public static final String OKEY_MUH = "okeyMuh";
	public static final String AMOUNT = "amount";
	public static final String COST = "cost";
	public static final String NEXTAMOUNT = "nextamount";
	public static final String AUTOPAY = "autopay";
	public static final String KISMI = "kismi";
	public static final String OKEY_FIRST = "okeyFirst";
	public static final String ASSIGNER = "assigner";
	public static final String STORE = "store";
	public static final String INVOICE_DATE = "invoiceDate";
	public static final String INVOICE_NUM = "invoiceNum";
	public static final String ODEME_YAPAN_SIRKET = "odemeYapanSirket";
	public static final String KAYNAK = "kaynak";
	public static final String PAYMENT_STYLE = "paymentStyle";
	public static final String SUCCESS = "success";
	public static final String IBAN = "iban";
	public static final String CANCEL_USER = "cancelUser";
	public static final String NAME = "name";
	public static final String SECOND_ASSIGNER = "secondAssigner";
	public static final String EXCHANGE = "exchange";
	public static final String CUSTOMER = "customer";
	public static final String STATUS = "status";

}

