package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OpetSale.class)
public abstract class OpetSale_ extends tr.com.meteor.crm.domain.IdEntity_ {

	public static volatile SingularAttribute<OpetSale, String> fleetName;
	public static volatile SingularAttribute<OpetSale, BigDecimal> unitPrice;
	public static volatile SingularAttribute<OpetSale, Integer> productId;
	public static volatile SingularAttribute<OpetSale, Instant> saleEnd;
	public static volatile SingularAttribute<OpetSale, Integer> groupId;
	public static volatile SingularAttribute<OpetSale, Integer> Odometer;
	public static volatile SingularAttribute<OpetSale, Integer> cityId;
	public static volatile SingularAttribute<OpetSale, Integer> fleetId;
	public static volatile SingularAttribute<OpetSale, Integer> rId;
	public static volatile SingularAttribute<OpetSale, Instant> processTime;
	public static volatile SingularAttribute<OpetSale, String> productName;
	public static volatile SingularAttribute<OpetSale, String> apiUser;
	public static volatile SingularAttribute<OpetSale, Integer> fleetCode;
	public static volatile SingularAttribute<OpetSale, BigDecimal> volume;
	public static volatile SingularAttribute<OpetSale, Integer> eCRReceiptNr;
	public static volatile SingularAttribute<OpetSale, String> invoicePeriodNr;
	public static volatile SingularAttribute<OpetSale, String> groupName;
	public static volatile SingularAttribute<OpetSale, BigDecimal> total;
	public static volatile SingularAttribute<OpetSale, String> licensePlateNr;
	public static volatile SingularAttribute<OpetSale, String> cityName;
	public static volatile SingularAttribute<OpetSale, String> stationName;
	public static volatile SingularAttribute<OpetSale, User> user;
	public static volatile SingularAttribute<OpetSale, Integer> stationId;
	public static volatile SingularAttribute<OpetSale, Customer> customer;

	public static final String FLEET_NAME = "fleetName";
	public static final String UNIT_PRICE = "unitPrice";
	public static final String PRODUCT_ID = "productId";
	public static final String SALE_END = "saleEnd";
	public static final String GROUP_ID = "groupId";
	public static final String ODOMETER = "Odometer";
	public static final String CITY_ID = "cityId";
	public static final String FLEET_ID = "fleetId";
	public static final String R_ID = "rId";
	public static final String PROCESS_TIME = "processTime";
	public static final String PRODUCT_NAME = "productName";
	public static final String API_USER = "apiUser";
	public static final String FLEET_CODE = "fleetCode";
	public static final String VOLUME = "volume";
	public static final String E_CR_RECEIPT_NR = "eCRReceiptNr";
	public static final String INVOICE_PERIOD_NR = "invoicePeriodNr";
	public static final String GROUP_NAME = "groupName";
	public static final String TOTAL = "total";
	public static final String LICENSE_PLATE_NR = "licensePlateNr";
	public static final String CITY_NAME = "cityName";
	public static final String STATION_NAME = "stationName";
	public static final String USER = "user";
	public static final String STATION_ID = "stationId";
	public static final String CUSTOMER = "customer";

}

