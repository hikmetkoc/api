package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "file-descriptors", displayField = "instanceName", title = "Dosya", pluralTitle = "Dosyalar")
@Table(indexes = {@Index(columnList = "search")})
public class FileDescriptor extends IdNameAuditingEntity<UUID> {

    @Formula("file_name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    @FieldMetadataAnn(readOnly = true, title = "Tip", priority = 45)
    @AttributeValueValidate(attributeId = "Dos_Tip")
    @ManyToOne
    private AttributeValue type;

    @FieldMetadataAnn(readOnly = true, title = "Obje Adı", search = true, display = true, priority = 20)
    private String entityName;

    @FieldMetadataAnn(readOnly = true, title = "Obje Id", search = true, priority = 30)
    private String entityId;

    @FieldMetadataAnn(readOnly = true, title = "Dosya Adı", search = true, display = true, priority = 10)
    private String fileName;

    @FieldMetadataAnn(readOnly = true, title = "İçerik Tipi", priority = 40)
    private String contentType;

    @FieldMetadataAnn(title = "Açıklama", priority = 15)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(readOnly = true, title = "Dosya Boyutu", display = true, priority = 30)
    private Long fileSize;

    @ManyToOne
    @JsonIgnoreProperties("fileDescriptors")
    @FieldMetadataAnn(readOnly = true, title = "Klasör", priority = 50)
    private FileDescriptor fileDescriptor;

    @OneToMany(mappedBy = "fileDescriptor")
    private Set<FileDescriptor> fileDescriptors = new HashSet<>();

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public AttributeValue getType() {
        return type;
    }

    public void setType(AttributeValue type) {
        this.type = type;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    public void setFileDescriptor(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    public Set<FileDescriptor> getFileDescriptors() {
        return fileDescriptors;
    }

    public void setFileDescriptors(Set<FileDescriptor> fileDescriptors) {
        this.fileDescriptors = fileDescriptors;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(fileName)) {
            search += " " + fileName;
        }

        if (StringUtils.isNotBlank(entityId)) {
            search += " " + entityId;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
