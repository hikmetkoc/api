package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CompanyMaterial.class)
public abstract class CompanyMaterial_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<CompanyMaterial, String> base64File;
	public static volatile SingularAttribute<CompanyMaterial, Boolean> muhasebeGoruntusu;
	public static volatile SingularAttribute<CompanyMaterial, Instant> cancelDate;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> sirket;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> moneyType;
	public static volatile SingularAttribute<CompanyMaterial, String> description;
	public static volatile SingularAttribute<CompanyMaterial, Boolean> dekont;
	public static volatile SingularAttribute<CompanyMaterial, BigDecimal> payamount;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> paymentType;
	public static volatile SingularAttribute<CompanyMaterial, String> strIban;
	public static volatile SingularAttribute<CompanyMaterial, Instant> maturityDate;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> paymentSubject;
	public static volatile SingularAttribute<CompanyMaterial, BigDecimal> payTl;
	public static volatile SingularAttribute<CompanyMaterial, Instant> okeySecond;
	public static volatile SingularAttribute<CompanyMaterial, User> owner;
	public static volatile SingularAttribute<CompanyMaterial, User> muhasebeci;
	public static volatile SingularAttribute<CompanyMaterial, Instant> okeyMuh;
	public static volatile SingularAttribute<CompanyMaterial, BigDecimal> amount;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> cost;
	public static volatile SingularAttribute<CompanyMaterial, BigDecimal> nextamount;
	public static volatile SingularAttribute<CompanyMaterial, Boolean> autopay;
	public static volatile SingularAttribute<CompanyMaterial, Boolean> kismi;
	public static volatile SingularAttribute<CompanyMaterial, Instant> okeyFirst;
	public static volatile SingularAttribute<CompanyMaterial, User> assigner;
	public static volatile SingularAttribute<CompanyMaterial, Store> store;
	public static volatile SingularAttribute<CompanyMaterial, Instant> invoiceDate;
	public static volatile SingularAttribute<CompanyMaterial, String> invoiceNum;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> odemeYapanSirket;
	public static volatile SingularAttribute<CompanyMaterial, String> kaynak;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> paymentStyle;
	public static volatile SingularAttribute<CompanyMaterial, Boolean> success;
	public static volatile SingularAttribute<CompanyMaterial, Iban> iban;
	public static volatile SingularAttribute<CompanyMaterial, User> cancelUser;
	public static volatile SingularAttribute<CompanyMaterial, String> name;
	public static volatile SingularAttribute<CompanyMaterial, User> secondAssigner;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> exchange;
	public static volatile SingularAttribute<CompanyMaterial, Customer> customer;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> status;

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

