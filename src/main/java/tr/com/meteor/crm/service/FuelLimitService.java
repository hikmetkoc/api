package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tr.com.meteor.crm.domain.FuelLimit;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.ContProductRepository;
import tr.com.meteor.crm.repository.FuelLimitRepository;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class FuelLimitService extends GenericIdNameAuditingEntityService<FuelLimit, UUID, FuelLimitRepository> {

    private final String apiUrl = "https://srv.meteorpetrol.com/DisServis/limitguncelle";
    private final RestTemplate restTemplate;

    public FuelLimitService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                            BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                            BaseConfigurationService baseConfigurationService,
                            FuelLimitRepository repository, RestTemplate restTemplate) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            FuelLimit.class, repository);
        this.restTemplate = restTemplate;
    }

    public List<FuelLimit> saveWeekly(List<FuelLimit> FuelLimits) throws Exception {
        List<FuelLimit> FuelLimitList = new ArrayList<>();
        for (FuelLimit FuelLimit : FuelLimits) {
            if (!StringUtils.isBlank(FuelLimit.getDescription()) && FuelLimit.getId() != null) {
                FuelLimitList.add(update(getCurrentUser(), FuelLimit));
            } else if (FuelLimit.getId() == null) {
                /*Buy.setType(BuyType.KOLAY_AJANDA.getAttributeValue());
                Buy.setStatus(BuyStatus.YENI.getAttributeValue());*/
                FuelLimitList.add(add(getCurrentUser(), FuelLimit));
            } else if (StringUtils.isBlank(FuelLimit.getDescription())) {
                delete(getCurrentUser(), FuelLimit.getId());
            }
        }

        return FuelLimitList;
    }
    public byte[] generateExcelFuelLimitReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
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
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("secondAssigner.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<FuelLimit> FuelLimits = getData(null, request, false).getBody();

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

        XSSFCell headerCodeCell = headerRow.createCell(columnIndex++);     //Satın Alma Kodu
        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);     //Satın Alma Sorumlusu
        XSSFCell headerAssignerCell = headerRow.createCell(columnIndex++);  //1.Onaycı
        XSSFCell headerSecondAssignerCell = headerRow.createCell(columnIndex++);  //2.Onaycı
        XSSFCell headerFuelLimitOkeyCell = headerRow.createCell(columnIndex++);   //Teklif Onay Durumu
        /*XSSFCell headerCompanyCell = headerRow.createCell(columnIndex++);     //Şirket
        XSSFCell headerDepartmentCell = headerRow.createCell(columnIndex++);     //Birim*/
        XSSFCell headerSupplierCell = headerRow.createCell(columnIndex++);     //Tedarikçi
        XSSFCell headerStartCell = headerRow.createCell(columnIndex++);     //Teklif Başlangıç
        XSSFCell headerEndCell = headerRow.createCell(columnIndex++);     //Teklif Bitiş
        XSSFCell headerFirstCell = headerRow.createCell(columnIndex++);     //1.Onay Tarihi
        XSSFCell headerSecondCell = headerRow.createCell(columnIndex++);     //2.Onay Tarihi
        XSSFCell headerMoneyCell = headerRow.createCell(columnIndex++);      //Tutar
        XSSFCell headerMoneyTypeCell = headerRow.createCell(columnIndex++);      //Para Birimi
        XSSFCell headerFuelLimitTypeCell = headerRow.createCell(columnIndex++);    //Ödeme Yöntemi
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);    //Teklif Gerekçesi
        XSSFCell headerOkeyCell = headerRow.createCell(columnIndex++);   //Onay Durumu
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);       //Oluşturulma Tarihi

        headerCodeCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerAssignerCell.setCellStyle(headerCellStyle);
        headerSecondAssignerCell.setCellStyle(headerCellStyle);
        headerFuelLimitOkeyCell.setCellStyle(headerCellStyle);
        /*headerCompanyCell.setCellStyle(headerCellStyle);
        headerDepartmentCell.setCellStyle(headerCellStyle);*/
        headerSupplierCell.setCellStyle(headerCellStyle);
        headerStartCell.setCellStyle(headerCellStyle);
        headerEndCell.setCellStyle(headerCellStyle);
        headerFirstCell.setCellStyle(headerCellStyle);
        headerSecondCell.setCellStyle(headerCellStyle);
        headerMoneyCell.setCellStyle(headerCellStyle);
        headerMoneyTypeCell.setCellStyle(headerCellStyle);
        headerFuelLimitTypeCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerOkeyCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);

        headerCodeCell.setCellValue("Satın Alma Kodu");
        headerOwnerCell.setCellValue("Satın Alma Sorumlusu");
        headerAssignerCell.setCellValue("1.Onaycı");
        headerSecondAssignerCell.setCellValue("2.Onaycı");
        headerFuelLimitOkeyCell.setCellValue("Teklif Onay Durumu");
        /*headerCompanyCell.setCellValue("Şirket");
        headerDepartmentCell.setCellValue("Birim");*/
        headerSupplierCell.setCellValue("Tedarikçi");
        headerStartCell.setCellValue("Teklif Başlangıç Tarihi");
        headerEndCell.setCellValue("Teklif Bitiş Tarihi");
        headerFirstCell.setCellValue("1.Onay Tarihi");
        headerSecondCell.setCellValue("2.Onay Tarihi");
        headerMoneyCell.setCellValue("Tutar");
        headerMoneyTypeCell.setCellValue("Para Birimi");
        headerFuelLimitTypeCell.setCellValue("Ödeme Yöntemi");
        headerDescriptionCell.setCellValue("Teklif Gerekçesi");
        headerOkeyCell.setCellValue("Onay Durumu");
        headerCreatedDateCell.setCellValue("Oluşturulma Tarihi");

        for (FuelLimit FuelLimit : FuelLimits) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell codeCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell assignerCell = row.createCell(columnIndex++);
            XSSFCell secondAssignerCell = row.createCell(columnIndex++);
            XSSFCell fuellimitOkeyCell = row.createCell(columnIndex++);
            /*XSSFCell companyCell = row.createCell(columnIndex++);
            XSSFCell departmentCell = row.createCell(columnIndex++);*/
            XSSFCell supplierCell = row.createCell(columnIndex++);
            XSSFCell startCell = row.createCell(columnIndex++);
            XSSFCell endCell = row.createCell(columnIndex++);
            XSSFCell firstCell = row.createCell(columnIndex++);
            XSSFCell secondCell = row.createCell(columnIndex++);
            XSSFCell moneyCell = row.createCell(columnIndex++);
            XSSFCell moneyTypeCell = row.createCell(columnIndex++);
            XSSFCell fuellimitTypeCell = row.createCell(columnIndex++);
            XSSFCell descriptionCell = row.createCell(columnIndex++);
            XSSFCell okeyCell = row.createCell(columnIndex++);
            XSSFCell createdDateCell = row.createCell(columnIndex++);

            if (FuelLimit.getOwner() != null) {
                ownerCell.setCellValue(FuelLimit.getOwner().getFullName());
            }
            if (FuelLimit.getAssigner() != null) {
                assignerCell.setCellValue(FuelLimit.getAssigner().getFullName());
            }

            if (FuelLimit.getStartDate() != null) {
                startCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(FuelLimit.getStartDate()));
            }
            if (FuelLimit.getEndDate() != null) {
                endCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(FuelLimit.getEndDate()));
            }
            if (FuelLimit.getOkeyFirst() != null) {
                firstCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(FuelLimit.getOkeyFirst()));
            }

            if (FuelLimit.getFuelTl() != null) {
                moneyCell.setCellValue(FuelLimit.getFuelTl().floatValue());
            }

            if (StringUtils.isNotBlank(FuelLimit.getDescription())) {
                descriptionCell.setCellValue(FuelLimit.getDescription());
            }
            if (FuelLimit.getStatus() != null) {
                okeyCell.setCellValue(FuelLimit.getStatus().getLabel());
            }
            if (FuelLimit.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(FuelLimit.getCreatedDate()));
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

    /*public ResponseEntity<String> makeApiRequest(String curcode, Integer fuelTl, String description, String startDate, String endDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String ServisSifre = "14ADa23.";
        int FirmaKodu = 875257;
        int KullaniciId = 47468;
        String CariKodu = curcode;
        int EkLimitTutar = fuelTl;
        String EkLimitAciklama = description;
        String BaslangicTarihi = startDate;
        String BitisTarihi = endDate;

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("ServisSifre", ServisSifre);
        jsonRequest.put("FirmaKodu", FirmaKodu);
        jsonRequest.put("KullaniciId", KullaniciId);
        jsonRequest.put("CariKodu", CariKodu);
        jsonRequest.put("EkLimitTutar", EkLimitTutar);
        jsonRequest.put("EkLimitAciklama", EkLimitAciklama);
        jsonRequest.put("BaslangicTarihi", BaslangicTarihi.toString());
        jsonRequest.put("BitisTarihi", BitisTarihi.toString());

        String jsonRequestString = jsonRequest.toString();
        System.out.println(jsonRequestString);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestString, headers);

        return restTemplate.exchange(
            apiUrl,
            HttpMethod.POST,
            requestEntity,
            String.class
        );
    }*/
    /*LocalDateTime baslangic = LocalDateTime.ofInstant(startDate, ZoneId.systemDefault());
        LocalDateTime bitis = LocalDateTime.ofInstant(endDate, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String BaslangicTarihi = baslangic.format(formatter);
        String BitisTarihi = bitis.format(formatter);*/

    // Verileri bir JSON nesnesine dönüştürün
        /*String jsonRequest = "{" +
            "\"ServisSifre\": \"" + ServisSifre + "\"," +
            "\"FirmaKodu\": " + FirmaKodu + "," +
            "\"KullaniciId\": " + KullaniciId + "," +
            "\"CariKodu\": \"" + CariKodu + "\"," +
            "\"EkLimitTutar\": " + EkLimitTutar + "," +
            "\"EkLimitAciklama\": \"" + EkLimitAciklama + "\"," +
            "\"BaslangicTarihi\": \"" + BaslangicTarihi + "\"," +
            "\"BitisTarihi\": \"" + BitisTarihi + "\"" +
            "}";*/
}
