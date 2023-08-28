package tr.com.meteor.crm.service;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.AttributeRepository;
import tr.com.meteor.crm.repository.AttributeValueRepository;
import tr.com.meteor.crm.repository.UserRepository;

import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class CacheService {

    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearCache() {
        cacheManager.getCache(AttributeRepository.cacheName).clear();
        cacheManager.getCache(AttributeValueRepository.cacheName).clear();
        Objects.requireNonNull(cacheManager.getCache(BaseConfigurationService.CONFIGURATION_BY_ID_CACHE)).clear();
        Objects.requireNonNull(cacheManager.getCache(BaseRoleService.ROLE_ALL_CACHE)).clear();
        Objects.requireNonNull(cacheManager.getCache(BaseRoleService.ROLE_BY_ID_CACHE)).clear();
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).clear();
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).clear();
        Objects.requireNonNull(cacheManager.getCache(User.class.getName())).clear();
        Objects.requireNonNull(cacheManager.getCache(User.class.getName() + ".roles")).clear();
    }
}
