package tr.com.meteor.crm.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import tr.com.meteor.crm.domain.Role;

import java.util.List;

/**
 * Spring Data JPA repository for the {@link Role} entity.
 */

public interface RoleRepository extends GenericIdEntityRepository<Role, String> {

    @EntityGraph(attributePaths = {"operations"})
    List<Role> findAll();
}
