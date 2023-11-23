package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FuelLimit.class)
public abstract class FuelLimit_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<FuelLimit, User> owner;
	public static volatile SingularAttribute<FuelLimit, BigDecimal> fuelTl;
	public static volatile SingularAttribute<FuelLimit, Instant> okeyFirst;
	public static volatile SingularAttribute<FuelLimit, Instant> endDate;
	public static volatile SingularAttribute<FuelLimit, String> instanceName;
	public static volatile SingularAttribute<FuelLimit, User> assigner;
	public static volatile SingularAttribute<FuelLimit, String> curcode;
	public static volatile SingularAttribute<FuelLimit, String> description;
	public static volatile SingularAttribute<FuelLimit, String> unvan;
	public static volatile SingularAttribute<FuelLimit, BigDecimal> totalTl;
	public static volatile SingularAttribute<FuelLimit, AttributeValue> total;
	public static volatile SingularAttribute<FuelLimit, String> name;
	public static volatile SingularAttribute<FuelLimit, Instant> startDate;
	public static volatile SingularAttribute<FuelLimit, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String FUEL_TL = "fuelTl";
	public static final String OKEY_FIRST = "okeyFirst";
	public static final String END_DATE = "endDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String ASSIGNER = "assigner";
	public static final String CURCODE = "curcode";
	public static final String DESCRIPTION = "description";
	public static final String UNVAN = "unvan";
	public static final String TOTAL_TL = "totalTl";
	public static final String TOTAL = "total";
	public static final String NAME = "name";
	public static final String START_DATE = "startDate";
	public static final String STATUS = "status";

}

