package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "reports", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Rapor", pluralTitle = "Raporlar"
)
@Table(indexes = {@Index(columnList = "search")})
public class Report extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Rapor Adı", search = true, display = true, priority = 10)
    private String name;

    @FieldMetadataAnn(readOnly = true, title = "Obje Adı", search = true, display = true, priority = 20)
    private String objectName;

    @Column(columnDefinition = "TEXT")
    @FieldMetadataAnn(title = "Sorgu", priority = 30)
    private String queryJson;

    @FieldMetadataAnn(title = "Açıklama", priority = 60)
    @Column(length = 2048)
    private String description;

    @FieldMetadataAnn(title = "Zamanlayıcı", priority = 40)
    private String cronString;

    @Column(columnDefinition = "TEXT")
    @FieldMetadataAnn(title = "Mail Adresleri", priority = 50)
    private String mails;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueryJson() {
        return queryJson;
    }

    public void setQueryJson(String queryJson) {
        this.queryJson = queryJson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCronString() {
        return cronString;
    }

    public void setCronString(String cronString) {
        this.cronString = cronString;
    }

    public String getMails() {
        return mails;
    }

    public void setMails(String mails) {
        this.mails = mails;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        if (StringUtils.isNotBlank(objectName)) {
            search += " " + objectName;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
