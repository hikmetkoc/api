package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Contract.class)
public abstract class Contract_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Contract, User> owner;
	public static volatile SingularAttribute<Contract, BigDecimal> fuelTl;
	public static volatile SingularAttribute<Contract, AttributeValue> sirket;
	public static volatile SingularAttribute<Contract, Instant> endDate;
	public static volatile SingularAttribute<Contract, String> instanceName;
	public static volatile SingularAttribute<Contract, User> assigner;
	public static volatile SingularAttribute<Contract, String> description;
	public static volatile SingularAttribute<Contract, String> products;
	public static volatile SingularAttribute<Contract, String> name;
	public static volatile SingularAttribute<Contract, AttributeValue> birim;
	public static volatile SingularAttribute<Contract, AttributeValue> paymentMethod;
	public static volatile SingularAttribute<Contract, Instant> startDate;
	public static volatile SingularAttribute<Contract, Customer> customer;
	public static volatile SingularAttribute<Contract, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String FUEL_TL = "fuelTl";
	public static final String SIRKET = "sirket";
	public static final String END_DATE = "endDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String ASSIGNER = "assigner";
	public static final String DESCRIPTION = "description";
	public static final String PRODUCTS = "products";
	public static final String NAME = "name";
	public static final String BIRIM = "birim";
	public static final String PAYMENT_METHOD = "paymentMethod";
	public static final String START_DATE = "startDate";
	public static final String CUSTOMER = "customer";
	public static final String STATUS = "status";

}

