package tr.com.meteor.crm.service;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.domain.VocationDay;
import tr.com.meteor.crm.repository.HolUserRepository;
import tr.com.meteor.crm.repository.VocationDayRepository;
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
public class VocationDayService extends GenericIdNameAuditingEntityService<VocationDay, UUID, VocationDayRepository> {

    public VocationDayService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                              BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                              BaseConfigurationService baseConfigurationService,
                              VocationDayRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            VocationDay.class, repository);
    }

    public byte[] generateExcelVocationDayReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.Or(
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<VocationDay> VocationDays = getData(null, request, false).getBody();

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

        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);
        XSSFCell headerHolStartCell = headerRow.createCell(columnIndex++);
        XSSFCell headerHolEndCell = headerRow.createCell(columnIndex++);
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);

        headerOwnerCell.setCellStyle(headerCellStyle);
        headerHolStartCell.setCellStyle(headerCellStyle);
        headerHolEndCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);

        headerOwnerCell.setCellValue("İzin Gününü Oluşturan");
        headerHolStartCell.setCellValue("Tatil Başlangıç Tarihi");
        headerHolEndCell.setCellValue("Tatil Bitiş Tarihi");
        headerDescriptionCell.setCellValue("Açıklama");
        headerCreatedDateCell.setCellValue("Oluşturma Tarihi");

        for (VocationDay vocationDay : VocationDays) {
            columnIndex = 1;

            XSSFRow Row = sheet.createRow(rowIndex++);

            XSSFCell ownerCell = Row.createCell(columnIndex++);
            XSSFCell holStartCell = Row.createCell(columnIndex++);
            XSSFCell holEndCell = Row.createCell(columnIndex++);
            XSSFCell descriptionCell = Row.createCell(columnIndex++);
            XSSFCell createdDateCell = Row.createCell(columnIndex++);

            if (vocationDay.getOwner() != null) {
                ownerCell.setCellValue(vocationDay.getOwner().getFullName());
            }

            if (vocationDay.getHolStart() != null) {
                holStartCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(vocationDay.getHolStart()));
            }

            if (vocationDay.getHolEnd() != null) {
                holEndCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(vocationDay.getHolEnd()));
            }
            descriptionCell.setCellValue(vocationDay.getDescription());

            if (vocationDay.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(vocationDay.getCreatedDate()));
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
