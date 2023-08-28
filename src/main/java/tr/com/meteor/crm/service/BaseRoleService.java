package tr.com.meteor.crm.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.repository.RoleRepository;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BaseRoleService {

    public static final String ROLE_BY_ID_CACHE = "roleByIdCache";
    public static final String ROLE_ALL_CACHE = "roleAllCache";

    private final RoleRepository repository;

    public BaseRoleService(RoleRepository repository) {
        this.repository = repository;
    }

    @Cacheable(cacheNames = ROLE_BY_ID_CACHE)
    public Role getRoleById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Cacheable(cacheNames = ROLE_ALL_CACHE)
    public List<Role> getRoles() {
        return repository.findAll();
    }
}
