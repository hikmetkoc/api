package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FuelRisk.class)
public abstract class FuelRisk_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<FuelRisk, BigDecimal> onayli;
	public static volatile SingularAttribute<FuelRisk, String> bank;
	public static volatile SingularAttribute<FuelRisk, FuelLimit> fuellimit;
	public static volatile SingularAttribute<FuelRisk, BigDecimal> dbs;
	public static volatile SingularAttribute<FuelRisk, String> instanceName;
	public static volatile SingularAttribute<FuelRisk, String> name;
	public static volatile SingularAttribute<FuelRisk, BigDecimal> nakit;

	public static final String ONAYLI = "onayli";
	public static final String BANK = "bank";
	public static final String FUELLIMIT = "fuellimit";
	public static final String DBS = "dbs";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String NAKIT = "nakit";

}

