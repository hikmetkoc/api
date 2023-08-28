package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QuoteVersion.class)
public abstract class QuoteVersion_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<QuoteVersion, Quote> quote;
	public static volatile SingularAttribute<QuoteVersion, String> instanceName;
	public static volatile SingularAttribute<QuoteVersion, String> name;
	public static volatile SingularAttribute<QuoteVersion, AttributeValue> status;
	public static volatile SingularAttribute<QuoteVersion, Customer> customer;

	public static final String QUOTE = "quote";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String NAME = "name";
	public static final String STATUS = "status";
	public static final String CUSTOMER = "customer";

}

