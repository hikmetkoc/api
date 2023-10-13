package tr.com.meteor.crm.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CompanyMaterial.class)
public abstract class CompanyMaterial_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<CompanyMaterial, User> owner;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> sirket;
	public static volatile SingularAttribute<CompanyMaterial, Instant> maturityDate;
	public static volatile SingularAttribute<CompanyMaterial, String> name;
	public static volatile SingularAttribute<CompanyMaterial, User> assigner;
	public static volatile SingularAttribute<CompanyMaterial, String> description;
	public static volatile SingularAttribute<CompanyMaterial, Instant> invoiceDate;
	public static volatile SingularAttribute<CompanyMaterial, String> invoiceNum;
	public static volatile SingularAttribute<CompanyMaterial, AttributeValue> status;

	public static final String OWNER = "owner";
	public static final String SIRKET = "sirket";
	public static final String MATURITY_DATE = "maturityDate";
	public static final String NAME = "name";
	public static final String ASSIGNER = "assigner";
	public static final String DESCRIPTION = "description";
	public static final String INVOICE_DATE = "invoiceDate";
	public static final String INVOICE_NUM = "invoiceNum";
	public static final String STATUS = "status";

}

