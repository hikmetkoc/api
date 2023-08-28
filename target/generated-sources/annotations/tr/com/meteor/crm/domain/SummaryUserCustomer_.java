package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SummaryUserCustomer.class)
public abstract class SummaryUserCustomer_ extends tr.com.meteor.crm.domain.IdEntity_ {

	public static volatile SingularAttribute<SummaryUserCustomer, Instant> date;
	public static volatile SingularAttribute<SummaryUserCustomer, User> user;
	public static volatile SingularAttribute<SummaryUserCustomer, Integer> countActive;

	public static final String DATE = "date";
	public static final String USER = "user";
	public static final String COUNT_ACTIVE = "countActive";

}

