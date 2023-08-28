package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class IdNameAuditingEntity<TIdType extends Serializable> extends IdNameEntity<TIdType> implements Serializable, HaveInstanceName {

    private static final long serialVersionUID = 1L;

    @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @FieldMetadataAnn(title = "Oluşturan", readOnly = true, priority = 9990, active = false)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @FieldMetadataAnn(title = "Oluşturulma Tarihi", readOnly = true, priority = 9991, active = false)
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @ManyToOne
    @FieldMetadataAnn(title = "Son Düzenleyen", readOnly = true, priority = 9992, active = false)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @FieldMetadataAnn(title = "Son Düzenlenme Tarihi", readOnly = true, priority = 9993, active = false)
    private Instant lastModifiedDate = Instant.now();
    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
