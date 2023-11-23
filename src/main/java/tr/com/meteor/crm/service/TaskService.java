package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.utils.attributevalues.TaskStatus;
import tr.com.meteor.crm.utils.attributevalues.TaskType;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;
import tr.com.meteor.crm.domain.Task;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.TaskRepository;
import tr.com.meteor.crm.repository.ActivityRepository;

import java.io.ByteArrayOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static tr.com.meteor.crm.utils.ExcelUtils.addBorderToCell;

@Service
@Transactional(rollbackFor = Exception.class)
public class TaskService extends GenericIdNameAuditingEntityService<Task, UUID, TaskRepository> {

    private final MailService mailService;
    private final ActivityRepository activityRepository;
    public TaskService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                       BaseConfigurationService baseConfigurationService,
                       TaskRepository repository, MailService mailService, ActivityRepository activityRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Task.class, repository);
        this.mailService = mailService;
        this.activityRepository = activityRepository;
    }

    public List<Task> saveWeekly(List<Task> tasks) throws Exception {
        List<Task> taskList = new ArrayList<>();
        for (Task task : tasks) {
            if (!StringUtils.isBlank(task.getDescription()) && task.getId() != null) {
                taskList.add(update(getCurrentUser(), task));
            } else if (task.getId() == null) {
                task.setTaskType(TaskType.KOLAY_AJANDA.getAttributeValue());
                task.setStatus(TaskStatus.YENI.getAttributeValue());
                taskList.add(add(getCurrentUser(), task));
            } else if (StringUtils.isBlank(task.getDescription())) {
                delete(getCurrentUser(), task.getId());
            }
        }

        return taskList;
    }

    public byte[] generateExcelTaskWizardReportForUser(User currentUser, Instant startDate, Instant endDate) throws Exception {
        startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("taskType.id", FilterItem.Operator.EQUALS, TaskType.KOLAY_AJANDA.getId()),
                Filter.FilterItem("dueTime", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("dueTime", FilterItem.Operator.LESS_THAN, endDate),
                Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
            )
        );

        List<Task> tasks = getData(null, request, false).getBody();

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

            List<Task> usersTasks = tasks.stream()
                .filter(x -> x.getOwner() != null && x.getOwner().getId().equals(user.getId()))
                .sorted(Comparator.comparing(Task::getDueTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
            for (Task task : usersTasks) {
                LocalDate taskDueDate = task.getDueTime().atZone(ZoneId.systemDefault()).toLocalDate();
                XSSFCell dataCell = userRow.getCell(dateColumnIndexMap.get(taskDueDate));

                if (dataCell == null) {
                    dataCell = userRow.createCell(dateColumnIndexMap.get(taskDueDate));
                    dataCell.setCellStyle(dataCellStyle);
                }

                if (StringUtils.isBlank(dataCell.getStringCellValue())) {
                    dataCell.setCellValue(task.getDescription());
                } else {
                    dataCell.setCellValue(dataCell.getStringCellValue() + "\n" + task.getDescription());
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

    public byte[] generateExcelTaskReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.Or(
                    Filter.FilterItem("assigner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<Task> tasks = getData(null, request, false).getBody();

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

        XSSFCell headertaskIdCell = headerRow.createCell(columnIndex++);     //Task ID
        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);     //Talep Eden
        XSSFCell headerAssignerCell = headerRow.createCell(columnIndex++);  //Yetkili
        XSSFCell headerDepartmentCell = headerRow.createCell(columnIndex++);     //Birim
        XSSFCell headerTypeCell = headerRow.createCell(columnIndex++);      //Konu
        XSSFCell headerTypeDescCell = headerRow.createCell(columnIndex++);      //Konu Başlığı
        XSSFCell headerStatusCell = headerRow.createCell(columnIndex++);    //Durum
        XSSFCell headerImportanceCell = headerRow.createCell(columnIndex++);    //Önem Derecesi
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);   //Açıklama
        XSSFCell headerDueTimeCell = headerRow.createCell(columnIndex++);       //Tahmini Tamamlanma Zamanı
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);       //Oluşturma Tarihi
        XSSFCell headerCreatorCell = headerRow.createCell(columnIndex++);       //Oluşturan
        XSSFCell headerUpdateDateCell = headerRow.createCell(columnIndex++);       //Son Düzenleme Tarihi
        XSSFCell headerUpdaterCell = headerRow.createCell(columnIndex++);       //Son Düzenleyen
        XSSFCell headercompleteDateCell = headerRow.createCell(columnIndex++);       //Tamamlanma Tarihi

        headertaskIdCell.setCellStyle(headerCellStyle);
        headerAssignerCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerDepartmentCell.setCellStyle(headerCellStyle);
        headerTypeCell.setCellStyle(headerCellStyle);
        headerTypeDescCell.setCellStyle(headerCellStyle);
        headerStatusCell.setCellStyle(headerCellStyle);
        headerImportanceCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerDueTimeCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);
        headerCreatorCell.setCellStyle(headerCellStyle);
        headerUpdateDateCell.setCellStyle(headerCellStyle);
        headerUpdaterCell.setCellStyle(headerCellStyle);
        headercompleteDateCell.setCellStyle(headerCellStyle);

        headertaskIdCell.setCellValue("Task ID");
        headerOwnerCell.setCellValue("Talep Eden");
        headerAssignerCell.setCellValue("Yetkili");
        headerDepartmentCell.setCellValue("Birim");
        headerTypeCell.setCellValue("Konu");
        headerTypeDescCell.setCellValue("Konu Başlığı");
        headerStatusCell.setCellValue("Durum");
        headerImportanceCell.setCellValue("Önem Derecesi");
        headerDescriptionCell.setCellValue("Açıklama");
        headerDueTimeCell.setCellValue("Tahmini Tamamlanma Zamanı");
        headerCreatedDateCell.setCellValue("Oluşturma Tarihi");
        headerCreatorCell.setCellValue("Oluşturan");
        headerUpdateDateCell.setCellValue("Son Düzenleme Tarihi");
        headerUpdaterCell.setCellValue("Son Düzenleyen");
        headercompleteDateCell.setCellValue("Talep Tamamlanma Tarihi");

        for (Task task : tasks) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell taskIdCell = row.createCell(columnIndex++);
            XSSFCell assignerCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell departmentCell = row.createCell(columnIndex++);
            XSSFCell typeCell = row.createCell(columnIndex++);
            XSSFCell typeDescCell = row.createCell(columnIndex++);
            XSSFCell statusCell = row.createCell(columnIndex++);
            XSSFCell importanceCell = row.createCell(columnIndex++);
            XSSFCell descriptionCell = row.createCell(columnIndex++);
            XSSFCell dueTimeCell = row.createCell(columnIndex++);
            XSSFCell createdDateCell = row.createCell(columnIndex++);
            XSSFCell creatorCell = row.createCell(columnIndex++);
            XSSFCell updateDateCell = row.createCell(columnIndex++);
            XSSFCell updaterCell = row.createCell(columnIndex++);
            XSSFCell complateDateCell = row.createCell(columnIndex++);

            taskIdCell.setCellValue(task.getId().toString());
            if (task.getTaskType() != null) {
                typeCell.setCellValue(task.getTaskType().getLabel());
            }

            if (task.getOwner() != null) {
                ownerCell.setCellValue(task.getOwner().getFullName());
            }

           if (task.getAssigner() != null) {
                assignerCell.setCellValue(task.getAssigner().getFullName());
            }

            if (task.getBirim() != null) {
                departmentCell.setCellValue(task.getBirim().getLabel());
            }

            if (task.getDueTime() != null) {
                dueTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(task.getDueTime()));
            }

            if (task.getComplateDate() != null) {
                complateDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(task.getComplateDate()));
            }

            if (task.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(task.getCreatedDate()));
            }

            if (task.getLastModifiedDate() != null) {
                updateDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(task.getLastModifiedDate()));
            }

            if (task.getCreatedBy() != null) {
                creatorCell.setCellValue(task.getCreatedBy().getFullName());
            }

            if (task.getLastModifiedBy() != null) {
                updaterCell.setCellValue(task.getLastModifiedBy().getFullName());
            }

            if (task.getStatus() != null) {
                statusCell.setCellValue(task.getStatus().getLabel());
            }

            if (task.getImportance() != null) {
                importanceCell.setCellValue(task.getImportance().getLabel());
            }

            if (StringUtils.isNotBlank(task.getDescription())) {
                descriptionCell.setCellValue(task.getDescription());
           }

            if (StringUtils.isNotBlank(task.getSubjectdesc())) {
                typeDescCell.setCellValue(task.getSubjectdesc());
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

    public byte[] generateExcelTaskBTReport(User currentUser) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        /*startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();*/

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("taskType", FilterItem.Operator.EQUALS, TaskType.BT_ISSURECLERI.getId()),
                Filter.Or(
                    Filter.FilterItem("status", FilterItem.Operator.EQUALS, TaskStatus.YENI.getId()),
                    Filter.FilterItem("status", FilterItem.Operator.EQUALS, TaskStatus.DEVAM_EDIYOR.getId())
                ),
                Filter.Or(
                    Filter.FilterItem("assigner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<Task> tasks = getData(null, request, false).getBody();


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

        XSSFCell headerTypeDescCell = headerRow.createCell(columnIndex++);      //Konu Başlığı
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);   //Açıklama
        XSSFCell headerDueTimeCell = headerRow.createCell(columnIndex++);       //Tahmini Tamamlanma Zamanı
        XSSFCell headerProDescCell = headerRow.createCell(columnIndex++);       //Son İşlem

        headerTypeDescCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerDueTimeCell.setCellStyle(headerCellStyle);
        headerProDescCell.setCellStyle(headerCellStyle);

        headerTypeDescCell.setCellValue("Konu");
        headerDescriptionCell.setCellValue("Açıklama");
        headerDueTimeCell.setCellValue("Tahmini Tamamlanma Zamanı");
        headerProDescCell.setCellValue("Son İşlem");

        for (Task task : tasks) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell typeDescCell = row.createCell(columnIndex++);
            XSSFCell descriptionCell = row.createCell(columnIndex++);
            XSSFCell dueTimeCell = row.createCell(columnIndex++);
            XSSFCell proDescCell = row.createCell(columnIndex++);

            List<Activity> activities = activityRepository.findAllByTaskId(task.getId());
            LocalDateTime date = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0);
            Instant lastactivityDate = date.atZone(ZoneId.systemDefault()).toInstant();
            for (Activity activity : activities) {
                if(activity.getCreatedDate().compareTo(lastactivityDate)>0){
                    lastactivityDate = activity.getCreatedDate();
                    proDescCell.setCellValue(activity.getDescription());
                }
            }

            if (task.getDueTime() != null) {
                dueTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(task.getDueTime()));
            }

            if (StringUtils.isNotBlank(task.getDescription())) {
                descriptionCell.setCellValue(task.getDescription());
            }

            if (StringUtils.isNotBlank(task.getSubjectdesc())) {
                typeDescCell.setCellValue(task.getSubjectdesc());
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

    public void notificationMail(String receiver, String subject, String message) throws Exception {
        mailService.sendEmail(receiver,
            subject,message,
            false,false);
    }
}
