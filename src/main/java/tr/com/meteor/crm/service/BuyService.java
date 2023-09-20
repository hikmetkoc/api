package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Buy;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.BuyRepository;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class BuyService extends GenericIdNameAuditingEntityService<Buy, UUID, BuyRepository> {
    public BuyService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                      BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                      BaseConfigurationService baseConfigurationService,
                      BuyRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Buy.class, repository);
    }

    public List<Buy> saveWeekly(List<Buy> Buys) throws Exception {
        List<Buy> BuyList = new ArrayList<>();
        for (Buy Buy : Buys) {
            if (!StringUtils.isBlank(Buy.getDescription()) && Buy.getId() != null) {
                BuyList.add(update(getCurrentUser(), Buy));
            } else if (Buy.getId() == null) {
                /*Buy.setType(BuyType.KOLAY_AJANDA.getAttributeValue());
                Buy.setStatus(BuyStatus.YENI.getAttributeValue());*/
                BuyList.add(add(getCurrentUser(), Buy));
            } else if (StringUtils.isBlank(Buy.getDescription())) {
                delete(getCurrentUser(), Buy.getId());
            }
        }

        return BuyList;
    }

    /*public void updateBuy(UUID buyid) throws Exception {
        if (buyid == null) {
            throw new Exception("Gönderilen ID BOŞ");
        }
        Buy buy = repository.findById(buyid).orElseThrow(() -> new Exception("Buy bulunamadı"));
        List<ContProduct> contProducts = contProductRepository.findByBuyId(buyid);
        int approvedCount = 0;
        BigDecimal totalBuy = BigDecimal.ZERO;
        for (ContProduct cp : contProducts) {
            totalBuy = totalBuy.add(cp.getFuelTl());
            if (cp.getStatus()) {
                approvedCount++;
            }
        }
        buy.setProductStatus(approvedCount + "/" + contProducts.size());
        buy.setFuelTl(totalBuy);tory.save
        repository.save(buy);
    }*/
    public byte[] generateExcelBuyReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
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

        List<Buy> Buys = getData(null, request, false).getBody();

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
        XSSFCell headerSecondAssignerCell = headerRow.createCell(columnIndex++);  //Onaycı
        XSSFCell headerBuyOkeyCell = headerRow.createCell(columnIndex++);   //Onay Durumu
        XSSFCell headerSuggestCell = headerRow.createCell(columnIndex++);   //Oneri
        XSSFCell headerSupplierCell = headerRow.createCell(columnIndex++);     //Tedarikçi
        XSSFCell headerStartCell = headerRow.createCell(columnIndex++);     //Teklif Başlangıç
        XSSFCell headerEndCell = headerRow.createCell(columnIndex++);     //Teklif Bitiş
        XSSFCell headerMaturityCell = headerRow.createCell(columnIndex++);     //Vade Tarihi
        XSSFCell headerFirstCell = headerRow.createCell(columnIndex++);     //Öneri Tarihi
        XSSFCell headerSecondCell = headerRow.createCell(columnIndex++);     //Onay Tarihi
        XSSFCell headerMoneyCell = headerRow.createCell(columnIndex++);      //Tutar
        XSSFCell headerMoneyTypeCell = headerRow.createCell(columnIndex++);      //Para Birimi
        XSSFCell headerBuyTypeCell = headerRow.createCell(columnIndex++);    //Ödeme Yöntemi
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);    //Teklif Gerekçesi
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);       //Oluşturulma Tarihi

        headerCodeCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerSecondAssignerCell.setCellStyle(headerCellStyle);
        headerBuyOkeyCell.setCellStyle(headerCellStyle);
        headerSuggestCell.setCellStyle(headerCellStyle);
        headerSupplierCell.setCellStyle(headerCellStyle);
        headerStartCell.setCellStyle(headerCellStyle);
        headerEndCell.setCellStyle(headerCellStyle);
        headerMaturityCell.setCellStyle(headerCellStyle);
        headerFirstCell.setCellStyle(headerCellStyle);
        headerSecondCell.setCellStyle(headerCellStyle);
        headerMoneyCell.setCellStyle(headerCellStyle);
        headerMoneyTypeCell.setCellStyle(headerCellStyle);
        headerBuyTypeCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);

        headerCodeCell.setCellValue("Satın Alma Kodu");
        headerOwnerCell.setCellValue("Satın Alma Sorumlusu");
        headerSecondAssignerCell.setCellValue("Onaycı");
        headerBuyOkeyCell.setCellValue("Onay Durumu");
        headerSuggestCell.setCellValue("Öneri");
        headerSupplierCell.setCellValue("Tedarikçi");
        headerStartCell.setCellValue("Teklif Başlangıç Tarihi");
        headerEndCell.setCellValue("Teklif Bitiş Tarihi");
        headerMaturityCell.setCellValue("Vade Tarihi");
        headerFirstCell.setCellValue("Öneri Tarihi");
        headerSecondCell.setCellValue("Onay Tarihi");
        headerMoneyCell.setCellValue("Tutar");
        headerMoneyTypeCell.setCellValue("Para Birimi");
        headerBuyTypeCell.setCellValue("Ödeme Yöntemi");
        headerDescriptionCell.setCellValue("Teklif Gerekçesi");
        headerCreatedDateCell.setCellValue("Oluşturulma Tarihi");

        for (Buy Buy : Buys) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell codeCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell secondAssignerCell = row.createCell(columnIndex++);
            XSSFCell buyOkeyCell = row.createCell(columnIndex++);
            XSSFCell suggestCell = row.createCell(columnIndex++);
            XSSFCell supplierCell = row.createCell(columnIndex++);
            XSSFCell startCell = row.createCell(columnIndex++);
            XSSFCell endCell = row.createCell(columnIndex++);
            XSSFCell maturityCell = row.createCell(columnIndex++);
            XSSFCell firstCell = row.createCell(columnIndex++);
            XSSFCell secondCell = row.createCell(columnIndex++);
            XSSFCell moneyCell = row.createCell(columnIndex++);
            XSSFCell moneyTypeCell = row.createCell(columnIndex++);
            XSSFCell buyTypeCell = row.createCell(columnIndex++);
            XSSFCell descriptionCell = row.createCell(columnIndex++);
            XSSFCell createdDateCell = row.createCell(columnIndex++);

            codeCell.setCellValue(Buy.getStcode());

            if (Buy.getOwner() != null) {
                ownerCell.setCellValue(Buy.getOwner().getFullName());
            }
            if (Buy.getSecondAssigner() != null) {
                secondAssignerCell.setCellValue(Buy.getSecondAssigner().getFullName());
            }
            if (Buy.getQuoteStatus() != null) {
                buyOkeyCell.setCellValue(Buy.getQuoteStatus().getLabel());
            }
            suggestCell.setCellValue(Buy.getSuggest().toString());

            if (Buy.getCustomer() != null) {
                supplierCell.setCellValue(Buy.getCustomer().getCommercialTitle());
            }
            if (Buy.getStartDate() != null) {
                startCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Buy.getStartDate()));
            }
            if (Buy.getEndDate() != null) {
                endCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Buy.getEndDate()));
            }
            if (Buy.getMaturityDate() != null) {
                maturityCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Buy.getMaturityDate()));
            }
            if (Buy.getOkeyFirst() != null) {
                firstCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Buy.getOkeyFirst()));
            }
            if (Buy.getOkeySecond() != null) {
                secondCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Buy.getOkeySecond()));
            }
            if (Buy.getFuelTl() != null) {
                moneyCell.setCellValue(Buy.getFuelTl().floatValue());
            }
            if (Buy.getMoneyType() != null) {
                moneyTypeCell.setCellValue(Buy.getMoneyType().getLabel());
            }
            if (Buy.getPaymentMethod() != null) {
                buyTypeCell.setCellValue(Buy.getPaymentMethod().getLabel());
            }
            if (StringUtils.isNotBlank(Buy.getDescription())) {
                descriptionCell.setCellValue(Buy.getDescription());
            }
            if (Buy.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Buy.getCreatedDate()));
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
