package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.utils.ExcelUtils;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.operations.Operations;
import tr.com.meteor.crm.utils.request.Request;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.domain.Target;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.TargetRepository;

import java.io.ByteArrayOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static tr.com.meteor.crm.utils.ExcelUtils.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class TargetService extends GenericIdNameAuditingEntityService<Target, UUID, TargetRepository> {

    private final MailService mailService;

    public TargetService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                         BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                         BaseConfigurationService baseConfigurationService,
                         TargetRepository repository, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Target.class, repository);
        this.mailService = mailService;
    }

    public List<Target> getYearlyTargets(int year) throws Exception {
        Instant firstDay = Year.of(year).atDay(1).atStartOfDay()
            .atZone(ZoneId.systemDefault()).toInstant();
        Instant lastDay = Year.of(year + 1).atDay(1).atStartOfDay()
            .atZone(ZoneId.systemDefault()).toInstant();

        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsers(getCurrentUser());
        Request request = Request.build()
            .filter(Filter.And(
                Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList())),
                Filter.FilterItem("termStart", FilterItem.Operator.GREATER_OR_EQUAL_THAN, firstDay),
                Filter.FilterItem("termStart", FilterItem.Operator.LESS_THAN, lastDay)
            ))
            .page(0).size(hierarchicalUsers.size() * 12);

        List<Target> list = getData(getCurrentUser(), request, false).getBody();

        List<String> checkList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneId.systemDefault());

        for (Target target : list) {
            checkList.add(target.getOwner().getId() + "-" + formatter.format(target.getTermStart()));
        }

        List<Target> targetListToInsert = new ArrayList<>();

        List<YearMonth> yearMonths = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            yearMonths.add(YearMonth.of(year, i));
        }

        for (User user : hierarchicalUsers) {
            for (YearMonth yearMonth : yearMonths) {
                if (!checkList.contains(user.getId() + "-" + yearMonth.toString())) {
                    Target target = new Target();
                    target.setOwner(user);
                    target.setAmount(0d);
                    target.setRealizedAmount(0d);
                    target.setTermStart(yearMonth.atDay(1).atStartOfDay()
                        .atZone(ZoneId.systemDefault()).toInstant());

                    targetListToInsert.add(target);
                }
            }
        }

        repository.saveAll(targetListToInsert);

        return getData(getCurrentUser(), request.size(hierarchicalUsers.size() * 12), false).getBody();
    }

    public ResponseEntity<List<Target>> saveYearlyTargets(List<Target> targets) throws Exception {
        for (Target target : targets) {
            update(getCurrentUser(), target);
        }

        return getData(getCurrentUser(), Request.build().filter(
            Filter.FilterItem("id", FilterItem.Operator.IN, targets.stream().map(Target::getId).collect(Collectors.toList())))
        );
    }

    public ResponseEntity report(User currentUser, int year) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + entityClass.getSimpleName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(generateExcelTargetReportForUser(currentUser, Year.of(year))));
    }

    @Scheduled(cron = "0 0 1 * * 5")
    public void sendFridayTargetReport() throws Exception {
        sendTargetReports();
    }

    @Scheduled(cron = "0 0 1 1 * *")
    public void sendMonthlyTargetReport() throws Exception {
        sendTargetReports();
    }

    public void sendTargetReports() throws Exception {
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

            ByteArrayResource excelFile = new ByteArrayResource(generateExcelTargetReportForUser(user, Year.now()));

            String dateString = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(Instant.now());
            String fileName = "Hedef Raporu " + dateString + ".xlsx";

            mailService.sendWithAttachment(Collections.singleton(user.getEmail()), null, null,
                "Hedef Raporu " + dateString, dateString + " tarihine ait hedef raporu ektedir.",
                true, false, fileName, excelFile);
        }
    }

    private byte[] generateExcelTargetReportForUser(User currentUser, Year year) throws Exception {
        Instant start = year.atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant end = year.plusYears(1).atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        List<User> users = baseUserService.getHierarchicalUsers(currentUser);

        List<Target> targets = repository.findAllByTermStartGreaterThanEqualAndTermStartLessThanAndOwnerIdIn(start, end, users.stream().map(User::getId).collect(Collectors.toList()));

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        XSSFDataFormat dataFormat = workbook.createDataFormat();
        XSSFFormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);

        XSSFCellStyle userNameStyle = workbook.createCellStyle();
        userNameStyle.setWrapText(true);
        userNameStyle.setFont(boldFont);
        userNameStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 217, (byte) 225, (byte) 242}, null));
        userNameStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        userNameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        ExcelUtils.addBorderToCell(userNameStyle, BorderStyle.THIN);

        XSSFCellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setDataFormat(dataFormat.getFormat("0.00"));

        XSSFCellStyle monthCellStyle = workbook.createCellStyle();
        monthCellStyle.setFont(boldFont);
        monthCellStyle.setAlignment(HorizontalAlignment.CENTER);
        monthCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 255, (byte) 217, (byte) 102}, null));
        monthCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        ExcelUtils.addBorderToCell(monthCellStyle, BorderStyle.THIN);

        XSSFCellStyle criterionTitleCellStyle = workbook.createCellStyle();
        criterionTitleCellStyle.setFont(boldFont);
        criterionTitleCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 255, (byte) 217, (byte) 102}, null));
        criterionTitleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        ExcelUtils.addBorderToCell(criterionTitleCellStyle, BorderStyle.THIN);

        XSSFCellStyle amountCellStyle = workbook.createCellStyle();
        amountCellStyle.setDataFormat(dataFormat.getFormat("0.00"));
        ExcelUtils.addBorderToCell(amountCellStyle, BorderStyle.THIN);

        XSSFCellStyle realizedAmountCellStyle = workbook.createCellStyle();
        realizedAmountCellStyle.setDataFormat(dataFormat.getFormat("0.00"));
        ExcelUtils.addBorderToCell(realizedAmountCellStyle, BorderStyle.THIN);

        XSSFCellStyle differenceCellStyle = workbook.createCellStyle();
        differenceCellStyle.setDataFormat(dataFormat.getFormat("0.00"));
        ExcelUtils.addBorderToCell(differenceCellStyle, BorderStyle.THIN);

        XSSFCellStyle percentageCellStyle = workbook.createCellStyle();
        percentageCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 198, (byte) 224, (byte) 180}, null));
        percentageCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        percentageCellStyle.setDataFormat(dataFormat.getFormat("0.00"));
        ExcelUtils.addBorderToCell(percentageCellStyle, BorderStyle.THIN);

        int rowIndex = 1;
        int columnIndex = 3;

        XSSFRow row = sheet.createRow(rowIndex++);
        for (Month month : Month.values()) {
            XSSFCell monthCell = row.createCell(columnIndex++);
            monthCell.setCellStyle(monthCellStyle);
            monthCell.setCellValue(month.getDisplayName(TextStyle.FULL, new Locale("tr", "TR")));
        }

        Map<String, Target> targetMap = new HashMap<>();
        for (Target target : targets) {
            if (target.getOwner() == null || target.getTermStart() == null) continue;
            String key = target.getOwner().getId() + "-" + YearMonth.from(target.getTermStart().atZone(ZoneId.systemDefault()));
            targetMap.put(key, target);
        }

        sheet.setColumnWidth(0, 200);
        sheet.createRow(0).setHeight((short) 200);

        for (User user : users) {
            if (targetMap.keySet().stream().noneMatch(x -> x.startsWith(user.getId().toString()))) continue;

            columnIndex = 1;
            sheet.setColumnWidth(columnIndex, 3500);

            CellRangeAddress userCellRange = new CellRangeAddress(rowIndex, rowIndex + 3, columnIndex, columnIndex);
            sheet.addMergedRegion(userCellRange);

            XSSFRow row1 = sheet.createRow(rowIndex);
            XSSFRow row2 = sheet.createRow(rowIndex + 1);
            XSSFRow row3 = sheet.createRow(rowIndex + 2);
            XSSFRow row4 = sheet.createRow(rowIndex + 3);

            XSSFCell userCell1 = row1.createCell(columnIndex);
            userCell1.setCellStyle(userNameStyle);
            userCell1.setCellValue(user.getFirstName().toUpperCase(new Locale("tr", "TR"))
                + " " + user.getLastName().toUpperCase(new Locale("tr", "TR")));

            XSSFCell userCell2 = row2.createCell(columnIndex);
            userCell2.setCellStyle(userNameStyle);

            XSSFCell userCell3 = row3.createCell(columnIndex);
            userCell3.setCellStyle(userNameStyle);

            XSSFCell userCell4 = row4.createCell(columnIndex);
            userCell4.setCellStyle(userNameStyle);

            columnIndex++;
            sheet.setColumnWidth(columnIndex, 2000);

            XSSFCell amountTitleCell = row1.createCell(columnIndex);
            amountTitleCell.setCellStyle(criterionTitleCellStyle);
            amountTitleCell.setCellValue("HEDEF");

            XSSFCell realizedAmountTitleCell = row2.createCell(columnIndex);
            realizedAmountTitleCell.setCellStyle(criterionTitleCellStyle);
            realizedAmountTitleCell.setCellValue("REAL");

            XSSFCell diffTitleCell = row3.createCell(columnIndex);
            diffTitleCell.setCellStyle(criterionTitleCellStyle);
            diffTitleCell.setCellValue("FARK");

            XSSFCell percentageTitleCell = row4.createCell(columnIndex);
            percentageTitleCell.setCellStyle(criterionTitleCellStyle);
            percentageTitleCell.setCellValue("YÜZDE");

            for (Month month : Month.values()) {
                columnIndex++;
                sheet.setColumnWidth(columnIndex, 3000);

                XSSFCell amountCell = row1.createCell(columnIndex);
                amountCell.setCellStyle(amountCellStyle);

                XSSFCell realizedAmount = row2.createCell(columnIndex);
                realizedAmount.setCellStyle(realizedAmountCellStyle);

                XSSFCell differenceCell = row3.createCell(columnIndex);
                differenceCell.setCellStyle(differenceCellStyle);

                XSSFCell percentageCell = row4.createCell(columnIndex);
                percentageCell.setCellStyle(percentageCellStyle);

                String key = user.getId() + "-" + Year.now().atMonth(month);
                Target target = targetMap.get(key);
                if (target != null) {
                    amountCell.setCellValue(target.getAmount());
                    realizedAmount.setCellFormula(round(target.getRealizedAmount().toString(), 2));
                    differenceCell.setCellFormula(ExcelUtils.round(ExcelUtils.diff(realizedAmount, amountCell), 2));
                    percentageCell.setCellFormula(ExcelUtils.round(ExcelUtils.divide(realizedAmount, amountCell), 2));
                }

                XSSFConditionalFormattingRule redFFontRule = sheet.getSheetConditionalFormatting()
                    .createConditionalFormattingRule(percentageCell.getAddress().formatAsString() + "<100");
                XSSFFontFormatting redFontFormatting = redFFontRule.createFontFormatting();
                redFontFormatting.setFontColor(new XSSFColor(new byte[]{(byte) 255, (byte) 0, (byte) 0}, null));
                redFontFormatting.setFontStyle(false, true);
                ConditionalFormattingRule[] cfRules = new ConditionalFormattingRule[]{redFFontRule};
                CellRangeAddress[] regions = new CellRangeAddress[]{CellRangeAddress.valueOf(percentageCell.getAddress().formatAsString())};
                sheet.getSheetConditionalFormatting().addConditionalFormatting(regions, cfRules);
            }

            rowIndex += 4;
        }

        formulaEvaluator.evaluateAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
}
