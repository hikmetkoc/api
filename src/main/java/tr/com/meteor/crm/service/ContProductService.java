package tr.com.meteor.crm.service;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.ContProduct;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.ContProductRepository;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContProductService extends GenericIdNameAuditingEntityService<ContProduct, UUID, ContProductRepository> {
    private final ContractRepository contractRepository;
    private final MailService mailService;

    public ContProductService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                              BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                              BaseConfigurationService baseConfigurationService, ContProductRepository repository,
                              ContractRepository contractRepository, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            ContProduct.class, repository);
        this.contractRepository = contractRepository;
        this.mailService = mailService;
    }

    public void updateCheckIn(CheckInOutDTO checkInOutDTO) throws Exception {
        User currentUser = getCurrentUser();
        //Optional<CustomActivity> activity = repository.findById(checkInOutDTO.getActivityId());

        /*if (!activity.isPresent()) {
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

    public void updateCheckOut(CheckInOutDTO checkInOutDTO) throws Exception {
        /*User currentUser = getCurrentUser();
        Optional<CustomActivity> activity = repository.findById(checkInOutDTO.getActivityId());

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
        /*List<String> rolesWithTargetReportOperation = baseRoleService.getRoles().stream()
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
        }*/
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

        List<ContProduct> activities = getData(null, request, false).getBody();
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
        Map<Long, List<ContProduct>> userIdActivities = new HashMap<>();

        for (ContProduct activity : activities) {
            List<ContProduct> userActivities = new ArrayList<>();

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
        /*XSSFCell taskSubjectHeaderCell = headerRow.createCell(columnIndex++);  //Konu
        XSSFCell taskSubjdescHeaderCell = headerRow.createCell(columnIndex++);  //Konu Başlığı
        XSSFCell usernameHeaderCell = headerRow.createCell(columnIndex++);  //İşlem Yapan
        XSSFCell activityStatusHeaderCell = headerRow.createCell(columnIndex++);    //Durum
        XSSFCell planningHeaderCell = headerRow.createCell(columnIndex++);   //Planlanan Tarih
        XSSFCell creatorHeaderCell = headerRow.createCell(columnIndex++);  //Oluşturan
        XSSFCell creatorTimeHeaderCell = headerRow.createCell(columnIndex++);      //Oluşturma Tarihi
        XSSFCell lastModifyHeaderCell = headerRow.createCell(columnIndex++);    //Son Düzenleyen
        XSSFCell lastModifyTimeHeaderCell = headerRow.createCell(columnIndex++);     //Son Düzenleme Tarihi
        XSSFCell descriptionHeaderCell = headerRow.createCell(columnIndex++); //Açıklama*/

        taskIdHeaderCell.setCellStyle(headerCellStyle);
        taskIdHeaderCell.setCellValue("Task ID");
        /*taskSubjectHeaderCell.setCellStyle(headerCellStyle);
        taskSubjectHeaderCell.setCellValue("Konu");
        taskSubjdescHeaderCell.setCellStyle(headerCellStyle);
        taskSubjdescHeaderCell.setCellValue("Konu Başlığı");
        usernameHeaderCell.setCellStyle(headerCellStyle);
        usernameHeaderCell.setCellValue("İşlem Yapan");
        activityStatusHeaderCell.setCellStyle(headerCellStyle);
        activityStatusHeaderCell.setCellValue("Durum");
        planningHeaderCell.setCellStyle(headerCellStyle);
        planningHeaderCell.setCellValue("Planlanan Tarih");
        creatorHeaderCell.setCellStyle(headerCellStyle);
        creatorHeaderCell.setCellValue("Oluşturan");
        creatorTimeHeaderCell.setCellStyle(headerCellStyle);
        creatorTimeHeaderCell.setCellValue("Oluşturma Zamanı");
        lastModifyHeaderCell.setCellStyle(headerCellStyle);
        lastModifyHeaderCell.setCellValue("Son Düzenleyen");
        lastModifyTimeHeaderCell.setCellStyle(headerCellStyle);
        lastModifyTimeHeaderCell.setCellValue("Son Düzenleme Tarihi");
        descriptionHeaderCell.setCellStyle(headerCellStyle);
        descriptionHeaderCell.setCellValue("Açıklama");*/

        for (User user : hierarchicalUsers) {
            if (!userIdActivities.containsKey(user.getId())) continue;
            List<ContProduct> sortedUserActivities = userIdActivities.get(user.getId()).stream()
                .sorted(Comparator.comparing(ContProduct::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
            for (ContProduct activity : sortedUserActivities) {
                //Quote quote = activityIdQuoteMap.get(activity.getId());
                XSSFRow row = sheet.createRow(rowIndex++);
                columnIndex = 1;

                XSSFCell taskIdCell = row.createCell(columnIndex++);  //Task ID
                /*XSSFCell usernameCell = row.createCell(columnIndex++);  //İşlem Yapan
                XSSFCell taskSubjectCell = row.createCell(columnIndex++);  //Konu
                XSSFCell taskSubjdescCell = row.createCell(columnIndex++);  //Konu Başlığı
                XSSFCell activityStatusCell = row.createCell(columnIndex++);    //Durum
                XSSFCell planningCell = row.createCell(columnIndex++);   //Planlanan Tarih
                XSSFCell creatorCell = row.createCell(columnIndex++);  //Oluşturan
                XSSFCell creatorTimeCell = row.createCell(columnIndex++);      //Oluşturma Tarihi
                XSSFCell lastModifyCell = row.createCell(columnIndex++);    //Son Düzenleyen
                XSSFCell lastModifyTimeCell = row.createCell(columnIndex++);     //Son Düzenleme Tarihi
                XSSFCell descriptionCell = row.createCell(columnIndex++); //Açıklama*/

                //usernameCell.setCellValue(user.getFullName());
                taskIdCell.setCellValue(activity.getBuy().getId().toString());

                /*if (activity.getStatus() != null) {
                    activityStatusCell.setCellValue(activity.getStatus().getLabel());
                }

                if (activity.getCreatedBy() != null) {
                    creatorCell.setCellValue(activity.getCreatedBy().getFullName());
                }

                if (activity.getCreatedDate() != null) {
                    creatorTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()).format(activity.getCreatedDate()));
                }

                if (activity.getCheckOutTime() != null) {
                    planningCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()).format(activity.getCheckOutTime()));
                }

                if (activity.getLastModifiedBy() != null) {
                    lastModifyCell.setCellValue(activity.getLastModifiedBy().getFullName());
                }

                if (activity.getLastModifiedDate() != null) {
                    lastModifyTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()).format(activity.getLastModifiedDate()));
                }*/

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


                /*if (activity.getDescription() != null) {
                    descriptionCell.setCellValue(activity.getDescription());
                }
                if (activity.getSubject() != null) {
                    taskSubjectCell.setCellValue(activity.getSubject());
                }
                if (activity.getSubjdesc() != null) {
                    taskSubjdescCell.setCellValue(activity.getSubjdesc());
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
