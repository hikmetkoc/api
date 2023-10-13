package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InvoiceList.class)
public abstract class InvoiceList_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<InvoiceList, AttributeValue> approvalGroup;
	public static volatile SingularAttribute<InvoiceList, AttributeValue> sirket;
	public static volatile SingularAttribute<InvoiceList, Instant> sendDate;
	public static volatile SingularAttribute<InvoiceList, String> instanceName;
	public static volatile SingularAttribute<InvoiceList, AttributeValue> moneyType;
	public static volatile SingularAttribute<InvoiceList, String> description;
	public static volatile SingularAttribute<InvoiceList, Boolean> dekont;
	public static volatile SingularAttribute<InvoiceList, Instant> successDate;
	public static volatile SingularAttribute<InvoiceList, AttributeValue> paymentType;
	public static volatile SingularAttribute<InvoiceList, Instant> maturityDate;
	public static volatile SingularAttribute<InvoiceList, AttributeValue> paymentSubject;
	public static volatile SingularAttribute<InvoiceList, BigDecimal> payTl;
	public static volatile SingularAttribute<InvoiceList, User> owner;
	public static volatile SingularAttribute<InvoiceList, BigDecimal> amount;
	public static volatile SingularAttribute<InvoiceList, AttributeValue> cost;
	public static volatile SingularAttribute<InvoiceList, Boolean> autopay;
	public static volatile SingularAttribute<InvoiceList, Boolean> kismi;
	public static volatile SingularAttribute<InvoiceList, User> permission;
	public static volatile SingularAttribute<InvoiceList, Instant> invoiceDate;
	public static volatile SingularAttribute<InvoiceList, String> invoiceNum;
	public static volatile SingularAttribute<InvoiceList, AttributeValue> odemeYapanSirket;
	public static volatile SingularAttribute<InvoiceList, AttributeValue> paymentStyle;
	public static volatile SingularAttribute<InvoiceList, Boolean> success;
	public static volatile SingularAttribute<InvoiceList, Iban> iban;
	public static volatile SingularAttribute<InvoiceList, String> name;
	public static volatile SingularAttribute<InvoiceList, AttributeValue> exchange;
	public static volatile SingularAttribute<InvoiceList, AttributeValue> invoiceStatus;
	public static volatile SingularAttribute<InvoiceList, Customer> customer;

	public static final String APPROVAL_GROUP = "approvalGroup";
	public static final String SIRKET = "sirket";
	public static final String SEND_DATE = "sendDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String MONEY_TYPE = "moneyType";
	public static final String DESCRIPTION = "description";
	public static final String DEKONT = "dekont";
	public static final String SUCCESS_DATE = "successDate";
	public static final String PAYMENT_TYPE = "paymentType";
	public static final String MATURITY_DATE = "maturityDate";
	public static final String PAYMENT_SUBJECT = "paymentSubject";
	public static final String PAY_TL = "payTl";
	public static final String OWNER = "owner";
	public static final String AMOUNT = "amount";
	public static final String COST = "cost";
	public static final String AUTOPAY = "autopay";
	public static final String KISMI = "kismi";
	public static final String PERMISSION = "permission";
	public static final String INVOICE_DATE = "invoiceDate";
	public static final String INVOICE_NUM = "invoiceNum";
	public static final String ODEME_YAPAN_SIRKET = "odemeYapanSirket";
	public static final String PAYMENT_STYLE = "paymentStyle";
	public static final String SUCCESS = "success";
	public static final String IBAN = "iban";
	public static final String NAME = "name";
	public static final String EXCHANGE = "exchange";
	public static final String INVOICE_STATUS = "invoiceStatus";
	public static final String CUSTOMER = "customer";

}

