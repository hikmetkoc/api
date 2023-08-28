package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "summary-user-customers", displayField = "id", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Kullanıcı Müşteri Özeti", pluralTitle = "Kullanıcı Müşteri Özetleri"
)
@Table(indexes = {@Index(columnList = "search"), @Index(columnList = "date")})
public class SummaryUserCustomer extends IdEntity<UUID> {

    @ManyToOne
    @FieldMetadataAnn(title = "Kullanıcı", priority = 10)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;

    @FieldMetadataAnn(title = "Kullanıcı", priority = 20)
    private Instant date;

    @FieldMetadataAnn(title = "Aktif Müşteri Sayısı", priority = 50)
    private Integer countActive;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getCountActive() {
        return countActive;
    }

    public void setCountActive(Integer countActive) {
        this.countActive = countActive;
    }
}
