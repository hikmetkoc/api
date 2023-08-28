package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ContProduct.class)
public abstract class ContProduct_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<ContProduct, User> owner;
	public static volatile SingularAttribute<ContProduct, BigDecimal> fuelTl;
	public static volatile SingularAttribute<ContProduct, String> instanceName;
	public static volatile SingularAttribute<ContProduct, Buy> buy;
	public static volatile SingularAttribute<ContProduct, String> name;
	public static volatile SingularAttribute<ContProduct, User> assigner;
	public static volatile SingularAttribute<ContProduct, String> ozellik;
	public static volatile SingularAttribute<ContProduct, String> description;
	public static volatile SingularAttribute<ContProduct, Integer> miktar;
	public static volatile SingularAttribute<ContProduct, String> gerekce;
	public static volatile SingularAttribute<ContProduct, Boolean> status;

	public static final String OWNER = "owner";
	public static final String FUEL_TL = "fuelTl";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String BUY = "buy";
	public static final String NAME = "name";
	public static final String ASSIGNER = "assigner";
	public static final String OZELLIK = "ozellik";
	public static final String DESCRIPTION = "description";
	public static final String MIKTAR = "miktar";
	public static final String GEREKCE = "gerekce";
	public static final String STATUS = "status";

}

