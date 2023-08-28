package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserPermission.class)
public abstract class UserPermission_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<UserPermission, User> owner;
	public static volatile SingularAttribute<UserPermission, String> instanceName;
	public static volatile SingularAttribute<UserPermission, String> subject;
	public static volatile SingularAttribute<UserPermission, Boolean> sendInvoice;
	public static volatile SingularAttribute<UserPermission, Boolean> createPayment;
	public static volatile SingularAttribute<UserPermission, Boolean> createUser;
	public static volatile SingularAttribute<UserPermission, Boolean> holidayView;
	public static volatile SingularAttribute<UserPermission, User> user;
	public static volatile SingularAttribute<UserPermission, Boolean> spendInvoice;

	public static final String OWNER = "owner";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String SUBJECT = "subject";
	public static final String SEND_INVOICE = "sendInvoice";
	public static final String CREATE_PAYMENT = "createPayment";
	public static final String CREATE_USER = "createUser";
	public static final String HOLIDAY_VIEW = "holidayView";
	public static final String USER = "user";
	public static final String SPEND_INVOICE = "spendInvoice";

}

