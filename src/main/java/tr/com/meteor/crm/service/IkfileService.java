package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.repository.IkfileRepository;
import tr.com.meteor.crm.repository.QuoteRepository;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.service.dto.QuoteSendDocumentDTO;
import tr.com.meteor.crm.utils.Documents;
import tr.com.meteor.crm.utils.configuration.Configurations;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.jasper.PdfTemplates;
import tr.com.meteor.crm.utils.jasper.rest.errors.BadRequestAlertException;
import tr.com.meteor.crm.utils.jasper.rest.errors.RecordNotFoundException;
import tr.com.meteor.crm.utils.operations.Operations;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class IkfileService extends GenericIdNameAuditingEntityService<Ikfile, UUID, IkfileRepository> {

    private static Map<String, String> documents = new HashMap<>();

    static {
        for (Documents d : Documents.values()) {
            documents.put(d.name(), d.getLabel());
        }
    }
    private final QuoteRepository quoteRepository;
    private final ContractRepository contractRepository;
    private final MailService mailService;

    public IkfileService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                         BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                         BaseConfigurationService baseConfigurationService, IkfileRepository repository,
                         QuoteRepository quoteRepository, ContractRepository contractRepository, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Ikfile.class, repository);
        this.quoteRepository = quoteRepository;
        this.contractRepository = contractRepository;
        this.mailService = mailService;
    }

    public void updateCheckIn(CheckInOutDTO checkInOutDTO) throws Exception {
        /*User currentUser = getCurrentUser();
        Optional<Ikfile> activity = repository.findById(checkInOutDTO.getActivityId());

        if (!activity.isPresent()) {
            throw new RecordNotFoundException(entityClass.getSimpleName(), checkInOutDTO);
        }

        if (!((currentUser.getId()) == (activity.get().getOwner().getId()))) {
            throw new BadRequestAlertException("Bu aktivite sze ait değil.", null, "idexists");
        }

        //activity.get().setCheckInTime(checkInOutDTO.getTime());
        //activity.get().setCheckInLongitude(checkInOutDTO.getLongitude());
        //activity.get().setCheckInLatitude(checkInOutDTO.getLatitude());

        add(currentUser, activity.get());*/
    }

    public Map<String, String> getDocumentList() {
        return documents;
    }

    @Async
    public void sendDocuments(QuoteSendDocumentDTO dto, User user) throws Exception {
        Optional<Ikfile> ikfile = repository.findById(dto.getIkfileId());

        if (!ikfile.isPresent()) {
            throw new Exception("Belge bulunamadı.");
        }

        User user1 = ikfile.get().getUser();

        if (user1 == null) {
            throw new Exception("Bağlı personel bulunamadı.");
        }

        Set<String> mails = new HashSet<>();
        /*if (StringUtils.isNotBlank(customer.getEmail())) {
            mails.add(customer.getEmail());
        }*/

        /*Optional<Contact> contact = contactRepository.findAllByCustomerId(customer.getId())
            .stream()
            .filter(x -> x.getType() != null && x.getType().getId().equals(ContactType.BIRINCIL.getId()))
            .findFirst();*/

        /*if (contact.isPresent() && StringUtils.isNotBlank(contact.get().getEmail())) {
            mails.add(contact.get().getEmail());
        }

        if (mails.isEmpty()) {
            throw new Exception("Müşteri ve ya birincil kişiye ait mail adresi bulunamadı.");
        }*/

        mails.add("hikmet@meteorpetrol.com");

        Map<String, InputStreamSource> map = new HashMap<>();
        //boolean sendBackOffice = false;
        for (Documents document : dto.getDocs()) {
            switch (document) {
                case OTOBIL_BILGILENDIRME:
                    map.put(document.getFileName(), PdfTemplates.otobilBilgilendirme(user));
                    break;
                /*case OTOBIL_SOZLESME:
                    //sendBackOffice = true;
                    map.put(document.getFileName(), PdfTemplates.otobilSozlesme(
                        user1.getName(),
                        "% " + quote.get().getDiscountDiesel(),
                        "% " + quote.get().getDiscountGasoline(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.now())
                    ));
                    break;
                case METEORCARD_SOZLESME:
                    //sendBackOffice = true;
                    map.put(document.getFileName(), PdfTemplates.meteorcardSozlesme(
                        customer.getName(),
                        addressStr,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.now())
                    ));
                    break;
                case OTOFILO_SOZLESME:
                    //sendBackOffice = true;
                    map.put(document.getFileName(), PdfTemplates.otofiloSozlesme(
                        customer.getName(),
                        addressStr,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.now())
                    ));
                    break;*/
                case IK_REPORT:
                    //sendBackOffice = true;
                    map.put(document.getFileName(), PdfTemplates.ikReport(user));
                    break;
                case E_ARSIV_TALEP_DILEKCESI:
                case FIRMA_BILGILERI_FORMU:
                case OTOBIL_ARAC_KAYIT_FORMU:
                case OTOFILO_KAYIT_FORMU:
                case METEORCARD_AKTIVASYON_TALEP_FORMU:
                case METEORCARD_TALEP_FORMU:
                    map.put(document.getFileName(), document.getFile());
                    break;
            }
        }

        if (map.isEmpty()) {
            throw new Exception("Gönderilecek dosya seçilmedi.");
        }

        Set<String> ccs = new HashSet<>();
        //String ccMail = baseConfigurationService.getConfigurationById(Configurations.SOZLESME_CC.getId()).getStoredValue();

        /*if (sendBackOffice && StringUtils.isNotBlank(ccMail)) {
            ccs.add(ccMail);
        }*/

        /*if (customer.getOwner() != null && StringUtils.isNotBlank(customer.getOwner().getEmail())) {
            ccs.add(customer.getOwner().getEmail());
        }*/

        mailService.sendWithAttachments(mails, ccs, null, "Dökümanlar", "Dökümanlar ektedir.",
            true, false, map
        );
    }
    public void updateCheckOut(CheckInOutDTO checkInOutDTO) throws Exception {
        /*User currentUser = getCurrentUser();
        Optional<Activity> activity = repository.findById(checkInOutDTO.getActivityId());

        if (!activity.isPresent()) {
            throw new RecordNotFoundException(entityClass.getSimpleName(), checkInOutDTO);
        }

        if (!((currentUser.getId()) == (activity.get().getOwner().getId()))) {
            throw new BadRequestAlertException("Bu aktivite size ait değil.", null, "idexists");
        }

        activity.get().setCheckOutTime(checkInOutDTO.getTime());
        //activity.get().setCheckOutLongitude(checkInOutDTO.getLongitude());
        //activity.get().setCheckOutLatitude(checkInOutDTO.getLatitude());

        add(currentUser, activity.get());*/
    }

    @Scheduled(cron = "0 0 1 * * 5")
    public void sendFridayTargetReport() throws Exception {
        Instant startDate = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant endDate = LocalDate.now().plusDays(-7).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        sendTargetReports(startDate, endDate);
    }

    @Scheduled(cron = "0 0 1 1 * *")
    public void sendMonthlyTargetReport() throws Exception {
        Instant startDate = YearMonth.now().atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant endDate = YearMonth.now().atEndOfMonth().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        sendTargetReports(startDate, endDate);
    }

    public void sendTargetReports(Instant startDate, Instant endDate) throws Exception {
        List<String> rolesWithTargetReportOperation = baseRoleService.getRoles().stream()
            .filter(x -> x.getOperations().stream().anyMatch(y -> y.getId().equals(Operations.HEDEF_RAPORU_ALICISI.getId())))
            .map(Role::getId)
            .collect(Collectors.toList());

        List<User> reportUsers = baseUserService.getAllUsers().stream()
            .filter(x -> x.getRoles().stream().anyMatch(y -> rolesWithTargetReportOperation.contains(y.getId())))
            .collect(Collectors.toList());

        for (User user : reportUsers) {
            if (StringUtils.isBlank(user.getEmail()) || !mailService.isValidEmailAddress(user.getEmail()) || user.getEmail().contains("localhost"))
                continue;

            ByteArrayResource excelFile = new ByteArrayResource(generateExcelActivityReportForUser(user, startDate, endDate));

            String dateString = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(Instant.now());
            String fileName = "Ziyaret Raporu " + dateString + ".xlsx";

            mailService.sendWithAttachment(Collections.singleton(user.getEmail()), null, null, "Ziyaret Raporu " + dateString,
                dateString + " tarihine ait hedef raporu ektedir.", true, false, fileName, excelFile);
        }
    }



    public byte[] generateExcelActivityReportForUser(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
            )
        );

        List<Ikfile> activities = getData(null, request, false).getBody();
        /*List<Quote> quotes = quoteRepository.findAllByActivityIdIn(activities.stream().map(Activity::getId).collect(Collectors.toList()));
        List<Contract> contracts = contractRepository.findAllByQuoteIdIn(quotes.stream().map(Quote::getId).collect(Collectors.toList()));

        Map<UUID, Quote> activityIdQuoteMap = new HashMap<>();
        Map<UUID, Contract> quoteIdContractMap = new HashMap<>();

        for (Quote quote : quotes) {
            if (activityIdQuoteMap.containsKey(quote.getActivity().getId())) {
                Quote q = activityIdQuoteMap.get(quote.getActivity().getId());

                if (q.getCreatedDate().isAfter(quote.getCreatedDate())) {
                    activityIdQuoteMap.put(quote.getActivity().getId(), quote);
                }
            } else {
                activityIdQuoteMap.put(quote.getActivity().getId(), quote);
            }
        }

        for (Contract contract : contracts) {
            if (activityIdQuoteMap.containsKey(contract.getQuote().getId())) {
                Contract q = quoteIdContractMap.get(contract.getQuote().getId());

                if (q.getCreatedDate().isAfter(contract.getCreatedDate())) {
                    quoteIdContractMap.put(contract.getQuote().getId(), contract);
                }
            } else {
                quoteIdContractMap.put(contract.getQuote().getId(), contract);
            }
        }
*/
        Map<Long, List<Ikfile>> userIdActivities = new HashMap<>();

        for (Ikfile activity : activities) {
            List<Ikfile> userActivities = new ArrayList<>();

            if (userIdActivities.containsKey(activity.getOwner().getId())) {
                userActivities = userIdActivities.get(activity.getOwner().getId());
            }

            userActivities.add(activity);

            userIdActivities.put(activity.getOwner().getId(), userActivities);
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        sheet.setColumnWidth(0, 200);
        sheet.createRow(0).setHeight((short) 200);

        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);

        int rowIndex = 1;
        int columnIndex = 1;

        XSSFRow headerRow = sheet.createRow(rowIndex++);

        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(boldFont);
        headerCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 0}, null));
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCell taskIdHeaderCell = headerRow.createCell(columnIndex++);  //TASK ID
        XSSFCell usernameHeaderCell = headerRow.createCell(columnIndex++);  //İşlem Yapan
        XSSFCell creatorHeaderCell = headerRow.createCell(columnIndex++);  //Oluşturan
        XSSFCell creatorTimeHeaderCell = headerRow.createCell(columnIndex++);      //Oluşturma Tarihi
        XSSFCell lastModifyHeaderCell = headerRow.createCell(columnIndex++);    //Son Düzenleyen
        XSSFCell lastModifyTimeHeaderCell = headerRow.createCell(columnIndex++);     //Son Düzenleme Tarihi

        taskIdHeaderCell.setCellStyle(headerCellStyle);
        taskIdHeaderCell.setCellValue("Task ID");
        usernameHeaderCell.setCellStyle(headerCellStyle);
        usernameHeaderCell.setCellValue("İşlem Yapan");
        creatorHeaderCell.setCellValue("Oluşturan");
        creatorTimeHeaderCell.setCellStyle(headerCellStyle);
        creatorTimeHeaderCell.setCellValue("Oluşturma Zamanı");
        lastModifyHeaderCell.setCellStyle(headerCellStyle);
        lastModifyHeaderCell.setCellValue("Son Düzenleyen");
        lastModifyTimeHeaderCell.setCellStyle(headerCellStyle);
        lastModifyTimeHeaderCell.setCellValue("Son Düzenleme Tarihi");

        for (User user : hierarchicalUsers) {
            if (!userIdActivities.containsKey(user.getId())) continue;
            List<Ikfile> sortedUserActivities = userIdActivities.get(user.getId()).stream()
                .sorted(Comparator.comparing(Ikfile::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
            for (Ikfile activity : sortedUserActivities) {
                XSSFRow row = sheet.createRow(rowIndex++);
                columnIndex = 1;

                XSSFCell taskIdCell = row.createCell(columnIndex++);  //Task ID
                XSSFCell usernameCell = row.createCell(columnIndex++);  //İşlem Yapan
                XSSFCell creatorCell = row.createCell(columnIndex++);  //Oluşturan
                XSSFCell creatorTimeCell = row.createCell(columnIndex++);      //Oluşturma Tarihi
                XSSFCell lastModifyCell = row.createCell(columnIndex++);    //Son Düzenleyen
                XSSFCell lastModifyTimeCell = row.createCell(columnIndex++);     //Son Düzenleme Tarihi

                usernameCell.setCellValue(user.getFullName());
                taskIdCell.setCellValue(activity.getUser().getId().toString());


                if (activity.getCreatedBy() != null) {
                    creatorCell.setCellValue(activity.getCreatedBy().getFullName());
                }

                if (activity.getCreatedDate() != null) {
                    creatorTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()).format(activity.getCreatedDate()));
                }


                if (activity.getLastModifiedBy() != null) {
                    lastModifyCell.setCellValue(activity.getLastModifiedBy().getFullName());
                }

                if (activity.getLastModifiedDate() != null) {
                    lastModifyTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()).format(activity.getLastModifiedDate()));
                }

                /*if (activity.getCreatedDate() != null) {
                    createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault()).format(activity.getCreatedDate()));
                }*/

                /*if (activity.getCheckInTime() != null) {
                    checkInCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault()).format(activity.getCheckInTime()));
                }*/

                /*if (activity.getCheckOutTime() != null) {
                    checkOutCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault()).format(activity.getCheckOutTime()));
                }*/

                /*if (activity.getCheckInTime() != null && activity.getCheckOutTime() != null) {
                    Duration duration = Duration.between(activity.getCheckInTime(), activity.getCheckOutTime());
                    durationCell.setCellValue(DurationFormatUtils.formatDuration(duration.toMillis(), "H 's' m 'dk'"));
                }*/

            }
        }

        for (int i = 0; i < 30; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
}
