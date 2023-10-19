package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.MotionSums;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.BehaviorRepository;
import tr.com.meteor.crm.repository.MotionSumsRepository;
import tr.com.meteor.crm.utils.attributevalues.MotionSumsStatus;
import tr.com.meteor.crm.utils.attributevalues.MotionSumsType;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static tr.com.meteor.crm.utils.ExcelUtils.addBorderToCell;

@Service
@Transactional(rollbackFor = Exception.class)
public class MotionSumsService extends GenericIdNameAuditingEntityService<MotionSums, UUID, MotionSumsRepository> {

    private final BehaviorRepository behaviorRepository;
    public MotionSumsService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                             BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                             BaseConfigurationService baseConfigurationService,
                             MotionSumsRepository customrepository, BehaviorRepository behaviorRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            MotionSums.class, customrepository);
        this.behaviorRepository = behaviorRepository;
    }

    public byte[] generateExcelMotionSumsWizardReportForUser(User currentUser, Instant startDate, Instant endDate) throws Exception {
        startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("type.id", FilterItem.Operator.EQUALS, MotionSumsType.KOLAY_AJANDA.getId()),
                Filter.FilterItem("dueTime", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("dueTime", FilterItem.Operator.LESS_THAN, endDate),
                Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
            )
        );

        List<MotionSums> MotionSumss = getData(null, request, false).getBody();

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
        addBorderToCell(headerCellStyle, BorderStyle.THICK);

        XSSFCellStyle usernameCellStyle = workbook.createCellStyle();
        usernameCellStyle.setFont(boldFont);
        usernameCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 189, (byte) 215, (byte) 238}, null));
        usernameCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        addBorderToCell(usernameCellStyle, BorderStyle.THICK);

        LocalDate start = startDate.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.atZone(ZoneId.systemDefault()).toLocalDate();

        Map<LocalDate, Integer> dateColumnIndexMap = new HashMap<>();
        LocalDate current = start;
        columnIndex++;
        while (current.isBefore(end)) {
            //XSSFCell headerCell = headerRow.createCell(columnIndex);
            //headerCell.setCellStyle(headerCellStyle);
            //headerCell.setCellValue(current.format(DateTimeFormatter.ofPattern("dd MMM YYYY E", new Locale("tr", "TR"))));
            dateColumnIndexMap.put(current, columnIndex);
            current = current.plusDays(1);
            columnIndex++;
        }

        XSSFCellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setWrapText(true);
        addBorderToCell(dataCellStyle, BorderStyle.THICK);

        for (User user : hierarchicalUsers) {
            columnIndex = 1;

            XSSFRow userRow = sheet.createRow(rowIndex++);
            XSSFCell usernameCell = userRow.createCell(columnIndex++);
            usernameCell.setCellStyle(usernameCellStyle);
            usernameCell.setCellValue(user.getFullName());

            for (Integer col : dateColumnIndexMap.values()) {
                userRow.createCell(col).setCellStyle(dataCellStyle);
            }

            List<MotionSums> usersMotionSumss = MotionSumss.stream()
                .filter(x -> x.getOwner() != null && x.getOwner().getId().equals(user.getId()))
                .sorted(Comparator.comparing(MotionSums::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
            for (MotionSums MotionSums : usersMotionSumss) {
                LocalDate MotionSumsDueDate = MotionSums.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDate();
                XSSFCell dataCell = userRow.getCell(dateColumnIndexMap.get(MotionSumsDueDate));

                if (dataCell == null) {
                    dataCell = userRow.createCell(dateColumnIndexMap.get(MotionSumsDueDate));
                    dataCell.setCellStyle(dataCellStyle);
                }

                if (StringUtils.isBlank(dataCell.getStringCellValue())) {
                    dataCell.setCellValue(MotionSums.getDescription());
                } else {
                    dataCell.setCellValue(dataCell.getStringCellValue() + "\n" + MotionSums.getDescription());
                }
            }
        }

        sheet.autoSizeColumn(1);
        for (Integer col : dateColumnIndexMap.values()) {
            sheet.setColumnWidth(col, 6000);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

    public byte[] generateExcelMotionSumsReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
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

        List<MotionSums> MotionSumss = getData(null, request, false).getBody();

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

        XSSFCell headerSupplierCell = headerRow.createCell(columnIndex++);     //MotionSums ID
        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);     //Görev Sahibi
        XSSFCell headerTypeCell = headerRow.createCell(columnIndex++);      //Konu
        XSSFCell headerTypeDescCell = headerRow.createCell(columnIndex++);      //Konu Başlığı
        XSSFCell headerStatusCell = headerRow.createCell(columnIndex++);    //Durum
        XSSFCell headerImportanceCell = headerRow.createCell(columnIndex++);    //Önem Derecesi
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);   //Açıklama
        XSSFCell headerDueTimeCell = headerRow.createCell(columnIndex++);       //Yapılacak Zaman
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);       //Oluşturma Tarihi
        XSSFCell headerCreatorCell = headerRow.createCell(columnIndex++);       //Oluşturan

        headerSupplierCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerTypeCell.setCellStyle(headerCellStyle);
        headerTypeDescCell.setCellStyle(headerCellStyle);
        headerStatusCell.setCellStyle(headerCellStyle);
        headerImportanceCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerDueTimeCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);
        headerCreatorCell.setCellStyle(headerCellStyle);

        headerSupplierCell.setCellValue("Tedarikçi");
        headerOwnerCell.setCellValue("Görev Sahibi");
        headerTypeCell.setCellValue("Konu");
        headerTypeDescCell.setCellValue("Konu Başlığı");
        headerStatusCell.setCellValue("Durum");
        headerImportanceCell.setCellValue("Önem Derecesi");
        headerDescriptionCell.setCellValue("Açıklama");
        headerDueTimeCell.setCellValue("Yapılacak Zaman");
        headerCreatedDateCell.setCellValue("Oluşturma Tarihi");
        headerCreatorCell.setCellValue("Oluşturan");

        for (MotionSums MotionSums : MotionSumss) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell supplierCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell typeCell = row.createCell(columnIndex++);
            XSSFCell typeDescCell = row.createCell(columnIndex++);
            XSSFCell statusCell = row.createCell(columnIndex++);
            XSSFCell importanceCell = row.createCell(columnIndex++);
            XSSFCell descriptionCell = row.createCell(columnIndex++);
            XSSFCell dueTimeCell = row.createCell(columnIndex++);
            XSSFCell createdDateCell = row.createCell(columnIndex++);
            XSSFCell creatorCell = row.createCell(columnIndex++);


            /*if (MotionSums.getCustomer() != null) {
                supplierCell.setCellValue(MotionSums.getCustomer().getCommercialTitle());
            }*/
            if (MotionSums.getOwner() != null) {
                ownerCell.setCellValue(MotionSums.getOwner().getFullName());
            }
            if (MotionSums.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(MotionSums.getCreatedDate()));
            }
            if (MotionSums.getCreatedBy() != null) {
                creatorCell.setCellValue(MotionSums.getCreatedBy().getFullName());
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
