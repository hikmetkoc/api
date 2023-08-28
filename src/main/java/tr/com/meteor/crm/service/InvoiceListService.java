package tr.com.meteor.crm.service;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.hibernate.engine.transaction.jta.platform.internal.SapNetWeaverJtaPlatform;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.InvoiceListRepository;
import tr.com.meteor.crm.repository.SapSoapRepository;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class InvoiceListService extends GenericIdNameAuditingEntityService<InvoiceList, UUID, InvoiceListRepository> {

    public InvoiceListService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                              BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                              BaseConfigurationService baseConfigurationService,
                              InvoiceListRepository repository, SapSoapRepository sapSoapRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            InvoiceList.class, repository);
        this.sapSoapRepository = sapSoapRepository;
    }

    public final SapSoapRepository sapSoapRepository;

    public byte[] generateExcelOrderReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
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

        List<InvoiceList> InvoiceLists = getData(null, request, false).getBody();

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
        XSSFCell headerBuyOkeyCell = headerRow.createCell(columnIndex++);   //Teklif Onay Durumu
        /*XSSFCell headerCompanyCell = headerRow.createCell(columnIndex++);     //Şirket
        XSSFCell headerDepartmentCell = headerRow.createCell(columnIndex++);     //Birim*/
        XSSFCell headerSupplierCell = headerRow.createCell(columnIndex++);     //Tedarikçi
        XSSFCell headerStartCell = headerRow.createCell(columnIndex++);     //Teklif Başlangıç
        XSSFCell headerEndCell = headerRow.createCell(columnIndex++);     //Teklif Bitiş
        XSSFCell headerFirstCell = headerRow.createCell(columnIndex++);     //1.Onay Tarihi
        XSSFCell headerSecondCell = headerRow.createCell(columnIndex++);     //2.Onay Tarihi
        XSSFCell headerMoneyCell = headerRow.createCell(columnIndex++);      //Tutar
        XSSFCell headerMoneyTypeCell = headerRow.createCell(columnIndex++);      //Para Birimi
        XSSFCell headerBuyTypeCell = headerRow.createCell(columnIndex++);    //Ödeme Yöntemi
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);    //Teklif Gerekçesi
        XSSFCell headerOkeyCell = headerRow.createCell(columnIndex++);   //Onay Durumu
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);       //Oluşturulma Tarihi

        headerCodeCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerAssignerCell.setCellStyle(headerCellStyle);
        headerSecondAssignerCell.setCellStyle(headerCellStyle);
        headerBuyOkeyCell.setCellStyle(headerCellStyle);
        /*headerCompanyCell.setCellStyle(headerCellStyle);
        headerDepartmentCell.setCellStyle(headerCellStyle);*/
        headerSupplierCell.setCellStyle(headerCellStyle);
        headerStartCell.setCellStyle(headerCellStyle);
        headerEndCell.setCellStyle(headerCellStyle);
        headerFirstCell.setCellStyle(headerCellStyle);
        headerSecondCell.setCellStyle(headerCellStyle);
        headerMoneyCell.setCellStyle(headerCellStyle);
        headerMoneyTypeCell.setCellStyle(headerCellStyle);
        headerBuyTypeCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerOkeyCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);

        headerCodeCell.setCellValue("Satın Alma Kodu");
        headerOwnerCell.setCellValue("Satın Alma Sorumlusu");
        headerAssignerCell.setCellValue("1.Onaycı");
        headerSecondAssignerCell.setCellValue("2.Onaycı");
        headerBuyOkeyCell.setCellValue("Teklif Onay Durumu");
        /*headerCompanyCell.setCellValue("Şirket");
        headerDepartmentCell.setCellValue("Birim");*/
        headerSupplierCell.setCellValue("Tedarikçi");
        headerStartCell.setCellValue("Teklif Başlangıç Tarihi");
        headerEndCell.setCellValue("Teklif Bitiş Tarihi");
        headerFirstCell.setCellValue("1.Onay Tarihi");
        headerSecondCell.setCellValue("2.Onay Tarihi");
        headerMoneyCell.setCellValue("Tutar");
        headerMoneyTypeCell.setCellValue("Para Birimi");
        headerBuyTypeCell.setCellValue("Ödeme Yöntemi");
        headerDescriptionCell.setCellValue("Teklif Gerekçesi");
        headerOkeyCell.setCellValue("Onay Durumu");
        headerCreatedDateCell.setCellValue("Oluşturulma Tarihi");

        for (InvoiceList invoiceList : InvoiceLists) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell codeCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell assignerCell = row.createCell(columnIndex++);
            XSSFCell secondAssignerCell = row.createCell(columnIndex++);
            XSSFCell buyOkeyCell = row.createCell(columnIndex++);
            /*XSSFCell companyCell = row.createCell(columnIndex++);
            XSSFCell departmentCell = row.createCell(columnIndex++);*/
            XSSFCell supplierCell = row.createCell(columnIndex++);
            XSSFCell startCell = row.createCell(columnIndex++);
            XSSFCell endCell = row.createCell(columnIndex++);
            XSSFCell firstCell = row.createCell(columnIndex++);
            XSSFCell secondCell = row.createCell(columnIndex++);
            XSSFCell moneyCell = row.createCell(columnIndex++);
            XSSFCell moneyTypeCell = row.createCell(columnIndex++);
            XSSFCell buyTypeCell = row.createCell(columnIndex++);
            XSSFCell descriptionCell = row.createCell(columnIndex++);
            XSSFCell okeyCell = row.createCell(columnIndex++);
            XSSFCell createdDateCell = row.createCell(columnIndex++);

            //codeCell.setCellValue(paymentOrder.getStcode());

            if (invoiceList.getOwner() != null) {
                ownerCell.setCellValue(invoiceList.getOwner().getFullName());
            }

            if (invoiceList.getCustomer() != null) {
                supplierCell.setCellValue(invoiceList.getCustomer().getCommercialTitle());
            }

            if (invoiceList.getMoneyType() != null) {
                moneyTypeCell.setCellValue(invoiceList.getMoneyType().getLabel());
            }

            if (invoiceList.getInvoiceStatus() != null) {
                okeyCell.setCellValue(invoiceList.getInvoiceStatus().getLabel());
            }
            if (invoiceList.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(invoiceList.getCreatedDate()));
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

    public String getEttntByInvoiceNum(String invoiceNum) throws Exception {
        List<SapSoap> sapSoap = sapSoapRepository.findByFaturano(invoiceNum);
        String veri = "";
        for (SapSoap sapSoap1: sapSoap) {
            veri = sapSoap1.getFpdf();
        }
        return veri;
    }

    public void updateStatus(UUID id, AttributeValue status, String description) throws Exception{
        repository.updateStatusById(status, id, description);
    }
}
