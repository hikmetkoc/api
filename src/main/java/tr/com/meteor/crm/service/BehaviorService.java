package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.*;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.utils.attributevalues.TaskStatus;
import tr.com.meteor.crm.utils.attributevalues.TaskType;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.jasper.rest.errors.BadRequestAlertException;
import tr.com.meteor.crm.utils.jasper.rest.errors.RecordNotFoundException;
import tr.com.meteor.crm.utils.operations.Operations;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class BehaviorService extends GenericIdNameAuditingEntityService<Behavior, UUID, BehaviorRepository> {

    private final MotionSumsRepository motionSumsRepository;
    private final ContractRepository contractRepository;
    private final MailService mailService;

    public BehaviorService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService, BehaviorRepository repository,
                           MotionSumsRepository motionSumsRepository, ContractRepository contractRepository, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Behavior.class, repository);
        this.motionSumsRepository = motionSumsRepository;
        this.contractRepository = contractRepository;
        this.mailService = mailService;
    }

    public byte[] generateExcelBehaviorReportForUser(User currentUser) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
            )
        );

        List<Behavior> behaviors = getData(null, request, false).getBody();


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
        XSSFCell headerTypeCell = headerRow.createCell(columnIndex++);
        XSSFCell headerSubjectCell = headerRow.createCell(columnIndex++);
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);
        XSSFCell headerDocumentCell = headerRow.createCell(columnIndex++);
        XSSFCell headerInputDateCell = headerRow.createCell(columnIndex++);
        XSSFCell headerTotalCell = headerRow.createCell(columnIndex++);
        XSSFCell headerMotionCell = headerRow.createCell(columnIndex++);
        XSSFCell headerCustomerCell = headerRow.createCell(columnIndex++);
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);

        headerOwnerCell.setCellStyle(headerCellStyle);
        headerTypeCell.setCellStyle(headerCellStyle);
        headerSubjectCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerDocumentCell.setCellStyle(headerCellStyle);
        headerInputDateCell.setCellStyle(headerCellStyle);
        headerTotalCell.setCellStyle(headerCellStyle);
        headerMotionCell.setCellStyle(headerCellStyle);
        headerCustomerCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);

        headerOwnerCell.setCellValue("Oluşturan");
        headerTypeCell.setCellValue("Hareket Tipi");
        headerSubjectCell.setCellValue("Konu");
        headerDescriptionCell.setCellValue("Açıklama");
        headerDocumentCell.setCellValue("Belge Nu");
        headerInputDateCell.setCellValue("Giriş Tarihi");
        headerTotalCell.setCellValue("Tutar");
        headerMotionCell.setCellValue("Maliyet Yeri");
        headerCustomerCell.setCellValue("Tedarikçi");
        headerCreatedDateCell.setCellValue("Oluşturulma Tarihi");

        for (Behavior behavior : behaviors) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell typeCell = row.createCell(columnIndex++);
            XSSFCell subjectCell = row.createCell(columnIndex++);
            XSSFCell descriptionCell = row.createCell(columnIndex++);
            XSSFCell documentCell = row.createCell(columnIndex++);
            XSSFCell inputDateCell = row.createCell(columnIndex++);
            XSSFCell totalCell = row.createCell(columnIndex++);
            XSSFCell motionCell = row.createCell(columnIndex++);
            XSSFCell customerCell = row.createCell(columnIndex++);
            XSSFCell createdDateCell = row.createCell(columnIndex++);

            if (behavior.getOwner() != null) {
                ownerCell.setCellValue(behavior.getOwner().getFullName());
            }
            if (behavior.getType() != null) {
                typeCell.setCellValue(behavior.getType().getLabel());
            }
            if (behavior.getSubject() != null) {
                subjectCell.setCellValue(behavior.getSubject());
            }
            if (behavior.getDescription() != null) {
                descriptionCell.setCellValue(behavior.getDescription());
            }
            if (behavior.getDocument() != null) {
                documentCell.setCellValue(behavior.getDocument());
            }

            if (behavior.getInputDate() != null) {
                inputDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(behavior.getInputDate()));
            }

            if (behavior.getFuelTl() != null) {
                totalCell.setCellValue(behavior.getFuelTl().floatValue());
            }

            if (behavior.getMotionsums() != null) {
                motionCell.setCellValue(behavior.getMotionsums().getCost().getLabel());
            }

            if (behavior.getMotionsums().getCustomer() != null) {
                customerCell.setCellValue(behavior.getMotionsums().getCustomer().getName());
            }

            if (behavior.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(behavior.getCreatedDate()));
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

    public byte[] generateSelectedExcelReport(List<UUID> ids, User currentUser) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build()
            .page(0)
            .size(Integer.MAX_VALUE)
            .filter(
                Filter.And(
                    Filter.FilterItem("id", FilterItem.Operator.IN, ids),
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            );
        List<Behavior> behaviors = getData(null, request, false).getBody();


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        sheet.setColumnWidth(0, 200);
        sheet.createRow(0).setHeight((short) 200);

        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);

        int rowIndex = 0;
        int columnIndex = 0;

        XSSFRow headerRow = sheet.createRow(rowIndex++);

        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(boldFont);
        headerCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 189, (byte) 215, (byte) 238}, null));
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCell headerParentCell = headerRow.createCell(columnIndex++);
        XSSFCell headerCostCell = headerRow.createCell(columnIndex++);
        XSSFCell headerCustomerCell = headerRow.createCell(columnIndex++);
        XSSFCell headerLoanCell = headerRow.createCell(columnIndex++);
        XSSFCell headerReceiveCell = headerRow.createCell(columnIndex++);
        XSSFCell headerBalanceCell = headerRow.createCell(columnIndex++);

        headerParentCell.setCellStyle(headerCellStyle);
        headerCostCell.setCellStyle(headerCellStyle);
        headerCustomerCell.setCellStyle(headerCellStyle);
        headerLoanCell.setCellStyle(headerCellStyle);
        headerReceiveCell.setCellStyle(headerCellStyle);
        headerBalanceCell.setCellStyle(headerCellStyle);

        headerParentCell.setCellValue("Üst Cari");
        headerCostCell.setCellValue("Maliyet Yeri");
        headerCustomerCell.setCellValue("Tedarikçi");
        headerLoanCell.setCellValue("Borç");
        headerReceiveCell.setCellValue("Alacak");
        headerBalanceCell.setCellValue("Bakiye");

        Set<MotionSums> motionSumsSet = new HashSet<>();
        for (Behavior behavior : behaviors) {
            motionSumsSet.add(behavior.getMotionsums());
        }

        List<MotionSums> motionSumsList = new ArrayList<>(motionSumsSet);

        for (MotionSums motionSums1: motionSumsList) {
            columnIndex = 0;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell parentCell = row.createCell(columnIndex++);
            XSSFCell costCell = row.createCell(columnIndex++);
            XSSFCell customerCell = row.createCell(columnIndex++);
            XSSFCell loanCell = row.createCell(columnIndex++);
            XSSFCell receiveCell = row.createCell(columnIndex++);
            XSSFCell balanceCell = row.createCell(columnIndex++);

            if (motionSums1.getParent() != null) {
                parentCell.setCellValue(motionSums1.getParent().getName());
            }
            if (motionSums1.getCost() != null) {
                costCell.setCellValue(motionSums1.getCost().getLabel());
            }
            if (motionSums1.getCustomer() != null) {
                customerCell.setCellValue(motionSums1.getCustomer().getName());
            }
            if (motionSums1.getLoan() != null) {
                loanCell.setCellValue(motionSums1.getLoan().floatValue());
            }
            if (motionSums1.getReceive() != null) {
                receiveCell.setCellValue(motionSums1.getReceive().floatValue());
            }
            if (motionSums1.getBalance() != null) {
                balanceCell.setCellValue(motionSums1.getBalance().floatValue());
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
