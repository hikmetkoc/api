package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Buy;
import tr.com.meteor.crm.domain.Holiday;
import tr.com.meteor.crm.domain.PaymentOrder;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.HolidayRepository;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class HolidayService extends GenericIdNameAuditingEntityService<Holiday, UUID, HolidayRepository> {

    public final MailService mailService;

    public HolidayService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                          BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                          BaseConfigurationService baseConfigurationService,
                          HolidayRepository repository, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Holiday.class, repository);
        this.mailService = mailService;
    }

    public byte[] generateExcelHolidayReport(User currentUser, String year, String month) throws Exception {
        String[] turkishMonths = {
            "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"
        };
        String selectedMonth = month.trim();
        int selectedYear = Integer.parseInt(year);
        int monthIndex = Arrays.asList(turkishMonths).indexOf(selectedMonth) + 1; // Ay indisleri 1'den başlar
        LocalDateTime firstDayOfMonth = LocalDateTime.of(selectedYear, monthIndex, 1, 0, 0);
        LocalDateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusSeconds(1);
        Instant startOfSelectedMonth = firstDayOfMonth.toInstant(ZoneOffset.UTC);
        Instant endOfSelectedMonth = lastDayOfMonth.toInstant(ZoneOffset.UTC);
        List<Holiday> Holidays = repository.findByStartDateBetweenOrEndDateBetween(startOfSelectedMonth, endOfSelectedMonth, startOfSelectedMonth, endOfSelectedMonth);

        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.Or(
                    Filter.FilterItem("assigner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("user.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
        ));
        List<Holiday> holidays = getData(null, request, false).getBody();

        Holidays.removeIf(holiday -> !holidays.contains(holiday));

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

        XSSFCell headerUserCell = headerRow.createCell(columnIndex++);     //Talebi Oluşturan
        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);     //İzni Kullanan Personel
        XSSFCell headerAssignerCell = headerRow.createCell(columnIndex++);  //Yetkili
        XSSFCell headerOwnerSirketCell = headerRow.createCell(columnIndex++);  //Şirket
        XSSFCell headerStatusCell = headerRow.createCell(columnIndex++);    //Durum
        XSSFCell headerTypeCell = headerRow.createCell(columnIndex++);    //İzin Türü
        XSSFCell headerUsedCell = headerRow.createCell(columnIndex++);    //Kullanılan İzin
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);   //Açıklama
        XSSFCell headerAddressCell = headerRow.createCell(columnIndex++);   //İzni Kullanacağı Adres
        XSSFCell headerStartDateCell = headerRow.createCell(columnIndex++);       //Başlangıç Zamanı
        XSSFCell headerEndDateCell = headerRow.createCell(columnIndex++);       //Bitiş Zamanı
        XSSFCell headerComeDateCell = headerRow.createCell(columnIndex++);       //Dönüş Zamanı
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);       //Oluşturma Zamanı

        headerUserCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerAssignerCell.setCellStyle(headerCellStyle);
        headerOwnerSirketCell.setCellStyle(headerCellStyle);
        headerStatusCell.setCellStyle(headerCellStyle);
        headerTypeCell.setCellStyle(headerCellStyle);
        headerUsedCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerAddressCell.setCellStyle(headerCellStyle);
        headerStartDateCell.setCellStyle(headerCellStyle);
        headerEndDateCell.setCellStyle(headerCellStyle);
        headerComeDateCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);

        headerUserCell.setCellValue("İzni Oluşturan");
        headerOwnerCell.setCellValue("Talep Eden");
        headerAssignerCell.setCellValue("Yetkili");
        headerOwnerSirketCell.setCellValue("Şirket");
        headerStatusCell.setCellValue("Durum");
        headerTypeCell.setCellValue("İzin Türü");
        headerUsedCell.setCellValue("Kullanılan İzin Günü");
        headerDescriptionCell.setCellValue("Açıklama");
        headerAddressCell.setCellValue("İzni Kullanacağı Adres");
        headerStartDateCell.setCellValue("Başlangıç Tarihi");
        headerEndDateCell.setCellValue("Bitiş Tarihi");
        headerComeDateCell.setCellValue("Dönüş Tarihi");
        headerCreatedDateCell.setCellValue("Oluşturma Tarihi");

        for (Holiday Holiday : Holidays) {
            columnIndex = 0;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell UserCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell assignerCell = row.createCell(columnIndex++);
            XSSFCell ownerSirketCell = row.createCell(columnIndex++);
            XSSFCell statusCell = row.createCell(columnIndex++);
            XSSFCell typeCell = row.createCell(columnIndex++);
            XSSFCell usedCell = row.createCell(columnIndex++);
            XSSFCell descriptionCell = row.createCell(columnIndex++);
            XSSFCell addressCell = row.createCell(columnIndex++);
            XSSFCell startDateCell = row.createCell(columnIndex++);
            XSSFCell endDateCell = row.createCell(columnIndex++);
            XSSFCell comeDateCell = row.createCell(columnIndex++);
            XSSFCell createdDateCell = row.createCell(columnIndex++);

            if (Holiday.getUser() != null) {
                UserCell.setCellValue(Holiday.getUser().getFullName());
            }
            if (Holiday.getOwner() != null) {
                ownerCell.setCellValue(Holiday.getOwner().getFullName());
            }

           if (Holiday.getAssigner() != null) {
                assignerCell.setCellValue(Holiday.getAssigner().getFullName());
            }

            if (Holiday.getOwner().getSgksirket() != null) {
                ownerSirketCell.setCellValue(Holiday.getOwner().getSgksirket().getLabel());
            }
            if (Holiday.getApprovalStatus() != null) {
                statusCell.setCellValue(Holiday.getApprovalStatus().getLabel());
            }
            if (Holiday.getType() != null) {
                typeCell.setCellValue(Holiday.getType().getLabel());
            }
            usedCell.setCellValue(String.valueOf(Holiday.getIzingun()));

            if (StringUtils.isNotBlank(Holiday.getDescription())) {
                descriptionCell.setCellValue(Holiday.getDescription());
            }
            if (Holiday.getCity() != null) {
                addressCell.setCellValue(Holiday.getCity().getName());
            }

            if (Holiday.getStartDate() != null) {
                startDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Holiday.getStartDate()));
            }

            if (Holiday.getEndDate() != null) {
                endDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Holiday.getEndDate()));
            }

            if (Holiday.getComeDate() != null) {
                comeDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Holiday.getComeDate()));
            }
            if (Holiday.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Holiday.getCreatedDate()));
            }
        }

        for (int i = 0; i < 25; i++) {
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

    public Holiday uploadHoliday(UUID id, String base64file) throws Exception {
        System.out.println(id.toString() + " id li izne dosya eklendi.");
        try {
            repository.updateBase64(id,base64file);
        } catch (Exception e) {
            System.out.println("HATA MESAJI: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("HATA!");
        }
        // Eğer id ile eşleşen bir PaymentOrder bulunamazsa burada null döndürmelisiniz.
        return null;
    }

    public String getEttntById(UUID id) throws Exception {
        List<Holiday> holidays = repository.findAll();
        String veri = "";
        for (Holiday holiday: holidays) {
            if (holiday.getId().equals(id)) {
                veri = holiday.getBase64File();
                break;
            }
        }
        return veri;
    }
}
