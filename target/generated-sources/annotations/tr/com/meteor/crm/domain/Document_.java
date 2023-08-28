package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Document.class)
public abstract class Document_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<Document, User> owner;
	public static volatile SingularAttribute<Document, AttributeValue> sirket;
	public static volatile SingularAttribute<Document, String> instanceName;
	public static volatile SingularAttribute<Document, String> subject;
	public static volatile SingularAttribute<Document, String> description;

	public static final String OWNER = "owner";
	public static final String SIRKET = "sirket";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String SUBJECT = "subject";
	public static final String DESCRIPTION = "description";

}

