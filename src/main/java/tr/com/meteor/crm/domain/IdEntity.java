package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import tr.com.meteor.crm.aop.logging.SoftDeleteAspect;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@FilterDef(name = SoftDeleteAspect.SOFT_DELETE_FILTER)
@Filter(name = SoftDeleteAspect.SOFT_DELETE_FILTER, condition = "deleted_date is null")
public abstract class IdEntity<TIdType extends Serializable> implements Serializable {

    @Id
    @GenericGenerator(name = "generic_id_generator", strategy = "tr.com.meteor.crm.utils.idgenerator.GenericIdGenerator")
    @GeneratedValue(generator = "generic_id_generator")
    @FieldMetadataAnn(title = "Id", search = true, readOnly = true, priority = Integer.MIN_VALUE, active = false)
    protected TIdType id;

    protected Instant deletedDate;

    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    protected User deletedBy;

    @JsonIgnore
    @Column(length = 2048)
    protected String search;

    public TIdType getId() {
        return id;
    }

    public void setId(TIdType id) {
        this.id = id;
    }

    public Instant getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public User getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(User deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @PrePersist
    @PreUpdate
    public void updateSearchPre() {
        search = id == null ? "" : id.toString();
    }
}
