package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.ExuseHoliday;
import tr.com.meteor.crm.domain.Holiday;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.ExuseHolidayRepository;
import tr.com.meteor.crm.repository.HolidayRepository;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExuseHolidayService extends GenericIdNameAuditingEntityService<ExuseHoliday, UUID, ExuseHolidayRepository> {

    public ExuseHolidayService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                               BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                               BaseConfigurationService baseConfigurationService,
                               ExuseHolidayRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            ExuseHoliday.class, repository);
    }

    public byte[] generateExcelHolidayReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.Or(
                    //Filter.FilterItem("assigner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<ExuseHoliday> Holidays = getData(null, request, false).getBody();

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
        headerCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 189, (byte) 215, (byte) 238}, null));
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCell headerUserCell = headerRow.createCell(columnIndex++);     //İzni Oluşturan
        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);     //Talep Eden
        XSSFCell headerAssignerCell = headerRow.createCell(columnIndex++);  //Yetkili
        XSSFCell headerStatusCell = headerRow.createCell(columnIndex++);    //Durum
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);   //Açıklama
        XSSFCell headerStartDateCell = headerRow.createCell(columnIndex++);       //Başlangıç Zamanı
        XSSFCell headerEndDateCell = headerRow.createCell(columnIndex++);       //Bitiş Zamanı
        XSSFCell headerComeDateCell = headerRow.createCell(columnIndex++);       //Dönüş Zamanı

        headerUserCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerAssignerCell.setCellStyle(headerCellStyle);
        headerStatusCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerStartDateCell.setCellStyle(headerCellStyle);
        headerEndDateCell.setCellStyle(headerCellStyle);
        headerComeDateCell.setCellStyle(headerCellStyle);

        headerUserCell.setCellValue("İzni Oluşturan");
        headerOwnerCell.setCellValue("Talep Eden");
        headerAssignerCell.setCellValue("Yetkili");
        headerStatusCell.setCellValue("Durum");
        headerDescriptionCell.setCellValue("Açıklama");
        headerStartDateCell.setCellValue("Başlangıç Tarihi");
        headerEndDateCell.setCellValue("Bitiş Tarihi");
        headerComeDateCell.setCellValue("Dönüş Tarihi");

        for (ExuseHoliday ExuseHoliday : Holidays) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell UserCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell assignerCell = row.createCell(columnIndex++);
            XSSFCell statusCell = row.createCell(columnIndex++);
            XSSFCell descriptionCell = row.createCell(columnIndex++);
            XSSFCell startDateCell = row.createCell(columnIndex++);
            XSSFCell endDateCell = row.createCell(columnIndex++);
            XSSFCell comeDateCell = row.createCell(columnIndex++);

            if (ExuseHoliday.getUser() != null) {
                UserCell.setCellValue(ExuseHoliday.getUser().getFullName());
            }
            if (ExuseHoliday.getOwner() != null) {
                ownerCell.setCellValue(ExuseHoliday.getOwner().getFullName());
            }

           if (ExuseHoliday.getAssigner() != null) {
                assignerCell.setCellValue(ExuseHoliday.getAssigner().getFullName());
            }

            if (ExuseHoliday.getApprovalStatus() != null) {
                statusCell.setCellValue(ExuseHoliday.getApprovalStatus().getLabel());
            }

            if (StringUtils.isNotBlank(ExuseHoliday.getDescription())) {
                descriptionCell.setCellValue(ExuseHoliday.getDescription());
            }

            if (ExuseHoliday.getStartDate() != null) {
                startDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(ExuseHoliday.getStartDate()));
            }

            if (ExuseHoliday.getEndDate() != null) {
                endDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(ExuseHoliday.getEndDate()));
            }

            if (ExuseHoliday.getComeDate() != null) {
                comeDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(ExuseHoliday.getLastModifiedDate()));
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
}
