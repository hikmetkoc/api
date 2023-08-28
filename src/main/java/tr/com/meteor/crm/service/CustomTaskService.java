package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.CustomTask;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.CustomTaskRepository;
import tr.com.meteor.crm.utils.attributevalues.CustomTaskStatus;
import tr.com.meteor.crm.utils.attributevalues.CustomTaskType;
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
public class CustomTaskService extends GenericIdNameAuditingEntityService<CustomTask, UUID, CustomTaskRepository> {

    private final ActivityRepository activityRepository;
    public CustomTaskService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                             BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                             BaseConfigurationService baseConfigurationService,
                             CustomTaskRepository customrepository, ActivityRepository activityRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            CustomTask.class, customrepository);
        this.activityRepository = activityRepository;
    }

    public List<CustomTask> saveWeekly(List<CustomTask> CustomTasks) throws Exception {
        List<CustomTask> CustomTaskList = new ArrayList<>();
        for (CustomTask CustomTask : CustomTasks) {
            if (!StringUtils.isBlank(CustomTask.getDescription()) && CustomTask.getId() != null) {
                CustomTaskList.add(update(getCurrentUser(), CustomTask));
            } else if (CustomTask.getId() == null) {
                CustomTask.setType(CustomTaskType.KOLAY_AJANDA.getAttributeValue());
                CustomTask.setStatus(CustomTaskStatus.YENI.getAttributeValue());
                CustomTaskList.add(add(getCurrentUser(), CustomTask));
            } else if (StringUtils.isBlank(CustomTask.getDescription())) {
                delete(getCurrentUser(), CustomTask.getId());
            }
        }

        return CustomTaskList;
    }

    public byte[] generateExcelCustomTaskWizardReportForUser(User currentUser, Instant startDate, Instant endDate) throws Exception {
        startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("type.id", FilterItem.Operator.EQUALS, CustomTaskType.KOLAY_AJANDA.getId()),
                Filter.FilterItem("dueTime", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("dueTime", FilterItem.Operator.LESS_THAN, endDate),
                Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
            )
        );

        List<CustomTask> CustomTasks = getData(null, request, false).getBody();

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

            List<CustomTask> usersCustomTasks = CustomTasks.stream()
                .filter(x -> x.getOwner() != null && x.getOwner().getId().equals(user.getId()))
                .sorted(Comparator.comparing(CustomTask::getDueTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
            for (CustomTask CustomTask : usersCustomTasks) {
                LocalDate CustomTaskDueDate = CustomTask.getDueTime().atZone(ZoneId.systemDefault()).toLocalDate();
                XSSFCell dataCell = userRow.getCell(dateColumnIndexMap.get(CustomTaskDueDate));

                if (dataCell == null) {
                    dataCell = userRow.createCell(dateColumnIndexMap.get(CustomTaskDueDate));
                    dataCell.setCellStyle(dataCellStyle);
                }

                if (StringUtils.isBlank(dataCell.getStringCellValue())) {
                    dataCell.setCellValue(CustomTask.getDescription());
                } else {
                    dataCell.setCellValue(dataCell.getStringCellValue() + "\n" + CustomTask.getDescription());
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

    public byte[] generateExcelCustomTaskReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
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

        List<CustomTask> CustomTasks = getData(null, request, false).getBody();

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

        XSSFCell headerSupplierCell = headerRow.createCell(columnIndex++);     //CustomTask ID
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

        for (CustomTask CustomTask : CustomTasks) {
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


            if (CustomTask.getCustomer() != null) {
                supplierCell.setCellValue(CustomTask.getCustomer().getCommercialTitle());
            }
            if (CustomTask.getOwner() != null) {
                ownerCell.setCellValue(CustomTask.getOwner().getFullName());
            }
            if (CustomTask.getType() != null) {
                typeCell.setCellValue(CustomTask.getType().getLabel());
            }
            typeDescCell.setCellValue(CustomTask.getSubjectdesc());
            if (CustomTask.getStatus() != null) {
                statusCell.setCellValue(CustomTask.getStatus().getLabel());
            }
            if (CustomTask.getImportance() != null) {
                importanceCell.setCellValue(CustomTask.getImportance().getLabel());
            }
            descriptionCell.setCellValue(CustomTask.getDescription());
            if (CustomTask.getDueTime() != null) {
                dueTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(CustomTask.getDueTime()));
            }
            if (CustomTask.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(CustomTask.getCreatedDate()));
            }
            if (CustomTask.getCreatedBy() != null) {
                creatorCell.setCellValue(CustomTask.getCreatedBy().getFullName());
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
