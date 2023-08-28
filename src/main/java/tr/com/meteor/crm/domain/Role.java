package tr.com.meteor.crm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A role (security) used by Spring Security.
 */
@Entity
@Table(name = "jhi_role", indexes = {@Index(columnList = "search")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@IdType(idType = IdType.IdTypeEnum.String)
@EntityMetadataAnn(apiName = "roles", displayField = "id", title = "Rol", pluralTitle = "Roller")
public class Role extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @ManyToMany
    @JoinTable(
        name = "role_operation",
        joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "operation_id", referencedColumnName = "id")})
    private Set<Operation> operations = new HashSet<>();

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public Role id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return Objects.equals(id, ((Role) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Role{" +
            "id='" + id + '\'' +
            "}";
    }
}
