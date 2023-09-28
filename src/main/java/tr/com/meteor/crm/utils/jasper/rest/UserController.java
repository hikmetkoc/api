package tr.com.meteor.crm.utils.jasper.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tr.com.meteor.crm.domain.Resign;
import tr.com.meteor.crm.utils.jasper.rest.GenericIdNameAuditingEntityController;
import tr.com.meteor.crm.utils.jasper.rest.errors.BadRequestAlertException;
import tr.com.meteor.crm.utils.jasper.rest.errors.EmailAlreadyUsedException;
import tr.com.meteor.crm.utils.jasper.rest.errors.LoginAlreadyUsedException;
import tr.com.meteor.crm.config.Constants;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.security.RolesConstants;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.UserService;
import tr.com.meteor.crm.service.dto.UserDTO;
import tr.com.meteor.crm.service.mapper.UserMapper;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends GenericIdNameAuditingEntityController<User, Long, UserRepository, UserService> {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserService userService;
    private final MailService mailService;
    private final UserMapper userMapper;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public UserController(UserService userService, UserRepository userRepository, MailService mailService, UserMapper userMapper) {
        super(userService);
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasRole(\"" + RolesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName, "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasRole(\"" + RolesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()));
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Gets a list of all roles.
     *
     * @return a string list of all roles.
     */
    @GetMapping("/users/roles")
    @PreAuthorize("hasRole(\"" + RolesConstants.ADMIN + "\")")
    public List<String> getRoles() {
        return userService.getRoles();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithRolesByLogin(login)
                .map(userMapper::toDto));
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasRole(\"" + RolesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "A user is deleted with identifier " + login, login)).build();
    }

    @PostMapping("/hierarchical-users")
    public Object getHierarchicalUsers(Long id) {
        return userService.getHierarchicalUsers(userService.getUserWithRoles(id).get());
    }

    @PostMapping("/hierarchical-users-only-downwards")
    public Object getHierarchicalUsersOnlyDownwards(Long id) {
        return userService.getHierarchicalUsersOnlyDownwards(userService.getUserWithRoles(id).get());
    }

    @PostMapping("/save-fcm-token")
    public void saveFcmToken(@RequestBody String token) {
        userService.saveFcmToken(token);
    }

    @GetMapping("/encode-password")
    public String encodePassword(@RequestParam(value = "password") String key) {
        return service.createHash(key);
    }

    @PostMapping("report")
    public ResponseEntity<ByteArrayResource> reportTask(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelUserReport(getCurrentUser(), startDate, endDate)));
    }

    @PostMapping("/generate-resign-form")
    public ResponseEntity<String> generatePdf(@RequestBody String htmlContent) {
        try {
            byte[] base64Pdf = service.generateResignBase64(htmlContent);
            String base64EncodedPDF = Base64.getEncoder().encodeToString(base64Pdf);
            return ResponseEntity.ok(base64EncodedPDF);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("PDF oluşturma hatası: " + e.getMessage());
        }
    }
    @PutMapping("/newPerson")
    public ResponseEntity<String> addNewPerson(
        @RequestBody User user,
        @RequestParam String unvan,
        @RequestParam String sgkSirket,
        @RequestParam String egitim,
        @RequestParam String askerlik,
        @RequestParam String cinsiyet,
        @RequestParam String ehliyet
    ) throws Exception{

        try {
            // Diğer işlemleri burada gerçekleştirin
            service.newPerson(user, unvan, sgkSirket, egitim, askerlik, cinsiyet, ehliyet);
            return ResponseEntity.ok().build();
        } catch (NullPointerException exception) {
            // Hata durumunda uygun bir hata yanıtı döndürün
            return ResponseEntity.badRequest().body("Eksik veya hatalı veri girişi yapıldı!");
        }
    }
}
