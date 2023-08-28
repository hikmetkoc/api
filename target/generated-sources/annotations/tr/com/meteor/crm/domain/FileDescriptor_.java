package tr.com.meteor.crm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FileDescriptor.class)
public abstract class FileDescriptor_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<FileDescriptor, String> fileName;
	public static volatile SingularAttribute<FileDescriptor, String> instanceName;
	public static volatile SingularAttribute<FileDescriptor, Long> fileSize;
	public static volatile SingularAttribute<FileDescriptor, FileDescriptor> fileDescriptor;
	public static volatile SingularAttribute<FileDescriptor, String> entityName;
	public static volatile SetAttribute<FileDescriptor, FileDescriptor> fileDescriptors;
	public static volatile SingularAttribute<FileDescriptor, String> description;
	public static volatile SingularAttribute<FileDescriptor, String> entityId;
	public static volatile SingularAttribute<FileDescriptor, AttributeValue> type;
	public static volatile SingularAttribute<FileDescriptor, String> contentType;

	public static final String FILE_NAME = "fileName";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String FILE_SIZE = "fileSize";
	public static final String FILE_DESCRIPTOR = "fileDescriptor";
	public static final String ENTITY_NAME = "entityName";
	public static final String FILE_DESCRIPTORS = "fileDescriptors";
	public static final String DESCRIPTION = "description";
	public static final String ENTITY_ID = "entityId";
	public static final String TYPE = "type";
	public static final String CONTENT_TYPE = "contentType";

}

