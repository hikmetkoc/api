package tr.com.meteor.crm.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import tr.com.meteor.crm.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof CustomSecurityUser) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    return springSecurityUser.getUsername();
                } else if (authentication.getPrincipal() instanceof String) {
                    return (String) authentication.getPrincipal();
                }

                return null;
            });
    }

    public static Optional<Long> getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof CustomSecurityUser) {
                    CustomSecurityUser springSecurityUser = (CustomSecurityUser) authentication.getPrincipal();
                    return springSecurityUser.getUserId();
                }

                return null;
            });
    }
    public static Optional<User> getCurrentUserReference() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof CustomSecurityUser) {
                    CustomSecurityUser springSecurityUser = (CustomSecurityUser) authentication.getPrincipal();

                    User user = new User();
                    user.setId(springSecurityUser.getUserId());
                    user.setLogin(springSecurityUser.getUsername());

                    return user;
                }

                return null;
            });
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.addAll(authentication.getAuthorities());
                return authorities.stream()
                    .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(RolesConstants.ANONYMOUS));
            })
            .orElse(false);
    }

    /**
     * If the current user has a specific role (security).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param role the role to check.
     * @return true if the current user has the role, false otherwise.
     */
    public static boolean isCurrentUserInRole(String role) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.addAll(authentication.getAuthorities());
                return authorities.stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
            })
            .orElse(false);
    }
}
