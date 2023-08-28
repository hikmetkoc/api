package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.Offer;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.repository.OfferRepository;
import tr.com.meteor.crm.repository.QuoteRepository;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
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
public class OfferService extends GenericIdNameAuditingEntityService<Offer, UUID, OfferRepository> {
    private final MailService mailService;

    public OfferService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                        BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                        BaseConfigurationService baseConfigurationService, OfferRepository repository, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Offer.class, repository);
        this.mailService = mailService;
    }


    public byte[] generateExcelOfferReportForUser(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
            )
        );

        List<Offer> activities = getData(null, request, false).getBody();
        Map<Long, List<Offer>> userIdActivities = new HashMap<>();

        for (Offer offer : activities) {
            List<Offer> userActivities = new ArrayList<>();

            if (userIdActivities.containsKey(offer.getOwner().getId())) {
                userActivities = userIdActivities.get(offer.getOwner().getId());
            }

            userActivities.add(offer);

            userIdActivities.put(offer.getOwner().getId(), userActivities);
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
        XSSFCell taskSubjectHeaderCell = headerRow.createCell(columnIndex++);  //Konu
        XSSFCell taskSubjdescHeaderCell = headerRow.createCell(columnIndex++);  //Konu Başlığı
        XSSFCell usernameHeaderCell = headerRow.createCell(columnIndex++);  //İşlem Yapan
        XSSFCell activityStatusHeaderCell = headerRow.createCell(columnIndex++);    //Durum
        XSSFCell planningHeaderCell = headerRow.createCell(columnIndex++);   //Planlanan Tarih
        XSSFCell creatorHeaderCell = headerRow.createCell(columnIndex++);  //Oluşturan
        XSSFCell creatorTimeHeaderCell = headerRow.createCell(columnIndex++);      //Oluşturma Tarihi
        XSSFCell lastModifyHeaderCell = headerRow.createCell(columnIndex++);    //Son Düzenleyen
        XSSFCell lastModifyTimeHeaderCell = headerRow.createCell(columnIndex++);     //Son Düzenleme Tarihi
        XSSFCell descriptionHeaderCell = headerRow.createCell(columnIndex++); //Açıklama

        taskIdHeaderCell.setCellStyle(headerCellStyle);
        taskIdHeaderCell.setCellValue("Task ID");
        taskSubjectHeaderCell.setCellStyle(headerCellStyle);
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
        descriptionHeaderCell.setCellValue("Açıklama");

        for (User user : hierarchicalUsers) {
            if (!userIdActivities.containsKey(user.getId())) continue;
            List<Offer> sortedUserActivities = userIdActivities.get(user.getId()).stream()
                .sorted(Comparator.comparing(Offer::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
            for (Offer offer : sortedUserActivities) {
                XSSFRow row = sheet.createRow(rowIndex++);
                columnIndex = 1;

                XSSFCell taskIdCell = row.createCell(columnIndex++);  //Task ID
                XSSFCell usernameCell = row.createCell(columnIndex++);  //İşlem Yapan
                XSSFCell taskSubjectCell = row.createCell(columnIndex++);  //Konu
                XSSFCell taskSubjdescCell = row.createCell(columnIndex++);  //Konu Başlığı
                XSSFCell offerStatusCell = row.createCell(columnIndex++);    //Durum
                XSSFCell planningCell = row.createCell(columnIndex++);   //Planlanan Tarih
                XSSFCell creatorCell = row.createCell(columnIndex++);  //Oluşturan
                XSSFCell creatorTimeCell = row.createCell(columnIndex++);      //Oluşturma Tarihi
                XSSFCell lastModifyCell = row.createCell(columnIndex++);    //Son Düzenleyen
                XSSFCell lastModifyTimeCell = row.createCell(columnIndex++);     //Son Düzenleme Tarihi
                XSSFCell descriptionCell = row.createCell(columnIndex++); //Açıklama

                usernameCell.setCellValue(user.getFullName());
                taskIdCell.setCellValue(offer.getCustomer().getId().toString());

                /*if (offer.getApprovalStatus() != null) {
                    offerStatusCell.setCellValue(offer.getApprovalStatus().getLabel());
                }*/

                if (offer.getCreatedBy() != null) {
                    creatorCell.setCellValue(offer.getCreatedBy().getFullName());
                }

                if (offer.getCreatedDate() != null) {
                    creatorTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()).format(offer.getCreatedDate()));
                }

                if (offer.getLastModifiedBy() != null) {
                    lastModifyCell.setCellValue(offer.getLastModifiedBy().getFullName());
                }

                if (offer.getLastModifiedDate() != null) {
                    lastModifyTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()).format(offer.getLastModifiedDate()));
                }

                if (offer.getDescription() != null) {
                    descriptionCell.setCellValue(offer.getDescription());
                }
                if (offer.getSubject() != null) {
                    taskSubjectCell.setCellValue(offer.getSubject());
                }
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
