package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SurveyAnswer.class)
public abstract class SurveyAnswer_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<SurveyAnswer, String> instanceName;
	public static volatile SingularAttribute<SurveyAnswer, String> name;
	public static volatile SingularAttribute<SurveyAnswer, SurveyQuestion> surveyQuestion;

	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String SURVEY_QUESTION = "surveyQuestion";

}

