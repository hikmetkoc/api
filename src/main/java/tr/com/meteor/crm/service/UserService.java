package tr.com.meteor.crm.service;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.AttributeValueRepository;
import tr.com.meteor.crm.service.dto.UserDTO;
import tr.com.meteor.crm.service.mapper.UserMapper;
import tr.com.meteor.crm.service.util.RandomUtil;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.jasper.rest.errors.EmailAlreadyUsedException;
import tr.com.meteor.crm.utils.jasper.rest.errors.InvalidPasswordException;
import tr.com.meteor.crm.utils.jasper.rest.errors.LoginAlreadyUsedException;
import tr.com.meteor.crm.config.Constants;
import tr.com.meteor.crm.repository.RoleRepository;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.security.RolesConstants;
import tr.com.meteor.crm.security.SecurityUtils;
import tr.com.meteor.crm.utils.request.Request;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.pdf.PdfWriter;

/**
 * Service class for managing users.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends GenericIdNameAuditingEntityService<User, Long, UserRepository> {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final AttributeValueRepository attributeValueRepository;

    private final RoleRepository roleRepository;

    private final CacheManager cacheManager;

    private final UserMapper userMapper;

    public UserService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                       CacheManager cacheManager, BaseConfigurationService baseConfigurationService, UserRepository repository,
                       PasswordEncoder passwordEncoder, AttributeValueRepository attributeValueRepository, RoleRepository roleRepository, UserMapper userMapper) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            User.class, repository);
        this.passwordEncoder = passwordEncoder;
        this.attributeValueRepository = attributeValueRepository;
        this.roleRepository = roleRepository;
        this.cacheManager = cacheManager;
        this.userMapper = userMapper;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return repository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return repository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                clearUserCaches(user);
                return user;
            });
    }

    public String createHash(String newPassword) {
        return passwordEncoder.encode(newPassword);
    }

    public Optional<User> requestPasswordReset(String mail) {
        return repository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        repository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        repository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(RolesConstants.USER).ifPresent(roles::add);
        newUser.setRoles(roles);
        repository.save(newUser);
        clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
            return false;
        }

        repository.deleteSoft(existingUser);
        repository.flush();
        clearUserCaches(existingUser);

        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());

        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }

        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);

        if (userDTO.getRoles() != null) {
            user.setRoles(userDTO.getRoles());
        }

        repository.save(user);
        clearUserCaches(user);
        log.debug("Created Information for User: {}", user);

        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(repository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(repository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail().toLowerCase());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Role> managedRoles = user.getRoles();
                managedRoles.clear();
                managedRoles.addAll(userDTO.getRoles());
                clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(userMapper::toDto);
    }

    public void deleteUser(String login) {
        repository.findOneByLogin(login).ifPresent(user -> {
            repository.deleteSoft(user);
            clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(repository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return repository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(userMapper::toDto);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<User> getUserWithRolesByLogin(String login) {
        return repository.findOneWithRolesByLogin(login);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<User> getUserWithRolesByEmail(String email) {
        return repository.findOneWithRolesByEmailIgnoreCase(email);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<User> getUserByEmail(String email) {
        return repository.findOneByEmailIgnoreCase(email);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<User> getUserWithRoles(Long id) {
        return baseUserService.getUserWithRoles(id);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<User> getUserWithRoles() {
        return baseUserService.getUserWithRoles();
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        repository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                repository.deleteSoft(user);
                clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the roles.
     *
     * @return a list of all the roles.
     */
    public List<String> getRoles() {
        return roleRepository.findAll().stream().map(Role::getId).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        Objects.requireNonNull(cacheManager.getCache(User.class.getName())).clear();
        Objects.requireNonNull(cacheManager.getCache(User.class.getName() + ".roles")).clear();
    }

    public List<User> getHierarchicalUsersOnlyDownwards(User user) {
        return baseUserService.getHierarchicalUsersOnlyDownwards(user);
    }

    public List<User> getHierarchicalUsers(User user) {
        return baseUserService.getHierarchicalUsers(user);
    }

    public void saveFcmToken(String token) {
        User user = getCurrentUser();
        user.addFcmToken(token);

        repository.save(user);
    }

    public byte[] generateResignBase64(String htmlContent) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // JSoup ile HTML içeriği analiz edilir
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlContent);

        // HTML içeriğini PDF'e ekleme
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph paragraph = new Paragraph(jsoupDoc.text(), font);
        document.add(paragraph);

        document.close();
        return outputStream.toByteArray();
    }
    public byte[] generateExcelUserReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.Or(
                    Filter.FilterItem("id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<User> Users = getData(null, request, false).getBody();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        sheet.setColumnWidth(0, 500);
        sheet.createRow(0).setHeight((short) 500);

        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);

        int rowIndex = 1;
        int columnIndex = 1;

        XSSFRow headerRow = sheet.createRow(rowIndex++);

        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(boldFont);
        headerCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 189, (byte) 215, (byte) 238}, null));
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCell headerTcCell = headerRow.createCell(columnIndex++);     //TC Kimlik Nu
        XSSFCell headerNameCell = headerRow.createCell(columnIndex++);     //Ad
        XSSFCell headerSurnameCell = headerRow.createCell(columnIndex++);  //Soyad
        XSSFCell headerMailCell = headerRow.createCell(columnIndex++);  //Mail Adresi
        XSSFCell headerCompanyCell = headerRow.createCell(columnIndex++);     //Şirket
        XSSFCell headerDepartmentCell = headerRow.createCell(columnIndex++);     //Birim
        XSSFCell headerTitleCell = headerRow.createCell(columnIndex++);      //Unvan
        XSSFCell headerComPhoneCell = headerRow.createCell(columnIndex++);      //Şirket Numarası
        XSSFCell headerShortCodeCell = headerRow.createCell(columnIndex++);    //Kısa Kod
        XSSFCell headerJobStartDateCell = headerRow.createCell(columnIndex++);    //İşe Başladığı Tarih
        XSSFCell headerPersonelPhoneCell = headerRow.createCell(columnIndex++);   //Şahsi Numara
        XSSFCell headerBirthDateCell = headerRow.createCell(columnIndex++); //Doğum Tarihi
        XSSFCell headerIBANCell = headerRow.createCell(columnIndex++); //IBAN
        XSSFCell headerInsCompanyCell = headerRow.createCell(columnIndex++); //Sigortalı Olduğu Şirket
        XSSFCell headerInsDepartmentCell = headerRow.createCell(columnIndex++); //Sigortalı Olduğu Birim
        XSSFCell headerInsTitleCell = headerRow.createCell(columnIndex++); //Sigortalı Olduğu Unvan
        XSSFCell headerActivatedCell = headerRow.createCell(columnIndex++); //Aktiflik
        XSSFCell headerQuitJobDateCell = headerRow.createCell(columnIndex++); //İşten Ayrılma Tarihi
        XSSFCell headerGenderCell = headerRow.createCell(columnIndex++); //Cinsiyet
        XSSFCell headerBloodGroupCell = headerRow.createCell(columnIndex++); //Kan Grubu
        XSSFCell headerEducationCell = headerRow.createCell(columnIndex++); //Eğitim Durumu
        XSSFCell headerGraduatedCell = headerRow.createCell(columnIndex++); //Mezun Olduğu Kurum
        XSSFCell headerGradSectionCell = headerRow.createCell(columnIndex++); //Mezun Olduğu Bölüm
        XSSFCell headerTCGovCell = headerRow.createCell(columnIndex++); //TC Vatandaşlığı
        XSSFCell headerBirthCityCell = headerRow.createCell(columnIndex++); //Doğduğu İl
        XSSFCell headerBirthDistrictCell = headerRow.createCell(columnIndex++); //Doğduğu İlçe
        XSSFCell headerAddressCell = headerRow.createCell(columnIndex++); //Açık Adres
        XSSFCell headerMilitaryCell = headerRow.createCell(columnIndex++); //Askerlik Durumu
        XSSFCell headerReasonCell = headerRow.createCell(columnIndex++); //Muafiyet Sebebi
        XSSFCell headerUrgentPhoneCell = headerRow.createCell(columnIndex++); //Acil İrtibat Numarası
        XSSFCell headerUrgentFullNameCell = headerRow.createCell(columnIndex++); //Acil İrtibat Kişisi
        XSSFCell headerUrgentCell = headerRow.createCell(columnIndex++); //Acil İrtibat Yakınlığı
        XSSFCell headerRetiredCell = headerRow.createCell(columnIndex++); //Emeklilik
        XSSFCell headerDisabledCell = headerRow.createCell(columnIndex++); //Engelli
        XSSFCell headerCoWorkingCell = headerRow.createCell(columnIndex++); //Eş Çalışma Durumu
        XSSFCell headerDrivingCell = headerRow.createCell(columnIndex++); //Ehliyet
        XSSFCell headerJobQuafCell = headerRow.createCell(columnIndex++); //Mesleki Yeterlilik Belgesi
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++); //Açıklama
        XSSFCell headerCreatedByCell = headerRow.createCell(columnIndex++); //Oluşturan
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++); //Oluşturma Tarihi



        headerTcCell.setCellStyle(headerCellStyle);
        headerNameCell.setCellStyle(headerCellStyle);
        headerSurnameCell.setCellStyle(headerCellStyle);
        headerMailCell.setCellStyle(headerCellStyle);
        headerCompanyCell.setCellStyle(headerCellStyle);
        headerDepartmentCell.setCellStyle(headerCellStyle);
        headerTitleCell.setCellStyle(headerCellStyle);
        headerComPhoneCell.setCellStyle(headerCellStyle);
        headerShortCodeCell.setCellStyle(headerCellStyle);
        headerJobStartDateCell.setCellStyle(headerCellStyle);
        headerPersonelPhoneCell.setCellStyle(headerCellStyle);
        headerBirthDateCell.setCellStyle(headerCellStyle);
        headerIBANCell.setCellStyle(headerCellStyle);
        headerInsCompanyCell.setCellStyle(headerCellStyle);
        headerInsDepartmentCell.setCellStyle(headerCellStyle);
        headerInsTitleCell.setCellStyle(headerCellStyle);
        headerActivatedCell.setCellStyle(headerCellStyle);
        headerQuitJobDateCell.setCellStyle(headerCellStyle);
        headerGenderCell.setCellStyle(headerCellStyle);
        headerBloodGroupCell.setCellStyle(headerCellStyle);
        headerEducationCell.setCellStyle(headerCellStyle);
        headerGraduatedCell.setCellStyle(headerCellStyle);
        headerGradSectionCell.setCellStyle(headerCellStyle);
        headerTCGovCell.setCellStyle(headerCellStyle);
        headerBirthCityCell.setCellStyle(headerCellStyle);
        headerBirthDistrictCell.setCellStyle(headerCellStyle);
        headerAddressCell.setCellStyle(headerCellStyle);
        headerMilitaryCell.setCellStyle(headerCellStyle);
        headerReasonCell.setCellStyle(headerCellStyle);
        headerUrgentPhoneCell.setCellStyle(headerCellStyle);
        headerUrgentFullNameCell.setCellStyle(headerCellStyle);
        headerUrgentCell.setCellStyle(headerCellStyle);
        headerRetiredCell.setCellStyle(headerCellStyle);
        headerDisabledCell.setCellStyle(headerCellStyle);
        headerCoWorkingCell.setCellStyle(headerCellStyle);
        headerDrivingCell.setCellStyle(headerCellStyle);
        headerJobQuafCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerCreatedByCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);


        headerTcCell.setCellValue("Tc Kimlik Nu");
        headerNameCell.setCellValue("Ad");
        headerSurnameCell.setCellValue("Soyad");
        headerMailCell.setCellValue("Mail");
        headerCompanyCell.setCellValue("Şirket");
        headerDepartmentCell.setCellValue("Birim");
        headerTitleCell.setCellValue("Unvan");
        headerComPhoneCell.setCellValue("Şirket Telefonu");
        headerShortCodeCell.setCellValue("Kısa Kod");
        headerJobStartDateCell.setCellValue("İşe Başlangıç Tarihi");
        headerPersonelPhoneCell.setCellValue("Şahsi Telefon");
        headerBirthDateCell.setCellValue("Doğum Tarihi");
        headerIBANCell.setCellValue("IBAN");
        headerInsCompanyCell.setCellValue("Sigortalı Olduğu Şirket");
        headerInsDepartmentCell.setCellValue("Sigorta Olduğu Birim");
        headerInsTitleCell.setCellValue("Sigortalı Olduğu Unvan");
        headerActivatedCell.setCellValue("Aktiflik");
        headerQuitJobDateCell.setCellValue("İşten Ayrılma Tarihi");
        headerGenderCell.setCellValue("Cinsiyet");
        headerBloodGroupCell.setCellValue("Kan Grubu");
        headerEducationCell.setCellValue("Eğitim Durumu");
        headerGraduatedCell.setCellValue("Mezun Olduğu Kurum");
        headerGradSectionCell.setCellValue("Mezun Olduğu Bölüm");
        headerTCGovCell.setCellValue("TC Vatandaşlığı");
        headerBirthCityCell.setCellValue("Doğduğu İl");
        headerBirthDistrictCell.setCellValue("Doğduğu İlçe");
        headerAddressCell.setCellValue("Açık Adres");
        headerMilitaryCell.setCellValue("Askerlik Durumu");
        headerReasonCell.setCellValue("Muafiyet Sebebi");
        headerUrgentPhoneCell.setCellValue("Acil İrtibat Numarası");
        headerUrgentFullNameCell.setCellValue("Acil Kişisi");
        headerUrgentCell.setCellValue("Acil Yakınlığı");
        headerRetiredCell.setCellValue("Emeklilik");
        headerDisabledCell.setCellValue("Engel Durumu");
        headerCoWorkingCell.setCellValue("Eş Çalışma Durumu");
        headerDrivingCell.setCellValue("Ehliyet");
        headerJobQuafCell.setCellValue("İşten Ayrılma Tarihi");
        headerDescriptionCell.setCellValue("Açıklama");
        headerCreatedByCell.setCellValue("Oluşturan");
        headerCreatedDateCell.setCellValue("Oluşturma Tarihi");

        for (User user : Users) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell TcCell = row.createCell(columnIndex++);
            XSSFCell NameCell = row.createCell(columnIndex++);
            XSSFCell SurnameCell = row.createCell(columnIndex++);
            XSSFCell MailCell = row.createCell(columnIndex++);
            XSSFCell CompanyCell = row.createCell(columnIndex++);
            XSSFCell DepartmentCell = row.createCell(columnIndex++);
            XSSFCell TitleCell = row.createCell(columnIndex++);
            XSSFCell ComPhoneCell = row.createCell(columnIndex++);
            XSSFCell ShortCodeCell = row.createCell(columnIndex++);
            XSSFCell JobStartDateCell = row.createCell(columnIndex++);
            XSSFCell PersonelPhoneCell = row.createCell(columnIndex++);
            XSSFCell BirthDateCell = row.createCell(columnIndex++);
            XSSFCell IBANCell = row.createCell(columnIndex++);
            XSSFCell InsCompanyCell = row.createCell(columnIndex++);
            XSSFCell InsDepartmentCell = row.createCell(columnIndex++);
            XSSFCell InsTitleCell = row.createCell(columnIndex++);
            XSSFCell ActivatedCell = row.createCell(columnIndex++);
            XSSFCell QuitJobDateCell = row.createCell(columnIndex++);
            XSSFCell GenderCell = row.createCell(columnIndex++);
            XSSFCell BloodGroupCell = row.createCell(columnIndex++);
            XSSFCell EducationCell = row.createCell(columnIndex++);
            XSSFCell GraduatedCell = row.createCell(columnIndex++);
            XSSFCell GradSectionCell = row.createCell(columnIndex++);
            XSSFCell TCGovCell = row.createCell(columnIndex++);
            XSSFCell BirthCityCell = row.createCell(columnIndex++);
            XSSFCell BirthDistrictCell = row.createCell(columnIndex++);
            XSSFCell AddressCell = row.createCell(columnIndex++);
            XSSFCell MilitaryCell = row.createCell(columnIndex++);
            XSSFCell ReasonCell = row.createCell(columnIndex++);
            XSSFCell UrgentPhoneCell = row.createCell(columnIndex++);
            XSSFCell UrgentFullNameCell = row.createCell(columnIndex++);
            XSSFCell UrgentCell = row.createCell(columnIndex++);
            XSSFCell RetiredCell = row.createCell(columnIndex++);
            XSSFCell DisabledCell = row.createCell(columnIndex++);
            XSSFCell CoWorkingCell = row.createCell(columnIndex++);
            XSSFCell DrivingCell = row.createCell(columnIndex++);
            XSSFCell JobQuafCell = row.createCell(columnIndex++);
            XSSFCell DescriptionCell = row.createCell(columnIndex++);
            XSSFCell CreatedByCell = row.createCell(columnIndex++);
            XSSFCell CreatedDateCell = row.createCell(columnIndex++);


            TcCell.setCellValue(user.getTck());
            NameCell.setCellValue(user.getFirstName());
            SurnameCell.setCellValue(user.getLastName());
            MailCell.setCellValue(user.getEmail());

            if (user.getSirket() != null) {
                CompanyCell.setCellValue(user.getSirket().getLabel());
            }
            if (user.getBirim() != null) {
                DepartmentCell.setCellValue(user.getBirim().getLabel());
            }
            if (user.getUnvan() != null) {
                TitleCell.setCellValue(user.getUnvan().getLabel());
            }
            ComPhoneCell.setCellValue(user.getPhone());
            ShortCodeCell.setCellValue(user.getShort_code());

            if (user.getStartDate() != null) {
                JobStartDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(user.getStartDate()));
            }
            PersonelPhoneCell.setCellValue(user.getPhone2());

            if (user.getBirthDate() != null) {
                BirthDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(user.getBirthDate()));
            }
            IBANCell.setCellValue(user.getIban());
            if (user.getSgksirket() != null) {
                InsCompanyCell.setCellValue(user.getSgksirket().getLabel());
            }
            if (user.getSgkbirim() != null) {
                InsDepartmentCell.setCellValue(user.getSgkbirim().getLabel());
            }
            if (user.getSgkunvan() != null) {
                InsTitleCell.setCellValue(user.getSgkunvan().getLabel());
            }
            ActivatedCell.setCellValue(user.getActivated().toString());

            if (user.getEndDate() != null) {
                QuitJobDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(user.getEndDate()));
            }
            if (user.getCinsiyet() != null) {
                GenderCell.setCellValue(user.getCinsiyet().getLabel());
            }
            BloodGroupCell.setCellValue(user.getKan());

            if (user.getEgitim() != null) {
                EducationCell.setCellValue(user.getEgitim().getLabel());
            }
            GraduatedCell.setCellValue(user.getMezunkurum());
            GradSectionCell.setCellValue(user.getMezunbolum());
            TCGovCell.setCellValue(user.getTcv().toString());
            BirthCityCell.setCellValue(user.getCity());
            BirthDistrictCell.setCellValue(user.getDistrict());
            AddressCell.setCellValue(user.getAdres());
            if (user.getAskerlik() != null) {
                MilitaryCell.setCellValue(user.getAskerlik().getLabel());
            }
            ReasonCell.setCellValue(user.getMuaf());
            UrgentPhoneCell.setCellValue(user.getAcilno());
            UrgentFullNameCell.setCellValue(user.getAciladsoyad());
            UrgentCell.setCellValue(user.getAcilyakinlik());

            RetiredCell.setCellValue(user.getEmekli().toString());
            DisabledCell.setCellValue(user.getEngelli().toString());
            CoWorkingCell.setCellValue(user.getEscalisma().toString());
            if (user.getEhliyet() != null) {
                DrivingCell.setCellValue(user.getEhliyet().getLabel());
            }
            JobQuafCell.setCellValue(user.getMyb().toString());
            DescriptionCell.setCellValue(user.getAciklama());

            if(user.getCreatedBy() != null) {
                CreatedByCell.setCellValue(user.getCreatedBy().getFullName());
            }
            if (user.getCreatedDate() != null) {
                CreatedDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(user.getCreatedDate()));
            }
        }

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
    public void newPerson(String tc, Instant baslangic, Instant dogum, String cep, String ad, String soyad, User assigner, String unvan, String sgkSirket) throws Exception {
        if (tc.length() > 11) {
            System.out.println("Lütfen 11 haneli TC giriniz...");
        }
        AttributeValue attrUnvan = new AttributeValue();
        AttributeValue attrSirket = new AttributeValue();
        List<AttributeValue> attributeValues = attributeValueRepository.findAll();
        for (AttributeValue attributeValue : attributeValues) {
            if (attributeValue.getId().equals(unvan)) attrUnvan = attributeValue;
            if (attributeValue.getId().equals(sgkSirket)) attrSirket = attributeValue;
        }
        String login = ad.toLowerCase() + "." + soyad.toLowerCase();
        String password = passwordEncoder.encode("12345");
        String mail = assigner.getEmail();
        String gonderilecekmail = assigner.getEposta();

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setTck(tc);
        user.setFirstName(ad);
        user.setLastName(soyad);
        user.setEmail(mail);
        user.setEposta(gonderilecekmail);
        user.setBirthDate(dogum);
        user.setStartDate(baslangic);
        user.setSgkStartDate(baslangic);
        user.setPhone2(cep);
        user.setIzinGoruntuleme(true);
        user.setSirket(assigner.getSirket());
        user.setBirim(assigner.getBirim());
        user.setSgkbirim(assigner.getBirim());
        user.setUnvan(attrUnvan);
        user.setSgksirket(attrSirket);
        user.setSgkunvan(attrUnvan);
        user.setActivated(false);
        repository.save(user);
    }
}
