package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SummaryOpetSale.class)
public abstract class SummaryOpetSale_ extends tr.com.meteor.crm.domain.IdEntity_ {

	public static volatile SingularAttribute<SummaryOpetSale, BigDecimal> volume;
	public static volatile SingularAttribute<SummaryOpetSale, BigDecimal> total;
	public static volatile SingularAttribute<SummaryOpetSale, Instant> saleEnd;
	public static volatile SingularAttribute<SummaryOpetSale, Integer> fleetId;
	public static volatile SingularAttribute<SummaryOpetSale, User> user;
	public static volatile SingularAttribute<SummaryOpetSale, String> productName;
	public static volatile SingularAttribute<SummaryOpetSale, Customer> customer;
	public static volatile SingularAttribute<SummaryOpetSale, Integer> fleetCode;

	public static final String VOLUME = "volume";
	public static final String TOTAL = "total";
	public static final String SALE_END = "saleEnd";
	public static final String FLEET_ID = "fleetId";
	public static final String USER = "user";
	public static final String PRODUCT_NAME = "productName";
	public static final String CUSTOMER = "customer";
	public static final String FLEET_CODE = "fleetCode";

}

