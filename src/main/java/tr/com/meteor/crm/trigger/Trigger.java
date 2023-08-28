package tr.com.meteor.crm.trigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.domain.IdNameAuditingEntity;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.GenericIdEntityRepository;
import tr.com.meteor.crm.repository.RoleRepository;
import tr.com.meteor.crm.security.SecurityUtils;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.utils.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Trigger<TEntity extends IdEntity<TIdType>, TIdType extends Serializable, TRepository extends GenericIdEntityRepository<TEntity, TIdType>>
    implements TriggerInterface<TEntity> {
    private static final Integer MAX_DEPTH_LIMIT = 2;
    private static Map<String, Integer> flags = new HashMap<>();
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    protected final CacheManager cacheManager;
    protected final BaseUserService baseUserService;
    protected final BaseConfigurationService baseConfigurationService;
    protected final TRepository repository;
    private final Class<TEntity> entityClass;
    private final Class<? extends Trigger<TEntity, TIdType, TRepository>> triggerClass;
    private String serializedDbEntity = null;

    Trigger(CacheManager cacheManager, Class<TEntity> entityClass,
            Class<? extends Trigger<TEntity, TIdType, TRepository>> triggerClass,
            TRepository repository, BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        this.cacheManager = cacheManager;
        this.entityClass = entityClass;
        this.triggerClass = triggerClass;
        this.repository = repository;
        this.baseUserService = baseUserService;
        this.baseConfigurationService = baseConfigurationService;
    }

    @Override
    public final TEntity beforeInsertAbs(TEntity newEntity) throws Exception {
        String key = getKey();
        if (isRunLimitExceeded(key)) return newEntity;
        increaseDepth(key);
        try {
            beforeInsert(newEntity);
        } catch (Exception ex) {
            decreaseDepth(getKey());
            throw ex;
        }

        decreaseDepth(getKey());
        return newEntity;
    }

    @Override
    public final TEntity afterInsertAbs(TEntity newEntity) throws Exception {
        String key = getKey();
        if (isRunLimitExceeded(key)) return newEntity;
        increaseDepth(getKey());
        try {
            afterInsert(newEntity);
            onClearCache(newEntity);
        } catch (Exception ex) {
            decreaseDepth(getKey());
            throw ex;
        }
        decreaseDepth(getKey());
        return newEntity;
    }

    @Override
    public final TEntity beforeUpdateAbs(TEntity newEntity) throws Exception {
        String key = getKey();
        if (isRunLimitExceeded(key)) return newEntity;
        increaseDepth(getKey());
        try {
            serializedDbEntity = getOldEntity(newEntity);
            beforeUpdate(objectMapper.readValue(serializedDbEntity, entityClass), newEntity);
        } catch (Exception ex) {
            decreaseDepth(getKey());
            throw ex;
        }
        decreaseDepth(getKey());
        return newEntity;
    }

    @Override
    public final TEntity afterUpdateAbs(TEntity newEntity) throws Exception {
        String key = getKey();
        if (isRunLimitExceeded(key)) return newEntity;
        increaseDepth(getKey());
        try {
            TEntity oldEntity = objectMapper.readValue(serializedDbEntity, entityClass);
            afterUpdate(oldEntity, newEntity);
            if (newEntity instanceof IdNameAuditingEntity && ((IdNameAuditingEntity) newEntity).getCreatedBy() == null
                && oldEntity instanceof IdNameAuditingEntity) {
                ((IdNameAuditingEntity) newEntity).setCreatedBy(((IdNameAuditingEntity) oldEntity).getCreatedBy());
            }
            onClearCache(newEntity);
        } catch (Exception ex) {
            decreaseDepth(getKey());
            throw ex;
        }
        decreaseDepth(getKey());
        return newEntity;
    }

    @Override
    public final void beforeDeleteAbs(TEntity entity) throws Exception {
        String key = getKey();
        if (isRunLimitExceeded(key)) return;
        increaseDepth(getKey());
        try {
            beforeDelete(entity);
        } catch (Exception ex) {
            decreaseDepth(getKey());
            throw ex;
        }
        decreaseDepth(getKey());
    }

    @Override
    public final void afterDeleteAbs(TEntity entity) throws Exception {
        String key = getKey();
        if (isRunLimitExceeded(key)) return;
        increaseDepth(getKey());
        try {
            afterDelete(entity);
            onClearCache(entity);
        } catch (Exception ex) {
            decreaseDepth(getKey());
            throw ex;
        }
        decreaseDepth(getKey());
    }

    private String getOldEntity(TEntity newEntity) throws JsonProcessingException {
        String serializedOldEntity = repository.getSerializedOldEntity(newEntity);

        if (serializedOldEntity != null) {
            return serializedOldEntity;
        } else {
            return objectMapper.writeValueAsString(Utils.extractHibernateProxy(repository.findById(newEntity.getId()).get()));
        }
    }

    private String getKey() {
        return Thread.currentThread().getId()
            + "." + entityClass.getSimpleName()
            + "." + triggerClass.getSimpleName()
            + "." + new Throwable().getStackTrace()[1].getMethodName();
    }

    private boolean isRunLimitExceeded(String key) {
        return flags.getOrDefault(key, 0) >= MAX_DEPTH_LIMIT;
    }

    private void increaseDepth(String key) {
        flags.put(key, flags.getOrDefault(key, 0) + 1);
    }

    private void decreaseDepth(String key) {
        if (flags.containsKey(key)) {
            int newValue = flags.getOrDefault(key, 0) - 1;
            if (newValue < 1) {
                flags.remove(key);
            } else {
                flags.put(key, newValue);
            }
        }
    }

    public User getCurrentUser() {
        return baseUserService.getUserFullFetched(SecurityUtils.getCurrentUserId().get()).get();
    }

    public Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId().get();
    }

    public boolean isUserHaveOperation(RoleRepository roleRepository, Long userId, String operationId) {
        List<String> userRoleIds = baseUserService.getUserFullFetched(userId).get()
            .getRoles().stream().map(Role::getId).collect(Collectors.toList());

        return roleRepository.findAll().stream()
            .filter(x -> userRoleIds.contains(x.getId()))
            .anyMatch(role -> role.getOperations().stream().anyMatch(x -> x.getId().equals(operationId)));
    }

    public boolean isUserHaveRole(Long userId, String roleId) {
        Set<Role> userRoles = baseUserService.getUserFullFetched(userId).get().getRoles();

        return userRoles.stream().anyMatch(role -> role.getId().equals(roleId));
    }
}
