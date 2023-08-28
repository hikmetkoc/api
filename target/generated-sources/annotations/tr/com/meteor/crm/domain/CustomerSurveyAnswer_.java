package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomerSurveyAnswer.class)
public abstract class CustomerSurveyAnswer_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<CustomerSurveyAnswer, SurveyAnswer> surveyAnswer;
	public static volatile SingularAttribute<CustomerSurveyAnswer, String> instanceName;
	public static volatile SingularAttribute<CustomerSurveyAnswer, Instant> time;
	public static volatile SingularAttribute<CustomerSurveyAnswer, Customer> customer;

	public static final String SURVEY_ANSWER = "surveyAnswer";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String TIME = "time";
	public static final String CUSTOMER = "customer";

}

