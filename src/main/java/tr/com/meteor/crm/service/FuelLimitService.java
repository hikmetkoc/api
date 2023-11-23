package tr.com.meteor.crm.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.domain.FuelLimit;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.FuelLimitRepository;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import javax.xml.ws.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class FuelLimitService extends GenericIdNameAuditingEntityService<FuelLimit, UUID, FuelLimitRepository> {

    //private final String apiUrl = "https://srv.nccpetrol.com/DisServis/riskdetayget?ServisSifre=14ADa23.&CariKodu={cariKodu}&FirmaKodu=875257";
    private final RestTemplate restTemplate;

    private final MailService mailService;

    public FuelLimitService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                            BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                            BaseConfigurationService baseConfigurationService,
                            FuelLimitRepository repository, RestTemplate restTemplate, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            FuelLimit.class, repository);
        this.restTemplate = restTemplate;
        this.mailService = mailService;
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

    public Boolean updateStatus(UUID id, AttributeValue status, String unvan, String totalTl, Instant startDate, Instant endDate) throws Exception {
        try {
            BigDecimal total = new BigDecimal(totalTl);
            Instant okeyFirst = Instant.now();
            repository.updateStatusById(status, unvan, total, startDate, endDate, okeyFirst, id);

            String start = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(startDate);
            String end = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(endDate);
            FuelLimit fuelLimit = repository.findById(id).get();
            String mailOwner = fuelLimit.getOwner().getEposta();
            mailService.sendEmail(mailOwner,
                "MeteorPanel - Ek Limit Talebi",fuelLimit.getOwner().getFullName() + ", " +
                    fuelLimit.getCurcode() + " cari kodlu müşteriniz için " + start + "  -  " + end + " tarihleri için" +
                    " yapmış olduğunuz " + fuelLimit.getFuelTl().toString() + " TL lik ek limit talebiniz " + fuelLimit.getStatus().getLabel(),
                false,false);


            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean limitControlService(String curcode, BigDecimal total) throws IOException {
        Double toplamDbsLimit = 0.0;
        String apiUrl = "https://srv.nccpetrol.com/DisServis/riskdetayget?ServisSifre=14ADa23.&CariKodu=" + curcode + "&FirmaKodu=875257";
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode[] items = objectMapper.readValue(responseBody, JsonNode[].class);

            /*Double toplamLimit = 0.0;
            Double mevcutTuketim = 0.0;
            String cariUnvan = "";*/

            for (JsonNode item : items) {
                //cariUnvan = item.get("CariUnvan").asText();
                Double dbsLimit = item.get("DbsLimit").asDouble();
                /*Double onayliFatura = item.get("OnayliFatura").asDouble();
                Double limit = item.get("KullanilabilirLimit").asDouble();
                String bankaAdi = item.get("BankaAdi").asText();
                mevcutTuketim = item.get("MevcutTuketim").asDouble();
                Double nakitRisk = bankaAdi.equals("GARANTİ BANKASI") || bankaAdi.equals("ING BANK") ?
                    dbsLimit - onayliFatura - item.get("BankadanGelenKullanilabilirLimit").asDouble() :
                    item.get("NakitRisk").asDouble();*/

                toplamDbsLimit += dbsLimit;
                //toplamLimit = limit;
            }
        } else {
            throw new RuntimeException("Servis çağrısı başarısız: " + response.getStatusCode());
        }
        BigDecimal totalDbsLimit = new BigDecimal(toplamDbsLimit);
        BigDecimal yuzdeOn = new BigDecimal("0.10");
        BigDecimal yuzdeYirmiBes = new BigDecimal("0.25");
        System.out.println(total);
        System.out.println(totalDbsLimit.multiply(yuzdeOn));
        if (totalDbsLimit.multiply(yuzdeOn).compareTo(total) >= 0 && !getCurrentUser().getId().equals(93L)) {
            return true;
        } else if (totalDbsLimit.multiply(yuzdeYirmiBes).compareTo(total) >= 0 && getCurrentUser().getId().equals(93L)) {
            return true;
        } else {
            return false;
        }
    }
}
