package tr.com.meteor.crm.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IdEntity.class)
public abstract class IdEntity_ {

	public static volatile SingularAttribute<IdEntity, String> search;
	public static volatile SingularAttribute<IdEntity, Instant> deletedDate;
	public static volatile SingularAttribute<IdEntity, Serializable> id;
	public static volatile SingularAttribute<IdEntity, User> deletedBy;

	public static final String SEARCH = "search";
	public static final String DELETED_DATE = "deletedDate";
	public static final String ID = "id";
	public static final String DELETED_BY = "deletedBy";

}

