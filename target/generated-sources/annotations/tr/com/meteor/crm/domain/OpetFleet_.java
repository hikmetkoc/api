package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OpetFleet.class)
public abstract class OpetFleet_ extends tr.com.meteor.crm.domain.IdEntity_ {

	public static volatile SingularAttribute<OpetFleet, String> fleetName;
	public static volatile SingularAttribute<OpetFleet, Integer> fleetCode;

	public static final String FLEET_NAME = "fleetName";
	public static final String FLEET_CODE = "fleetCode";

}

