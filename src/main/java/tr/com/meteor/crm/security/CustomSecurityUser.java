package tr.com.meteor.crm.security;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomSecurityUser extends User implements UserDetails, CredentialsContainer {

    private Long userId;

    public CustomSecurityUser(tr.com.meteor.crm.domain.User user) {
        super(user.getLogin(), user.getPassword(), user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getId()))
            .collect(Collectors.toList()));
        this.userId = user.getId();
    }

    public CustomSecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userId) {
        this(username, password, true, true, true, true, authorities, userId);
    }

    public CustomSecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                              boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
