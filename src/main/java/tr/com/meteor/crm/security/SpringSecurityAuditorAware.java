package tr.com.meteor.crm.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.User;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        User u = new User();
        u.setId(SecurityUtils.getCurrentUserId().orElse(1L));
        return Optional.of(u);
    }
}
