package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Holiday.class)
public abstract class Holiday_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Holiday, User> owner;
	public static volatile SingularAttribute<Holiday, AttributeValue> approvalStatus;
	public static volatile SingularAttribute<Holiday, String> base64File;
	public static volatile SingularAttribute<Holiday, City> city;
	public static volatile SingularAttribute<Holiday, Instant> endDate;
	public static volatile SingularAttribute<Holiday, Instant> comeDate;
	public static volatile SingularAttribute<Holiday, String> instanceName;
	public static volatile SingularAttribute<Holiday, AttributeValue> haftalikGun;
	public static volatile SingularAttribute<Holiday, Boolean> haftalikizin;
	public static volatile SingularAttribute<Holiday, User> assigner;
	public static volatile SingularAttribute<Holiday, String> description;
	public static volatile SingularAttribute<Holiday, AttributeValue> type;
	public static volatile SingularAttribute<Holiday, Double> izingun;
	public static volatile SingularAttribute<Holiday, String> name;
	public static volatile SingularAttribute<Holiday, User> user;
	public static volatile SingularAttribute<Holiday, Instant> startDate;

	public static final String OWNER = "owner";
	public static final String APPROVAL_STATUS = "approvalStatus";
	public static final String BASE64_FILE = "base64File";
	public static final String CITY = "city";
	public static final String END_DATE = "endDate";
	public static final String COME_DATE = "comeDate";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String HAFTALIK_GUN = "haftalikGun";
	public static final String HAFTALIKIZIN = "haftalikizin";
	public static final String ASSIGNER = "assigner";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String IZINGUN = "izingun";
	public static final String NAME = "name";
	public static final String USER = "user";
	public static final String START_DATE = "startDate";

}

