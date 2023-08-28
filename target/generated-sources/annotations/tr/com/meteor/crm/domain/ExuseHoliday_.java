package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ExuseHoliday.class)
public abstract class ExuseHoliday_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<ExuseHoliday, User> owner;
	public static volatile SingularAttribute<ExuseHoliday, AttributeValue> approvalStatus;
	public static volatile SingularAttribute<ExuseHoliday, Double> izingun;
	public static volatile SingularAttribute<ExuseHoliday, Instant> endDate;
	public static volatile SingularAttribute<ExuseHoliday, Instant> comeDate;
	public static volatile SingularAttribute<ExuseHoliday, String> instanceName;
	public static volatile SingularAttribute<ExuseHoliday, String> name;
	public static volatile SingularAttribute<ExuseHoliday, User> assigner;
	public static volatile SingularAttribute<ExuseHoliday, String> description;
	public static volatile SingularAttribute<ExuseHoliday, User> user;
	public static volatile SingularAttribute<ExuseHoliday, Instant> startDate;

	public static final String OWNER = "owner";
	public static final String APPROVAL_STATUS = "approvalStatus";
	public static final String IZINGUN = "izingun";
	public static final String END_DATE = "endDate";
	public static final String COME_DATE = "comeDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String ASSIGNER = "assigner";
	public static final String DESCRIPTION = "description";
	public static final String USER = "user";
	public static final String START_DATE = "startDate";

}

