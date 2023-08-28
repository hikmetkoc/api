package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Campaign.class)
public abstract class Campaign_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Campaign, BigDecimal> expectedRevenue;
	public static volatile SingularAttribute<Campaign, Instant> endDate;
	public static volatile SingularAttribute<Campaign, String> instanceName;
	public static volatile SingularAttribute<Campaign, String> name;
	public static volatile SingularAttribute<Campaign, String> description;
	public static volatile SingularAttribute<Campaign, Instant> startDate;
	public static volatile SingularAttribute<Campaign, BigDecimal> budget;
	public static volatile SingularAttribute<Campaign, AttributeValue> status;

	public static final String EXPECTED_REVENUE = "expectedRevenue";
	public static final String END_DATE = "endDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String START_DATE = "startDate";
	public static final String BUDGET = "budget";
	public static final String STATUS = "status";

}

