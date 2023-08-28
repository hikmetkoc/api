package tr.com.meteor.crm.utils.jasper.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.service.*;
import tr.com.meteor.crm.utils.jasper.rest.errors.RecordNotFoundException;
import tr.com.meteor.crm.utils.jasper.rest.vm.LoginVM;
import tr.com.meteor.crm.utils.metadata.MetadataReader;
import tr.com.meteor.crm.security.CustomSecurityUser;
import tr.com.meteor.crm.security.RolesConstants;
import tr.com.meteor.crm.security.jwt.JWTFilter;
import tr.com.meteor.crm.security.jwt.TokenProvider;
import tr.com.meteor.crm.utils.metadata.EntityMetadataFull;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserService userService;

    private final AttributeValueService attributeValueService;

    private final BasePermissionService basePermissionService;

    private final BaseRoleService baseRoleService;

    private final BaseConfigurationService baseConfigurationService;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                             UserService userService, AttributeValueService attributeValueService,
                             BasePermissionService basePermissionService, BaseRoleService baseRoleService, BaseConfigurationService baseConfigurationService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.attributeValueService = attributeValueService;
        this.basePermissionService = basePermissionService;
        this.baseRoleService = baseRoleService;
        this.baseConfigurationService = baseConfigurationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Optional<User> user = userService.getUserWithRolesByLogin(loginVM.getUsername());

        if (!user.isPresent()) {
            user = userService.getUserWithRolesByEmail(loginVM.getUsername());
        }

        return prepareToken(user.get(), authentication);
    }

    @Secured(RolesConstants.ADMIN)
    @PostMapping("/switch-user")
    public ResponseEntity switchUser(@RequestParam String login) {
        Optional<User> user = userService.getUserWithRolesByLogin(login);

        if (!user.isPresent()) {
            user = userService.getUserWithRolesByEmail(login);
        }

        if (!user.isPresent()) {
            throw new RecordNotFoundException(User.class.getName(), login);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            new CustomSecurityUser(user.get()),
            null,
            AuthorityUtils.createAuthorityList("ROLE_USER")
        );

        return prepareToken(user.get(), authentication);
    }

    private ResponseEntity<JWTToken> prepareToken(User user, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        List<Permission> permissions = basePermissionService.getPermissionsByRoles(user.getRoles());
        boolean isAdmin = user.getRoles().stream().anyMatch(x -> x.getId().equals(RolesConstants.ADMIN));
        Map<String, Permission> permissionMap = new HashMap<>();

        if (isAdmin) {
            for (EntityMetadataFull entityMetadataFull : MetadataReader.getClassMetadataList().values()) {
                Permission permission = new Permission();

                if (permissionMap.containsKey(entityMetadataFull.getName())) {
                    permission = permissionMap.get(entityMetadataFull.getName());
                } else {
                    permission.setObjectName(entityMetadataFull.getName());
                    permission.setRead(true);
                    permission.setUpdate(true);
                    permission.setDelete(true);
                    permission.setHierarchical(false);
                }

                permissionMap.put(permission.getObjectName(), permission);
            }
        } else {
            for (Permission p : permissions) {
                Permission permission = new Permission();

                if (permissionMap.containsKey(p.getObjectName())) {
                    permission = permissionMap.get(p.getObjectName());
                } else {
                    permission.setObjectName(p.getObjectName());
                    permission.setRead(p.isRead());
                    permission.setUpdate(p.isUpdate());
                    permission.setDelete(p.isDelete());
                    permission.setHierarchical(p.isHierarchical());
                }

                permission.setRead(permission.isRead() || p.isRead());
                permission.setUpdate(permission.isUpdate() || p.isUpdate());
                permission.setDelete(permission.isDelete() || p.isDelete());
                permission.setHierarchical(permission.isHierarchical() && p.isHierarchical());

                permissionMap.put(permission.getObjectName(), permission);
            }
        }


        List<String> userRoleIds = user.getRoles().stream().map(IdEntity::getId).collect(Collectors.toList());

        List<Role> roles = baseRoleService.getRoles()
            .stream()
            .filter(role -> userRoleIds.contains(role.getId()))
            .collect(Collectors.toList());

        List<Operation> operations = new ArrayList<>();
        roles.forEach(role -> role.getOperations().forEach(operation -> {
            if (!operations.contains(operation)) operations.add(operation);
        }));

        JWTToken jwtToken = new JWTToken(jwt)
            .user(user)
            .metadata(MetadataReader.getClassMetadataList())
            .attributeValues(attributeValueService.getAll())
            .permissions(permissionMap)
            .roles(roles)
            .operations(operations)
            .configurations(baseConfigurationService.getConfigurations());

        return new ResponseEntity<>(jwtToken, httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;
        private User user;
        private Object metadata;
        private List<AttributeValue> attributeValues;
        private Map<String, Permission> permissions;
        private List<Role> roles;
        private List<Operation> operations;
        private List<Configuration> configurations;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        public JWTToken(String idToken, User user, Object metadata) {
            this.idToken = idToken;
            this.user = user;
            this.metadata = metadata;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public JWTToken user(User user) {
            this.user = user;
            return this;
        }

        public Object getMetadata() {
            return metadata;
        }

        public void setMetadata(Object metadata) {
            this.metadata = metadata;
        }

        public JWTToken metadata(Object metadata) {
            this.metadata = metadata;
            return this;
        }

        public List<AttributeValue> getAttributeValues() {
            return attributeValues;
        }

        public void setAttributeValues(List<AttributeValue> attributeValues) {
            this.attributeValues = attributeValues;
        }

        public JWTToken attributeValues(List<AttributeValue> attributeValues) {
            this.attributeValues = attributeValues;
            return this;
        }

        public Map<String, Permission> getPermissions() {
            return permissions;
        }

        public void setPermissions(Map<String, Permission> permissions) {
            this.permissions = permissions;
        }

        public JWTToken permissions(Map<String, Permission> permissions) {
            this.permissions = permissions;
            return this;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }

        public JWTToken roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public List<Operation> getOperations() {
            return operations;
        }

        public void setOperations(List<Operation> operations) {
            this.operations = operations;
        }

        public JWTToken operations(List<Operation> operations) {
            this.operations = operations;
            return this;
        }

        public List<Configuration> getConfigurations() {
            return configurations;
        }

        public void setConfigurations(List<Configuration> configurations) {
            this.configurations = configurations;
        }

        public JWTToken configurations(List<Configuration> configurations) {
            this.configurations = configurations;
            return this;
        }
    }
}
