package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RiskAnalysis.class)
public abstract class RiskAnalysis_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<RiskAnalysis, AttributeValue> riskCommercialType;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> mounthlyLimit;
	public static volatile SingularAttribute<RiskAnalysis, String> currencyShortName;
	public static volatile SingularAttribute<RiskAnalysis, String> userPhone;
	public static volatile SingularAttribute<RiskAnalysis, User> salesOwner;
	public static volatile SingularAttribute<RiskAnalysis, String> invoiceAddress;
	public static volatile SingularAttribute<RiskAnalysis, String> taxApartment;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> speRateDone;
	public static volatile SingularAttribute<RiskAnalysis, String> contact;
	public static volatile SingularAttribute<RiskAnalysis, String> systemEmail;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> eCollection;
	public static volatile SingularAttribute<RiskAnalysis, Double> dieselDiscountRate;
	public static volatile SingularAttribute<RiskAnalysis, Integer> invoiceMaturity;
	public static volatile SingularAttribute<RiskAnalysis, Integer> deviceCount;
	public static volatile SingularAttribute<RiskAnalysis, Instant> contractStartDate;
	public static volatile SingularAttribute<RiskAnalysis, BigDecimal> compensationAmount;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> currentExtract;
	public static volatile SingularAttribute<RiskAnalysis, String> taxNumber;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> fuelcard;
	public static volatile SingularAttribute<RiskAnalysis, BigDecimal> giftCardLimRate;
	public static volatile SingularAttribute<RiskAnalysis, String> fleetCode;
	public static volatile SingularAttribute<RiskAnalysis, AttributeValue> ttsService;
	public static volatile SingularAttribute<RiskAnalysis, String> phone;
	public static volatile SingularAttribute<RiskAnalysis, District> district;
	public static volatile SingularAttribute<RiskAnalysis, BigDecimal> blockingLimit;
	public static volatile SingularAttribute<RiskAnalysis, String> name;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> autoLimit;
	public static volatile SingularAttribute<RiskAnalysis, AttributeValue> paymentMethod;
	public static volatile SingularAttribute<RiskAnalysis, String> note;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> warningOn;
	public static volatile SingularAttribute<RiskAnalysis, Double> dieselDrDefined;
	public static volatile SingularAttribute<RiskAnalysis, City> city;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> dubDbs;
	public static volatile SingularAttribute<RiskAnalysis, Double> specialRate;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> accountingDif;
	public static volatile SingularAttribute<RiskAnalysis, BigDecimal> deviceFee;
	public static volatile SingularAttribute<RiskAnalysis, String> currencyName;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> entranceFee;
	public static volatile SingularAttribute<RiskAnalysis, User> owner;
	public static volatile SingularAttribute<RiskAnalysis, AttributeValue> currenyGroup;
	public static volatile SingularAttribute<RiskAnalysis, String> address;
	public static volatile SingularAttribute<RiskAnalysis, Double> gasolineDiscountRate;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> giftcard;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> blockAmount;
	public static volatile SingularAttribute<RiskAnalysis, Instant> contractEndDate;
	public static volatile SingularAttribute<RiskAnalysis, BigDecimal> guaranteeAmount;
	public static volatile SingularAttribute<RiskAnalysis, Double> gasolineDrDefined;
	public static volatile SingularAttribute<RiskAnalysis, AttributeValue> collectionType;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> preAuthModel;
	public static volatile SingularAttribute<RiskAnalysis, BigDecimal> commitmentAmount;
	public static volatile SingularAttribute<RiskAnalysis, AttributeValue> currencyStatus;
	public static volatile SingularAttribute<RiskAnalysis, String> infoEmail;
	public static volatile SingularAttribute<RiskAnalysis, Boolean> upperLimit;
	public static volatile SingularAttribute<RiskAnalysis, String> currencyCode;
	public static volatile SingularAttribute<RiskAnalysis, BigDecimal> fuelCardLimRate;

	public static final String RISK_COMMERCIAL_TYPE = "riskCommercialType";
	public static final String MOUNTHLY_LIMIT = "mounthlyLimit";
	public static final String CURRENCY_SHORT_NAME = "currencyShortName";
	public static final String USER_PHONE = "userPhone";
	public static final String SALES_OWNER = "salesOwner";
	public static final String INVOICE_ADDRESS = "invoiceAddress";
	public static final String TAX_APARTMENT = "taxApartment";
	public static final String SPE_RATE_DONE = "speRateDone";
	public static final String CONTACT = "contact";
	public static final String SYSTEM_EMAIL = "systemEmail";
	public static final String E_COLLECTION = "eCollection";
	public static final String DIESEL_DISCOUNT_RATE = "dieselDiscountRate";
	public static final String INVOICE_MATURITY = "invoiceMaturity";
	public static final String DEVICE_COUNT = "deviceCount";
	public static final String CONTRACT_START_DATE = "contractStartDate";
	public static final String COMPENSATION_AMOUNT = "compensationAmount";
	public static final String CURRENT_EXTRACT = "currentExtract";
	public static final String TAX_NUMBER = "taxNumber";
	public static final String FUELCARD = "fuelcard";
	public static final String GIFT_CARD_LIM_RATE = "giftCardLimRate";
	public static final String FLEET_CODE = "fleetCode";
	public static final String TTS_SERVICE = "ttsService";
	public static final String PHONE = "phone";
	public static final String DISTRICT = "district";
	public static final String BLOCKING_LIMIT = "blockingLimit";
	public static final String NAME = "name";
	public static final String AUTO_LIMIT = "autoLimit";
	public static final String PAYMENT_METHOD = "paymentMethod";
	public static final String NOTE = "note";
	public static final String WARNING_ON = "warningOn";
	public static final String DIESEL_DR_DEFINED = "dieselDrDefined";
	public static final String CITY = "city";
	public static final String DUB_DBS = "dubDbs";
	public static final String SPECIAL_RATE = "specialRate";
	public static final String ACCOUNTING_DIF = "accountingDif";
	public static final String DEVICE_FEE = "deviceFee";
	public static final String CURRENCY_NAME = "currencyName";
	public static final String ENTRANCE_FEE = "entranceFee";
	public static final String OWNER = "owner";
	public static final String CURRENY_GROUP = "currenyGroup";
	public static final String ADDRESS = "address";
	public static final String GASOLINE_DISCOUNT_RATE = "gasolineDiscountRate";
	public static final String GIFTCARD = "giftcard";
	public static final String BLOCK_AMOUNT = "blockAmount";
	public static final String CONTRACT_END_DATE = "contractEndDate";
	public static final String GUARANTEE_AMOUNT = "guaranteeAmount";
	public static final String GASOLINE_DR_DEFINED = "gasolineDrDefined";
	public static final String COLLECTION_TYPE = "collectionType";
	public static final String PRE_AUTH_MODEL = "preAuthModel";
	public static final String COMMITMENT_AMOUNT = "commitmentAmount";
	public static final String CURRENCY_STATUS = "currencyStatus";
	public static final String INFO_EMAIL = "infoEmail";
	public static final String UPPER_LIMIT = "upperLimit";
	public static final String CURRENCY_CODE = "currencyCode";
	public static final String FUEL_CARD_LIM_RATE = "fuelCardLimRate";

}

