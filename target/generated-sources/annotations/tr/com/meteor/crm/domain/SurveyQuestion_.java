package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SurveyQuestion.class)
public abstract class SurveyQuestion_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<SurveyQuestion, String> instanceName;
	public static volatile SingularAttribute<SurveyQuestion, String> name;
	public static volatile SingularAttribute<SurveyQuestion, Boolean> active;
	public static volatile SingularAttribute<SurveyQuestion, AttributeValue> type;

	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String ACTIVE = "active";
	public static final String TYPE = "type";

}

