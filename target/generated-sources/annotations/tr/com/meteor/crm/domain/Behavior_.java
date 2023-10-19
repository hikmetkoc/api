package tr.com.meteor.crm.domain;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Behavior.class)
public abstract class Behavior_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Behavior, User> owner;
	public static volatile SingularAttribute<Behavior, BigDecimal> fuelTl;
	public static volatile SingularAttribute<Behavior, String> instanceName;
	public static volatile SingularAttribute<Behavior, String> subject;
	public static volatile SingularAttribute<Behavior, String> document;
	public static volatile SingularAttribute<Behavior, MotionSums> motionsums;
	public static volatile SingularAttribute<Behavior, String> description;
	public static volatile SingularAttribute<Behavior, AttributeValue> type;
	public static volatile SingularAttribute<Behavior, Instant> inputDate;

	public static final String OWNER = "owner";
	public static final String FUEL_TL = "fuelTl";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String SUBJECT = "subject";
	public static final String DOCUMENT = "document";
	public static final String MOTIONSUMS = "motionsums";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String INPUT_DATE = "inputDate";

}

