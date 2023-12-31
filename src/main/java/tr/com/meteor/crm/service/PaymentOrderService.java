package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.dto.PaymentOrderDTO;
import tr.com.meteor.crm.utils.attributevalues.InvoiceStatus;
import tr.com.meteor.crm.utils.attributevalues.PaymentStatus;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.jasper.rest.errors.RecordNotFoundException;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PaymentOrderService extends GenericIdNameAuditingEntityService<PaymentOrder, UUID, PaymentOrderRepository> {

    private final AttributeValueRepository attributeValueRepository;
    private final SpendRepository spendRepository;
    private final InvoiceListRepository invoiceListRepository;
    private final StoreRepository storeRepository;

    private final PostaGuverciniService postaGuverciniService;
    private final MailService mailService;
    public PaymentOrderService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                               BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                               BaseConfigurationService baseConfigurationService,
                               PaymentOrderRepository repository, AttributeValueRepository attributeValueRepository, SpendRepository spendRepository, InvoiceListRepository invoiceListRepository, StoreRepository storeRepository, PostaGuverciniService postaGuverciniService, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            PaymentOrder.class, repository);
        this.attributeValueRepository = attributeValueRepository;
        this.spendRepository = spendRepository;
        this.invoiceListRepository = invoiceListRepository;
        this.storeRepository = storeRepository;
        this.postaGuverciniService = postaGuverciniService;
        this.mailService = mailService;
    }
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
                    Filter.FilterItem("muhasebeci.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("assigner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("secondAssigner.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<PaymentOrder> PaymentOrders = getData(null, request, false).getBody();

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

        XSSFCell headerCodeCell = headerRow.createCell(columnIndex++);     //Satın Alma Kodu
        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);     //Talimat Oluşturan
        XSSFCell headerAssignerCell = headerRow.createCell(columnIndex++);  //1.Onaycı
        XSSFCell headerSecondAssignerCell = headerRow.createCell(columnIndex++);  //2.Onaycı
        XSSFCell headerInvoiceDateCell = headerRow.createCell(columnIndex++);  //Fatura Tarihi
        XSSFCell headerMaturityDateCell = headerRow.createCell(columnIndex++);  //Vade Tarihi
        XSSFCell headerInvoiceNumCell = headerRow.createCell(columnIndex++);  //Fatura Numarası
        XSSFCell headerCustomerCell = headerRow.createCell(columnIndex++);  //Ödeme Yapılacak Firma
        XSSFCell headerSirketCell = headerRow.createCell(columnIndex++);  //Fatura Kesilen Şirket
        XSSFCell headerOdemeYapanSirketCell = headerRow.createCell(columnIndex++);  //Ödeme Yapan Şirket
        XSSFCell headerCostCell = headerRow.createCell(columnIndex++);  //Maliyet Yeri
        XSSFCell headerPaymentSubjectCell = headerRow.createCell(columnIndex++);  //Ödeme Konusu
        XSSFCell headerAmountCell = headerRow.createCell(columnIndex++);  //Toplam Tutar
        XSSFCell headerPayAmountCell = headerRow.createCell(columnIndex++);  //Ödenen Tutar
        XSSFCell headerNextAmountCell = headerRow.createCell(columnIndex++);  //Kalan Tutar
        XSSFCell headerMoneyTypeCell = headerRow.createCell(columnIndex++);  //Faturadaki Para Birimi
        XSSFCell headerIbanCell = headerRow.createCell(columnIndex++);  //Iban
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);  //Açıklama
        XSSFCell headerStatusCell = headerRow.createCell(columnIndex++);  //Onay Durumu
        XSSFCell headerFirstCell = headerRow.createCell(columnIndex++);     //1.Onay Tarihi
        XSSFCell headerMuhCell = headerRow.createCell(columnIndex++);     //Muhasebe Onay Tarihi
        XSSFCell headerSecondCell = headerRow.createCell(columnIndex++);     //2.Onay Tarihi
        XSSFCell headerCancelDateCell = headerRow.createCell(columnIndex++);     //Reddetme Tarihi
        XSSFCell headerCancelUserCell = headerRow.createCell(columnIndex++);     //Reddeden
        XSSFCell headerPaymentTypeCell = headerRow.createCell(columnIndex++);     //Ödeme Şekli
        XSSFCell headerPaymentStyleCell = headerRow.createCell(columnIndex++);     //Ödenecek Para Birimi
        XSSFCell headerExchangeCell = headerRow.createCell(columnIndex++);     //Kur Tarihi
        XSSFCell headerPayTlCell = headerRow.createCell(columnIndex++);     //Ödenen Tl Tutarı
        XSSFCell headerSuccessCell = headerRow.createCell(columnIndex++);     //Ödeme Yapıldı Mı?
        XSSFCell headerAutoPayCell = headerRow.createCell(columnIndex++);     //Otomatik Ödemede Mi?
        XSSFCell headerDekontCell = headerRow.createCell(columnIndex++);     //Dekont Talep Edildi Mi?
        XSSFCell headerKismiCell = headerRow.createCell(columnIndex++);     //Kısmi Ödeme Mi?
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);       //Oluşturulma Tarihi

        headerCodeCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerAssignerCell.setCellStyle(headerCellStyle);
        headerSecondAssignerCell.setCellStyle(headerCellStyle);
        headerInvoiceDateCell.setCellStyle(headerCellStyle);
        headerMaturityDateCell.setCellStyle(headerCellStyle);
        headerInvoiceNumCell.setCellStyle(headerCellStyle);
        headerCustomerCell.setCellStyle(headerCellStyle);
        headerSirketCell.setCellStyle(headerCellStyle);
        headerOdemeYapanSirketCell.setCellStyle(headerCellStyle);
        headerCostCell.setCellStyle(headerCellStyle);
        headerPaymentSubjectCell.setCellStyle(headerCellStyle);
        headerAmountCell.setCellStyle(headerCellStyle);
        headerPayAmountCell.setCellStyle(headerCellStyle);
        headerNextAmountCell.setCellStyle(headerCellStyle);
        headerMoneyTypeCell.setCellStyle(headerCellStyle);
        headerIbanCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerStatusCell.setCellStyle(headerCellStyle);
        headerFirstCell.setCellStyle(headerCellStyle);
        headerMuhCell.setCellStyle(headerCellStyle);
        headerSecondCell.setCellStyle(headerCellStyle);
        headerCancelDateCell.setCellStyle(headerCellStyle);
        headerCancelUserCell.setCellStyle(headerCellStyle);
        headerPaymentTypeCell.setCellStyle(headerCellStyle);
        headerPaymentStyleCell.setCellStyle(headerCellStyle);
        headerExchangeCell.setCellStyle(headerCellStyle);
        headerPayTlCell.setCellStyle(headerCellStyle);
        headerSuccessCell.setCellStyle(headerCellStyle);
        headerAutoPayCell.setCellStyle(headerCellStyle);
        headerDekontCell.setCellStyle(headerCellStyle);
        headerKismiCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);


        headerCodeCell.setCellValue("Satın Alma Kodu");
        headerOwnerCell.setCellValue("Talimat Oluşturan");
        headerAssignerCell.setCellValue("1.Onaycı");
        headerSecondAssignerCell.setCellValue("2.Onaycı");
        headerInvoiceDateCell.setCellValue("Fatura Tarihi");
        headerMaturityDateCell.setCellValue("Vade Tarihi");
        headerInvoiceNumCell.setCellValue("Fatura Numarası");
        headerCustomerCell.setCellValue("Ödeme Yapılacak Firma");
        headerSirketCell.setCellValue("Fatura Kesilen Şirket");
        headerOdemeYapanSirketCell.setCellValue("Ödeme Yapan Şirket");
        headerCostCell.setCellValue("Maliyet Yeri");
        headerPaymentSubjectCell.setCellValue("Ödeme Konusu");
        headerAmountCell.setCellValue("Toplam Tutar");
        headerPayAmountCell.setCellValue("Ödenen Tutar");
        headerNextAmountCell.setCellValue("Kalan Tutar");
        headerMoneyTypeCell.setCellValue("Faturadaki Para Birimi");
        headerIbanCell.setCellValue("Iban");
        headerDescriptionCell.setCellValue("Açıklama");
        headerStatusCell.setCellValue("Onay Durumu");
        headerFirstCell.setCellValue("1.Onay Tarihi");
        headerMuhCell.setCellValue("Muhasebe Onay Tarihi");
        headerSecondCell.setCellValue("2.Onay Tarihi");
        headerCancelDateCell.setCellValue("Reddetme Tarihi");
        headerCancelUserCell.setCellValue("Reddeden");
        headerPaymentTypeCell.setCellValue("Ödeme Şekli");
        headerPaymentStyleCell.setCellValue("Ödenecek Para Birimi");
        headerExchangeCell.setCellValue("Kur Tarihi");
        headerPayTlCell.setCellValue("Ödenen Tl Tutarı");
        headerSuccessCell.setCellValue("Ödeme Yapıldı Mı?");
        headerAutoPayCell.setCellValue("Otomatik Ödemede Mi?");
        headerDekontCell.setCellValue("Dekont Talep Edildi Mi?");
        headerKismiCell.setCellValue("Kısmi Ödeme Mi?");
        headerCreatedDateCell.setCellValue("Oluşturulma Tarihi");

        for (PaymentOrder paymentOrder : PaymentOrders) {
            columnIndex = 0;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell codeCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell assignerCell = row.createCell(columnIndex++);
            XSSFCell secondAssignerCell = row.createCell(columnIndex++);
            XSSFCell InvoiceDateCell = row.createCell(columnIndex++);  //Fatura Tarihi
            XSSFCell MaturityDateCell = row.createCell(columnIndex++);  //Vade Tarihi
            XSSFCell InvoiceNumCell = row.createCell(columnIndex++);  //Fatura Numarası
            XSSFCell CustomerCell = row.createCell(columnIndex++);  //Ödeme Yapılacak Firma
            XSSFCell SirketCell = row.createCell(columnIndex++);  //Fatura Kesilen Şirket
            XSSFCell OdemeYapanSirketCell = row.createCell(columnIndex++);  //Ödeme Yapan Şirket
            XSSFCell CostCell = row.createCell(columnIndex++);  //Maliyet Yeri
            XSSFCell PaymentSubjectCell = row.createCell(columnIndex++);  //Ödeme Konusu
            XSSFCell AmountCell = row.createCell(columnIndex++);  //Toplam Tutar
            XSSFCell PayAmountCell = row.createCell(columnIndex++);  //Ödenen Tutar
            XSSFCell NextAmountCell = row.createCell(columnIndex++);  //Kalan Tutar
            XSSFCell MoneyTypeCell = row.createCell(columnIndex++);  //Faturadaki Para Birimi
            XSSFCell IbanCell = row.createCell(columnIndex++);  //Iban
            XSSFCell DescriptionCell = row.createCell(columnIndex++);  //Açıklama
            XSSFCell StatusCell = row.createCell(columnIndex++);  //Onay Durumu
            XSSFCell FirstCell = row.createCell(columnIndex++);     //1.Onay Tarihi
            XSSFCell MuhCell = row.createCell(columnIndex++);     //Muhasebe Onay Tarihi
            XSSFCell SecondCell = row.createCell(columnIndex++);     //2.Onay Tarihi
            XSSFCell CancelDateCell = row.createCell(columnIndex++);     //Reddetme Tarihi
            XSSFCell CancelUserCell = row.createCell(columnIndex++);     //Reddeden
            XSSFCell PaymentTypeCell = row.createCell(columnIndex++);     //Ödeme Şekli
            XSSFCell PaymentStyleCell = row.createCell(columnIndex++);     //Ödenecek Para Birimi
            XSSFCell ExchangeCell = row.createCell(columnIndex++);     //Kur Tarihi
            XSSFCell PayTlCell = row.createCell(columnIndex++);         //Ödenen Tl Tutarı
            XSSFCell SuccessCell = row.createCell(columnIndex++);     //Ödeme Yapıldı Mı?
            XSSFCell AutoPayCell = row.createCell(columnIndex++);     //Otomatik Ödemede Mi?
            XSSFCell DekontCell = row.createCell(columnIndex++);     //Dekont Talep Edildi Mi?
            XSSFCell KismiCell = row.createCell(columnIndex++);     //Kısmi Ödeme Mi?
            XSSFCell CreatedDateCell = row.createCell(columnIndex++);       //Oluşturulma Tarihi

            if (paymentOrder.getStore() != null) {
                codeCell.setCellValue(paymentOrder.getStore().getStcode());
            }
            if (paymentOrder.getOwner() != null) {
                ownerCell.setCellValue(paymentOrder.getOwner().getFullName());
            }
            if (paymentOrder.getAssigner() != null) {
                assignerCell.setCellValue(paymentOrder.getAssigner().getFullName());
            }
            if (paymentOrder.getSecondAssigner() != null) {
                secondAssignerCell.setCellValue(paymentOrder.getSecondAssigner().getFullName());
            }

            if (paymentOrder.getInvoiceDate() != null) {
                InvoiceDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getInvoiceDate()));
            }
            if (paymentOrder.getMaturityDate() != null) {
                MaturityDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getMaturityDate()));
            }
            InvoiceNumCell.setCellValue(paymentOrder.getInvoiceNum());

            if (paymentOrder.getCustomer() != null) {
                CustomerCell.setCellValue(paymentOrder.getCustomer().getCommercialTitle());
            }

            if (paymentOrder.getSirket() != null) {
                SirketCell.setCellValue(paymentOrder.getSirket().getLabel());
            }
            if (paymentOrder.getOdemeYapanSirket() != null) {
                OdemeYapanSirketCell.setCellValue(paymentOrder.getOdemeYapanSirket().getLabel());
            }
            if (paymentOrder.getCost() != null) {
                CostCell.setCellValue(paymentOrder.getCost().getLabel());
            }
            if (paymentOrder.getPaymentSubject() != null) {
                PaymentSubjectCell.setCellValue(paymentOrder.getPaymentSubject().getLabel());
            }

            if (paymentOrder.getAmount() != null) {
                AmountCell.setCellValue(paymentOrder.getAmount().floatValue());
            }
            if (paymentOrder.getPayamount() != null) {
                PayAmountCell.setCellValue(paymentOrder.getPayamount().floatValue());
            }
            if (paymentOrder.getNextamount() != null) {
                NextAmountCell.setCellValue(paymentOrder.getNextamount().floatValue());
            }

            if (paymentOrder.getMoneyType() != null) {
                MoneyTypeCell.setCellValue(paymentOrder.getMoneyType().getLabel());
            }
            if (paymentOrder.getIban().getName() != null) {
                IbanCell.setCellValue(paymentOrder.getIban().getName());
            }
            if (StringUtils.isNotBlank(paymentOrder.getDescription())) {
                DescriptionCell.setCellValue(paymentOrder.getDescription());
            }
            if (paymentOrder.getStatus() != null) {
                StatusCell.setCellValue(paymentOrder.getStatus().getLabel());
            }
            if (paymentOrder.getOkeyFirst() != null) {
                FirstCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getOkeyFirst()));
            }
            if (paymentOrder.getOkeyMuh() != null) {
                MuhCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getOkeyMuh()));
            }
            if (paymentOrder.getOkeySecond() != null) {
                SecondCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getOkeySecond()));
            }
            if (paymentOrder.getCancelDate() != null) {
                CancelDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getCancelDate()));
            }

            if (paymentOrder.getCancelUser() != null) {
                CancelUserCell.setCellValue(paymentOrder.getCancelUser().getFullName());
            }

            if (paymentOrder.getPaymentType() != null) {
                PaymentTypeCell.setCellValue(paymentOrder.getPaymentType().getLabel());
            }
            if (paymentOrder.getPaymentStyle() != null) {
                PaymentStyleCell.setCellValue(paymentOrder.getPaymentStyle().getLabel());
            }
            if (paymentOrder.getExchange() != null) {
                ExchangeCell.setCellValue(paymentOrder.getExchange().getLabel());
            }
            if (paymentOrder.getPayTl() != null) {
                PayTlCell.setCellValue(paymentOrder.getPayTl().toString());
            }
            if (paymentOrder.getSuccess() != null) {
                SuccessCell.setCellValue(paymentOrder.getSuccess().toString());
            }
            if (paymentOrder.getAutopay() != null) {
                AutoPayCell.setCellValue(paymentOrder.getAutopay().toString());
            }
            if (paymentOrder.getDekont() != null) {
                DekontCell.setCellValue(paymentOrder.getDekont().toString());
            }
            if (paymentOrder.getKismi() != null) {
                KismiCell.setCellValue(paymentOrder.getKismi().toString());
            }
            if (paymentOrder.getCreatedDate() != null) {
                CreatedDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getCreatedDate()));
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

    public byte[] generateSelectedExcelReport(List<UUID> ids, User currentUser) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build()
            .page(0)
            .size(Integer.MAX_VALUE)
            .filter(
            Filter.And(
                Filter.FilterItem("id", FilterItem.Operator.IN, ids),
                Filter.Or(
                    Filter.FilterItem("muhasebeci.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("assigner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("secondAssigner.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<PaymentOrder> PaymentOrders = getData(null, request, false).getBody();

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

        XSSFCell headerCodeCell = headerRow.createCell(columnIndex++);     //Satın Alma Kodu
        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);     //Talimat Oluşturan
        XSSFCell headerAssignerCell = headerRow.createCell(columnIndex++);  //1.Onaycı
        XSSFCell headerSecondAssignerCell = headerRow.createCell(columnIndex++);  //2.Onaycı
        XSSFCell headerInvoiceDateCell = headerRow.createCell(columnIndex++);  //Fatura Tarihi
        XSSFCell headerMaturityDateCell = headerRow.createCell(columnIndex++);  //Vade Tarihi
        XSSFCell headerInvoiceNumCell = headerRow.createCell(columnIndex++);  //Fatura Numarası
        XSSFCell headerCustomerCell = headerRow.createCell(columnIndex++);  //Ödeme Yapılacak Firma
        XSSFCell headerSirketCell = headerRow.createCell(columnIndex++);  //Fatura Kesilen Şirket
        XSSFCell headerOdemeYapanSirketCell = headerRow.createCell(columnIndex++);  //Ödeme Yapan Şirket
        XSSFCell headerCostCell = headerRow.createCell(columnIndex++);  //Maliyet Yeri
        XSSFCell headerPaymentSubjectCell = headerRow.createCell(columnIndex++);  //Ödeme Konusu
        XSSFCell headerAmountCell = headerRow.createCell(columnIndex++);  //Toplam Tutar
        XSSFCell headerPayAmountCell = headerRow.createCell(columnIndex++);  //Ödenen Tutar
        XSSFCell headerNextAmountCell = headerRow.createCell(columnIndex++);  //Kalan Tutar
        XSSFCell headerMoneyTypeCell = headerRow.createCell(columnIndex++);  //Faturadaki Para Birimi
        XSSFCell headerIbanCell = headerRow.createCell(columnIndex++);  //Iban
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);  //Açıklama
        XSSFCell headerStatusCell = headerRow.createCell(columnIndex++);  //Onay Durumu
        XSSFCell headerFirstCell = headerRow.createCell(columnIndex++);     //1.Onay Tarihi
        XSSFCell headerMuhCell = headerRow.createCell(columnIndex++);     //Muhasebe Onay Tarihi
        XSSFCell headerSecondCell = headerRow.createCell(columnIndex++);     //2.Onay Tarihi
        XSSFCell headerCancelDateCell = headerRow.createCell(columnIndex++);     //Reddetme Tarihi
        XSSFCell headerCancelUserCell = headerRow.createCell(columnIndex++);     //Reddeden
        XSSFCell headerPaymentTypeCell = headerRow.createCell(columnIndex++);     //Ödeme Şekli
        XSSFCell headerPaymentStyleCell = headerRow.createCell(columnIndex++);     //Ödenecek Para Birimi
        XSSFCell headerExchangeCell = headerRow.createCell(columnIndex++);     //Kur Tarihi
        XSSFCell headerPayTlCell = headerRow.createCell(columnIndex++);     //Ödenen Tl Tutarı
        XSSFCell headerSuccessCell = headerRow.createCell(columnIndex++);     //Ödeme Yapıldı Mı?
        XSSFCell headerAutoPayCell = headerRow.createCell(columnIndex++);     //Otomatik Ödemede Mi?
        XSSFCell headerDekontCell = headerRow.createCell(columnIndex++);     //Dekont Talep Edildi Mi?
        XSSFCell headerKismiCell = headerRow.createCell(columnIndex++);     //Kısmi Ödeme Mi?
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);       //Oluşturulma Tarihi

        headerCodeCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerAssignerCell.setCellStyle(headerCellStyle);
        headerSecondAssignerCell.setCellStyle(headerCellStyle);
        headerInvoiceDateCell.setCellStyle(headerCellStyle);
        headerMaturityDateCell.setCellStyle(headerCellStyle);
        headerInvoiceNumCell.setCellStyle(headerCellStyle);
        headerCustomerCell.setCellStyle(headerCellStyle);
        headerSirketCell.setCellStyle(headerCellStyle);
        headerOdemeYapanSirketCell.setCellStyle(headerCellStyle);
        headerCostCell.setCellStyle(headerCellStyle);
        headerPaymentSubjectCell.setCellStyle(headerCellStyle);
        headerAmountCell.setCellStyle(headerCellStyle);
        headerPayAmountCell.setCellStyle(headerCellStyle);
        headerNextAmountCell.setCellStyle(headerCellStyle);
        headerMoneyTypeCell.setCellStyle(headerCellStyle);
        headerIbanCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerStatusCell.setCellStyle(headerCellStyle);
        headerFirstCell.setCellStyle(headerCellStyle);
        headerMuhCell.setCellStyle(headerCellStyle);
        headerSecondCell.setCellStyle(headerCellStyle);
        headerCancelDateCell.setCellStyle(headerCellStyle);
        headerCancelUserCell.setCellStyle(headerCellStyle);
        headerPaymentTypeCell.setCellStyle(headerCellStyle);
        headerPaymentStyleCell.setCellStyle(headerCellStyle);
        headerExchangeCell.setCellStyle(headerCellStyle);
        headerPayTlCell.setCellStyle(headerCellStyle);
        headerSuccessCell.setCellStyle(headerCellStyle);
        headerAutoPayCell.setCellStyle(headerCellStyle);
        headerDekontCell.setCellStyle(headerCellStyle);
        headerKismiCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);


        headerCodeCell.setCellValue("Satın Alma Kodu");
        headerOwnerCell.setCellValue("Talimat Oluşturan");
        headerAssignerCell.setCellValue("1.Onaycı");
        headerSecondAssignerCell.setCellValue("2.Onaycı");
        headerInvoiceDateCell.setCellValue("Fatura Tarihi");
        headerMaturityDateCell.setCellValue("Vade Tarihi");
        headerInvoiceNumCell.setCellValue("Fatura Numarası");
        headerCustomerCell.setCellValue("Ödeme Yapılacak Firma");
        headerSirketCell.setCellValue("Fatura Kesilen Şirket");
        headerOdemeYapanSirketCell.setCellValue("Ödeme Yapan Şirket");
        headerCostCell.setCellValue("Maliyet Yeri");
        headerPaymentSubjectCell.setCellValue("Ödeme Konusu");
        headerAmountCell.setCellValue("Toplam Tutar");
        headerPayAmountCell.setCellValue("Ödenen Tutar");
        headerNextAmountCell.setCellValue("Kalan Tutar");
        headerMoneyTypeCell.setCellValue("Faturadaki Para Birimi");
        headerIbanCell.setCellValue("Iban");
        headerDescriptionCell.setCellValue("Açıklama");
        headerStatusCell.setCellValue("Onay Durumu");
        headerFirstCell.setCellValue("1.Onay Tarihi");
        headerMuhCell.setCellValue("Muhasebe Onay Tarihi");
        headerSecondCell.setCellValue("2.Onay Tarihi");
        headerCancelDateCell.setCellValue("Reddetme Tarihi");
        headerCancelUserCell.setCellValue("Reddeden");
        headerPaymentTypeCell.setCellValue("Ödeme Şekli");
        headerPaymentStyleCell.setCellValue("Ödenecek Para Birimi");
        headerExchangeCell.setCellValue("Kur Tarihi");
        headerPayTlCell.setCellValue("Ödenen Tl Tutarı");
        headerSuccessCell.setCellValue("Ödeme Yapıldı Mı?");
        headerAutoPayCell.setCellValue("Otomatik Ödemede Mi?");
        headerDekontCell.setCellValue("Dekont Talep Edildi Mi?");
        headerKismiCell.setCellValue("Kısmi Ödeme Mi?");
        headerCreatedDateCell.setCellValue("Oluşturulma Tarihi");

        for (PaymentOrder paymentOrder : PaymentOrders) {
            columnIndex = 0;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell codeCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell assignerCell = row.createCell(columnIndex++);
            XSSFCell secondAssignerCell = row.createCell(columnIndex++);
            XSSFCell InvoiceDateCell = row.createCell(columnIndex++);  //Fatura Tarihi
            XSSFCell MaturityDateCell = row.createCell(columnIndex++);  //Vade Tarihi
            XSSFCell InvoiceNumCell = row.createCell(columnIndex++);  //Fatura Numarası
            XSSFCell CustomerCell = row.createCell(columnIndex++);  //Ödeme Yapılacak Firma
            XSSFCell SirketCell = row.createCell(columnIndex++);  //Fatura Kesilen Şirket
            XSSFCell OdemeYapanSirketCell = row.createCell(columnIndex++);  //Ödeme Yapan Şirket
            XSSFCell CostCell = row.createCell(columnIndex++);  //Maliyet Yeri
            XSSFCell PaymentSubjectCell = row.createCell(columnIndex++);  //Ödeme Konusu
            XSSFCell AmountCell = row.createCell(columnIndex++);  //Toplam Tutar
            XSSFCell PayAmountCell = row.createCell(columnIndex++);  //Ödenen Tutar
            XSSFCell NextAmountCell = row.createCell(columnIndex++);  //Kalan Tutar
            XSSFCell MoneyTypeCell = row.createCell(columnIndex++);  //Faturadaki Para Birimi
            XSSFCell IbanCell = row.createCell(columnIndex++);  //Iban
            XSSFCell DescriptionCell = row.createCell(columnIndex++);  //Açıklama
            XSSFCell StatusCell = row.createCell(columnIndex++);  //Onay Durumu
            XSSFCell FirstCell = row.createCell(columnIndex++);     //1.Onay Tarihi
            XSSFCell MuhCell = row.createCell(columnIndex++);     //Muhasebe Onay Tarihi
            XSSFCell SecondCell = row.createCell(columnIndex++);     //2.Onay Tarihi
            XSSFCell CancelDateCell = row.createCell(columnIndex++);     //Reddetme Tarihi
            XSSFCell CancelUserCell = row.createCell(columnIndex++);     //Reddeden
            XSSFCell PaymentTypeCell = row.createCell(columnIndex++);     //Ödeme Şekli
            XSSFCell PaymentStyleCell = row.createCell(columnIndex++);     //Ödenecek Para Birimi
            XSSFCell ExchangeCell = row.createCell(columnIndex++);     //Kur Tarihi
            XSSFCell PayTlCell = row.createCell(columnIndex++);         //Ödenen Tl Tutarı
            XSSFCell SuccessCell = row.createCell(columnIndex++);     //Ödeme Yapıldı Mı?
            XSSFCell AutoPayCell = row.createCell(columnIndex++);     //Otomatik Ödemede Mi?
            XSSFCell DekontCell = row.createCell(columnIndex++);     //Dekont Talep Edildi Mi?
            XSSFCell KismiCell = row.createCell(columnIndex++);     //Kısmi Ödeme Mi?
            XSSFCell CreatedDateCell = row.createCell(columnIndex++);       //Oluşturulma Tarihi

            if (paymentOrder.getStore() != null) {
                codeCell.setCellValue(paymentOrder.getStore().getStcode());
            }
            if (paymentOrder.getOwner() != null) {
                ownerCell.setCellValue(paymentOrder.getOwner().getFullName());
            }
            if (paymentOrder.getAssigner() != null) {
                assignerCell.setCellValue(paymentOrder.getAssigner().getFullName());
            }
            if (paymentOrder.getSecondAssigner() != null) {
                secondAssignerCell.setCellValue(paymentOrder.getSecondAssigner().getFullName());
            }

            if (paymentOrder.getInvoiceDate() != null) {
                InvoiceDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getInvoiceDate()));
            }
            if (paymentOrder.getMaturityDate() != null) {
                MaturityDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getMaturityDate()));
            }
            InvoiceNumCell.setCellValue(paymentOrder.getInvoiceNum());

            if (paymentOrder.getCustomer() != null) {
                CustomerCell.setCellValue(paymentOrder.getCustomer().getCommercialTitle());
            }

            if (paymentOrder.getSirket() != null) {
                SirketCell.setCellValue(paymentOrder.getSirket().getLabel());
            }
            if (paymentOrder.getOdemeYapanSirket() != null) {
                OdemeYapanSirketCell.setCellValue(paymentOrder.getOdemeYapanSirket().getLabel());
            }
            if (paymentOrder.getCost() != null) {
                CostCell.setCellValue(paymentOrder.getCost().getLabel());
            }
            if (paymentOrder.getPaymentSubject() != null) {
                PaymentSubjectCell.setCellValue(paymentOrder.getPaymentSubject().getLabel());
            }

            if (paymentOrder.getAmount() != null) {
                AmountCell.setCellValue(paymentOrder.getAmount().floatValue());
            }
            if (paymentOrder.getPayamount() != null) {
                PayAmountCell.setCellValue(paymentOrder.getPayamount().floatValue());
            }
            if (paymentOrder.getNextamount() != null) {
                NextAmountCell.setCellValue(paymentOrder.getNextamount().floatValue());
            }

            if (paymentOrder.getMoneyType() != null) {
                MoneyTypeCell.setCellValue(paymentOrder.getMoneyType().getLabel());
            }
            if (paymentOrder.getIban() == null) {
                IbanCell.setCellValue("");
            } else {
                IbanCell.setCellValue(paymentOrder.getIban().getName());
            }
            if (StringUtils.isNotBlank(paymentOrder.getDescription())) {
                DescriptionCell.setCellValue(paymentOrder.getDescription());
            }
            if (paymentOrder.getStatus() != null) {
                StatusCell.setCellValue(paymentOrder.getStatus().getLabel());
            }
            if (paymentOrder.getOkeyFirst() != null) {
                FirstCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getOkeyFirst()));
            }
            if (paymentOrder.getOkeyMuh() != null) {
                MuhCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getOkeyMuh()));
            }
            if (paymentOrder.getOkeySecond() != null) {
                SecondCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getOkeySecond()));
            }
            if (paymentOrder.getCancelDate() != null) {
                CancelDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getCancelDate()));
            }

            if (paymentOrder.getCancelUser() != null) {
                CancelUserCell.setCellValue(paymentOrder.getCancelUser().getFullName());
            }

            if (paymentOrder.getPaymentType() != null) {
                PaymentTypeCell.setCellValue(paymentOrder.getPaymentType().getLabel());
            }
            if (paymentOrder.getPaymentStyle() != null) {
                PaymentStyleCell.setCellValue(paymentOrder.getPaymentStyle().getLabel());
            }
            if (paymentOrder.getExchange() != null) {
                ExchangeCell.setCellValue(paymentOrder.getExchange().getLabel());
            }
            if (paymentOrder.getPayTl() != null) {
                PayTlCell.setCellValue(paymentOrder.getPayTl().toString());
            }
            if (paymentOrder.getSuccess() != null) {
                SuccessCell.setCellValue(paymentOrder.getSuccess().toString());
            }
            if (paymentOrder.getAutopay() != null) {
                AutoPayCell.setCellValue(paymentOrder.getAutopay().toString());
            }
            if (paymentOrder.getDekont() != null) {
                DekontCell.setCellValue(paymentOrder.getDekont().toString());
            }
            if (paymentOrder.getKismi() != null) {
                KismiCell.setCellValue(paymentOrder.getKismi().toString());
            }
            if (paymentOrder.getCreatedDate() != null) {
                CreatedDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(paymentOrder.getCreatedDate()));
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

    public byte[] generateSelectedSpendExcelReport(List<UUID> ids, User currentUser) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build()
            .page(0)
            .size(Integer.MAX_VALUE)
            .filter(
                Filter.And(
                    Filter.FilterItem("paymentorder.id", FilterItem.Operator.IN, ids)
                )
            );

        List<Spend> spends = spendRepository.findByPaymentorderIdIn(ids);

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

        XSSFCell headerStatusCell = headerRow.createCell(columnIndex++);  //Onay Durumu
        XSSFCell headerSpendStatusCell = headerRow.createCell(columnIndex++);  //Ödeme Durumu
        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);     //Talimat Oluşturan
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++);       //Oluşturulma Tarihi
        XSSFCell headerInvoiceDateCell = headerRow.createCell(columnIndex++);  //Fatura Tarihi
        XSSFCell headerInvoiceNumCell = headerRow.createCell(columnIndex++);  //Fatura Numarası
        XSSFCell headerPaymentStyleCell = headerRow.createCell(columnIndex++);     //Ödenecek Para Birimi
        XSSFCell headerSpendAmountCell = headerRow.createCell(columnIndex++);  //Ödenmesi Talep Edilen Tutar
        XSSFCell headerOdemeYapanSirketCell = headerRow.createCell(columnIndex++);  //Ödeme Yapan Şirket
        XSSFCell headerCustomerCell = headerRow.createCell(columnIndex++);  //Ödeme Yapılacak Firma
        XSSFCell headerMoneyTypeCell = headerRow.createCell(columnIndex++);  //Faturadaki Para Birimi
        XSSFCell headerAmountCell = headerRow.createCell(columnIndex++);  //Toplam Tutar
        XSSFCell headerPayAmountCell = headerRow.createCell(columnIndex++);  //Ödenen Tutar
        XSSFCell headerNextAmountCell = headerRow.createCell(columnIndex++);  //Kalan Tutar
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);  //Açıklama
        XSSFCell headerExchangeCell = headerRow.createCell(columnIndex++);     //Kur Tarihi
        XSSFCell headerPayTlCell = headerRow.createCell(columnIndex++);     //Ödenen Tl Tutarı
        XSSFCell headerAssignerCell = headerRow.createCell(columnIndex++);  //1.Onaycı
        XSSFCell headerSecondAssignerCell = headerRow.createCell(columnIndex++);  //2.Onaycı

        headerStatusCell.setCellStyle(headerCellStyle);
        headerSpendStatusCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);
        headerInvoiceDateCell.setCellStyle(headerCellStyle);
        headerInvoiceNumCell.setCellStyle(headerCellStyle);
        headerPaymentStyleCell.setCellStyle(headerCellStyle);
        headerSpendAmountCell.setCellStyle(headerCellStyle);
        headerOdemeYapanSirketCell.setCellStyle(headerCellStyle);
        headerCustomerCell.setCellStyle(headerCellStyle);
        headerMoneyTypeCell.setCellStyle(headerCellStyle);
        headerAmountCell.setCellStyle(headerCellStyle);
        headerPayAmountCell.setCellStyle(headerCellStyle);
        headerNextAmountCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);
        headerExchangeCell.setCellStyle(headerCellStyle);
        headerPayTlCell.setCellStyle(headerCellStyle);
        headerAssignerCell.setCellStyle(headerCellStyle);
        headerSecondAssignerCell.setCellStyle(headerCellStyle);


        headerStatusCell.setCellValue("Onay Durumu");
        headerSpendStatusCell.setCellValue("Ödeme Durumu");
        headerOwnerCell.setCellValue("Talimat Oluşturan");
        headerCreatedDateCell.setCellValue("Oluşturulma Tarihi");
        headerInvoiceDateCell.setCellValue("Fatura Tarihi");
        headerInvoiceNumCell.setCellValue("Fatura Numarası");
        headerPaymentStyleCell.setCellValue("Ödenecek Para Birimi");
        headerSpendAmountCell.setCellValue("Ödenmesi Talep Edilen Tutar");
        headerOdemeYapanSirketCell.setCellValue("Ödeme Yapan Şirket");
        headerCustomerCell.setCellValue("Ödeme Yapılacak Firma");
        headerMoneyTypeCell.setCellValue("Faturadaki Para Birimi");
        headerAmountCell.setCellValue("Toplam Tutar");
        headerPayAmountCell.setCellValue("Ödenen Tutar");
        headerNextAmountCell.setCellValue("Kalan Tutar");
        headerDescriptionCell.setCellValue("Açıklama");
        headerExchangeCell.setCellValue("Kur Tarihi");
        headerPayTlCell.setCellValue("Ödenen Tl Tutarı");
        headerAssignerCell.setCellValue("1.Onaycı");
        headerSecondAssignerCell.setCellValue("2.Onaycı");


        for (Spend spend : spends) {
            columnIndex = 0;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell StatusCell = row.createCell(columnIndex++);  //Onay Durumu
            XSSFCell SpendStatusCell = row.createCell(columnIndex++);  //Ödeme Durumu
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell CreatedDateCell = row.createCell(columnIndex++);       //Oluşturulma Tarihi
            XSSFCell InvoiceDateCell = row.createCell(columnIndex++);  //Fatura Tarihi
            XSSFCell InvoiceNumCell = row.createCell(columnIndex++);  //Fatura Numarası
            XSSFCell PaymentStyleCell = row.createCell(columnIndex++);     //Ödenecek Para Birimi
            XSSFCell SpendAmountCell = row.createCell(columnIndex++);     //Ödenmesi Talep Edilen Tutar
            XSSFCell OdemeYapanSirketCell = row.createCell(columnIndex++);  //Ödeme Yapan Şirket
            XSSFCell CustomerCell = row.createCell(columnIndex++);  //Ödeme Yapılacak Firma
            XSSFCell MoneyTypeCell = row.createCell(columnIndex++);  //Faturadaki Para Birimi
            XSSFCell AmountCell = row.createCell(columnIndex++);  //Toplam Tutar
            XSSFCell PayAmountCell = row.createCell(columnIndex++);  //Ödenen Tutar
            XSSFCell NextAmountCell = row.createCell(columnIndex++);  //Kalan Tutar
            XSSFCell DescriptionCell = row.createCell(columnIndex++);  //Açıklama
            XSSFCell ExchangeCell = row.createCell(columnIndex++);     //Kur Tarihi
            XSSFCell PayTlCell = row.createCell(columnIndex++);         //Ödenen Tl Tutarı
            XSSFCell assignerCell = row.createCell(columnIndex++);
            XSSFCell secondAssignerCell = row.createCell(columnIndex++);

            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            if (spend.getPaymentorder().getOwner() != null) {
                ownerCell.setCellValue(spend.getPaymentorder().getOwner().getFullName());
            }
            if (spend.getPaymentorder().getAssigner() != null) {
                assignerCell.setCellValue(spend.getPaymentorder().getAssigner().getFullName());
            }
            if (spend.getPaymentorder().getSecondAssigner() != null) {
                secondAssignerCell.setCellValue(spend.getPaymentorder().getSecondAssigner().getFullName());
            }

            if (spend.getPaymentorder().getInvoiceDate() != null) {
                InvoiceDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(spend.getPaymentorder().getInvoiceDate()));
            }
            InvoiceNumCell.setCellValue(spend.getPaymentorder().getInvoiceNum());

            if (spend.getPaymentorder().getCustomer() != null) {
                CustomerCell.setCellValue(spend.getPaymentorder().getCustomer().getCommercialTitle());
            }

            if (spend.getPaymentorder().getOdemeYapanSirket() != null) {
                OdemeYapanSirketCell.setCellValue(spend.getPaymentorder().getOdemeYapanSirket().getLabel());
            }

            if (spend.getPaymentorder().getAmount() != null) {
                AmountCell.setCellValue(decimalFormat.format(spend.getPaymentorder().getAmount()));
            }
            if (spend.getAmount() != null) {
                SpendAmountCell.setCellValue(decimalFormat.format(spend.getAmount()));
            }
            if (spend.getPaymentorder().getPayamount() != null) {
                PayAmountCell.setCellValue(decimalFormat.format(spend.getPaymentorder().getPayamount()));
            }
            if (spend.getPaymentorder().getNextamount() != null) {
                NextAmountCell.setCellValue(decimalFormat.format(spend.getPaymentorder().getNextamount()));
            }

            if (spend.getPaymentorder().getMoneyType() != null) {
                MoneyTypeCell.setCellValue(spend.getPaymentorder().getMoneyType().getLabel());
            }

            if (StringUtils.isNotBlank(spend.getPaymentorder().getDescription())) {
                DescriptionCell.setCellValue(spend.getPaymentorder().getDescription());
            }
            if (spend.getPaymentorder().getStatus() != null) {
                StatusCell.setCellValue(spend.getPaymentorder().getStatus().getLabel());
            }
            if (spend.getStatus() != null) {
                SpendStatusCell.setCellValue(spend.getStatus().getLabel());
            }

            if (spend.getPaymentorder().getPaymentStyle() != null) {
                PaymentStyleCell.setCellValue(spend.getPaymentorder().getPaymentStyle().getLabel());
            }
            if (spend.getPaymentorder().getExchange() != null) {
                ExchangeCell.setCellValue(spend.getPaymentorder().getExchange().getLabel());
            }
            if (spend.getPaymentorder().getPayTl() != null) {
                PayTlCell.setCellValue(decimalFormat.format(spend.getPaymentorder().getPayTl()));
            }

            if (spend.getPaymentorder().getCreatedDate() != null) {
                CreatedDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(spend.getPaymentorder().getCreatedDate()));
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

    public void updatePaymentOrderStatus(UUID id, AttributeValue status, String description) throws Exception{
        Instant iptalzamani = null;
        User iptaleden = null;
        User muhasebeOnaycisi = null;
        Instant muhasebeOnayi = null;
        Instant okeyFirst = null;
        Instant okeySecond = null;
        BigDecimal payAmount = BigDecimal.ZERO;
        BigDecimal nextAmount = BigDecimal.ZERO;
        String kaynak = "";
        String invoiceNum = "";
        String onayci1Unvani = "";
        String onayci2Unvani = "";
        String onayci1Teli = "";
        String onayci2Teli = "";
        String olusturan = "";
        String onay1 = "";
        String onay2 = "";
        String muhOnay = "hikmet@meteorpetrol.com";
        PaymentOrder odemeTalimati = new PaymentOrder();
        Boolean muhasebeGoruntusu = false;

        List <PaymentOrder> paymentOrder = repository.findAll();
        for (PaymentOrder paymentOrder1 : paymentOrder) {
            if (paymentOrder1.getId().equals(id)) {
                onayci1Unvani = paymentOrder1.getAssigner().getUnvan().getId();
                onayci2Unvani = paymentOrder1.getSecondAssigner().getUnvan().getId();
                onayci1Teli = paymentOrder1.getAssigner().getPhone();
                onayci2Teli = paymentOrder1.getSecondAssigner().getPhone();
                iptalzamani = paymentOrder1.getCancelDate();
                iptaleden = paymentOrder1.getCancelUser();
                muhasebeOnayi = paymentOrder1.getOkeyMuh();
                okeyFirst = paymentOrder1.getOkeyFirst();
                okeySecond = paymentOrder1.getOkeySecond();
                payAmount = paymentOrder1.getPayamount();
                nextAmount = paymentOrder1.getNextamount();
                kaynak  = paymentOrder1.getKaynak();
                invoiceNum = paymentOrder1.getInvoiceNum();
                olusturan = paymentOrder1.getOwner().getEposta();
                onay1 = paymentOrder1.getAssigner().getEposta();
                onay2 = paymentOrder1.getSecondAssigner().getEposta();
                if (paymentOrder1.getCost().getId().equals("Cost_Place_MeteorIzmir")) muhOnay = "elif.kucukkurt@loher.com.tr;selin.akbayirli@loher.com.tr";
                else if (paymentOrder1.getCost().getId().equals("Cost_Place_MeteorIgdir") || paymentOrder1.getCost().getId().equals("Cost_Place_Avelice")) muhOnay = "muharrem.alcan@aveliceasansor.com.tr";
                else muhOnay = "muhasebe@meteorpetrol.com";
                break;
            }
        }

        if (status.getId().equals("Payment_Status_Red")) {
            iptalzamani = Instant.now();
            iptaleden = getCurrentUser();
            payAmount = BigDecimal.ZERO;
            nextAmount = BigDecimal.ZERO;

            //todo: BURADA REDDEDİLEN TALİMATIN FATURA LİSTESİNDEKİ GÜNCELLEMESİ YAPILACAK...
            if (kaynak.equals("FATURA LİSTESİ")) {
                List<InvoiceList> invoiceList = invoiceListRepository.findByInvoiceNum(invoiceNum);
                List<InvoiceStatus> invoiceStatuses = Arrays.asList(InvoiceStatus.values());
                for (InvoiceStatus invoiceStatus: invoiceStatuses) {
                    if (invoiceStatus.getId().equals("Fatura_Durumlari_Atandi")) {
                        for (InvoiceList invoiceList1: invoiceList) {
                            if (invoiceList1.getInvoiceNum().equals(invoiceNum)) {
                                invoiceListRepository.updateInvoice(invoiceStatus.getAttributeValue(), invoiceList1.getId(), "Talimatı Reddedildi. " + description);
                            }
                        }
                    }
                }
            }
        } else if(status.getId().equals("Payment_Status_Bek2") && getCurrentUser().getBirim().getId().equals("Birimler_Muh")){
            muhasebeOnayi = Instant.now();  // Muhasebeci Onay Verdiyse Muhasebe Onay Zamanını Al
            muhasebeGoruntusu = false;
            muhasebeOnaycisi = getCurrentUser();
        } else if(status.getId().equals("Payment_Status_Bek2") && !getCurrentUser().getBirim().getId().equals("Birimler_Muh")){
            okeyFirst = Instant.now();      // Prim ödemesi, 1.Onaycı onay verdi ve Muhasebe onayı atlandıysa 1.Onay Zamanını Al
            muhasebeGoruntusu = false;
        } else if(status.getId().equals("Payment_Status_Onay")){
            okeySecond = Instant.now();     // 2.Onaycı onay Verdiyse 2.Onay Zamanını Al
        } else if(status.getId().equals("Payment_Status_Muh")){
            muhasebeGoruntusu = true;
            okeyFirst = Instant.now();      // 1.Onaycı onay Verdiyse 1.Onay Zamanını Al
        }
        repository.updateStatusById(status, id, iptalzamani, iptaleden, muhasebeOnayi, okeyFirst, okeySecond, payAmount, nextAmount, description, muhasebeGoruntusu, muhasebeOnaycisi);
        //todo:Mail ve sms gönderimi.
        System.out.println(olusturan + " - " +  onay1 + " - " + muhOnay + " " + onay2 + " - " + status.getId() + " - " + invoiceNum);
        mailSend(olusturan, onay1, muhOnay, onay2, status.getId(), invoiceNum, onayci1Unvani, onayci2Unvani, onayci1Teli, onayci2Teli);
    }

    public void mailSend(String olusturan, String onay1, String muhasebe, String onay2, String durum, String invoiceNum,  String onayci1Unvani, String onayci2Unvani, String onayci1Teli, String onayci2Teli) throws Exception {
       String text = invoiceNum + " fatura numarali odeme talimati, ";
       //String testmail = "hikmet@meteorpetrol.com";
       try {
           if (durum.equals("Payment_Status_Bek1")) {
               text = text + "1.Onay Bekliyor" +
                   " durumundadir ve onayiniz beklenmektedir. meteorpanel.com/paymentorder adresinden ilgili talimata ulasabilirsiniz.";
               mailService.sendEmail(onay1, "MeteorPanel - Ödeme Talimatı", text,false, false);
           } else if (durum.equals("Payment_Status_Bek2")) {
               text = text + "2.Onay Bekliyor" +
                   " durumundadir ve onayiniz beklenmektedir. meteorpanel.com/paymentorder adresinden ilgili talimata ulasabilirsiniz.";
               mailService.sendEmail(onay2, "MeteorPanel - Ödeme Talimatı", text,false, false);
           } else if (durum.equals("Payment_Status_Muh")) {
               text = text + "Muhasebe Onayı" +
                   " durumundadir ve onayiniz beklenmektedir. meteorpanel.com/paymentorder adresinden ilgili talimata ulasabilirsiniz.";
               mailService.sendEmail(muhasebe, "MeteorPanel - Ödeme Talimatı", text,false, false);
           } else if (durum.equals("Payment_Status_Onay")) {
               text = text + "ONAYLANMISTIR." +
                   " meteorpanel.com/paymentorder adresinden ilgili talimata ulasabilirsiniz.";
               mailService.sendEmail(olusturan, "MeteorPanel - Ödeme Talimatı", text,false, false);
           } else if (durum.equals("Payment_Status_Red")) {
               text = text + " REDDEDILMISTIR. meteorpanel.com/paymentorder adresinden ilgili talimata ulasabilirsiniz.";
               mailService.sendEmail(olusturan, "MeteorPanel - Ödeme Talimatı", text,false, false);
           } else {

           }

           if ((onayci1Unvani.equals("Unvanlar_Gen_Mud") || onayci1Unvani.equals("Unvanlar_Yon_Bas") ||
               onayci1Unvani.equals("Unvanlar_San_Bas") || onayci1Unvani.equals("Unvanlar_Ins_Dir")) && durum.equals("Payment_Status_Bek1")
           ) {
               postaGuverciniService.SendSmsService(onayci1Teli, text);
           }
           if ((onayci2Unvani.equals("Unvanlar_Gen_Mud") || onayci2Unvani.equals("Unvanlar_Yon_Bas") ||
               onayci2Unvani.equals("Unvanlar_San_Bas") || onayci2Unvani.equals("Unvanlar_Ins_Dir")) && durum.equals("Payment_Status_Bek2")
           ) {
               postaGuverciniService.SendSmsService(onayci2Teli, text);
           }
       } catch (Exception e) {
           throw new Exception(e.getMessage());
       }

    }

    public PaymentOrder updateStoreId(UUID id, UUID storeid) throws Exception {
        try {
            Optional<Store> optionalStore = storeRepository.findById(storeid);
            if (optionalStore.isPresent()) {
                Store store1 = optionalStore.get();
                repository.updateStoreId(id, store1);
            }
        } catch (Exception e) {
            // Hata durumunda uygun bir işlem yapabilirsiniz
        }
        return null;
    }
    /*public void updateStatus(PaymentOrderDTO paymentOrderDTO) throws Exception {
        //User currentUser = getCurrentUser();
        Optional<PaymentOrder> paymentOrder = repository.findById(paymentOrderDTO.getPaymentId());

        if (!paymentOrder.isPresent()) {
            throw new RecordNotFoundException(entityClass.getSimpleName(), paymentOrderDTO);
        }

        if (!((currentUser.getId()) == (activity.get().getOwner().getId()))) {
            throw new BadRequestAlertException("Bu aktivite sze ait değil.", null, "idexists");
        }

        paymentOrder.get().setStatus(paymentOrderDTO.getStatus());
        paymentOrder.get().setOkeyFirst(paymentOrderDTO.getOkeyFirst());

        add(getCurrentUser(), paymentOrder.get());
    }*/
    public List<PaymentOrder> myCorrect () {
        List<PaymentOrder> paymentOrders = repository.findAll();
        List<PaymentOrder> returnList = new ArrayList<>();
        for (PaymentOrder paymentOrder : paymentOrders) {
            if (paymentOrder.getStatus().getId().equals(PaymentStatus.ONAY1.getId()) && paymentOrder.getAssigner().equals(getCurrentUser()) ||
                paymentOrder.getStatus().getId().equals(PaymentStatus.ONAY2.getId()) && paymentOrder.getSecondAssigner().equals(getCurrentUser())) {
                returnList.add(paymentOrder);
            }
        }
        return returnList;
    }
    /*public List<PaymentOrderDTO> mobileAllList () {
        List<PaymentOrder> paymentOrders = repository.findAll();
        List<PaymentOrderDTO> returnList = new ArrayList<>();
        PaymentOrderDTO dto = new PaymentOrderDTO();

        for (PaymentOrder paymentOrder : paymentOrders) {
            if (paymentOrder.getSirket() != null) {
                dto.setCompany(paymentOrder.getSirket().getLabel());
            } else {
                dto.setCompany("");
            }
            if (paymentOrder.getCustomer() != null) {
                dto.setCustomer(paymentOrder.getCustomer().getName());
            } else {
                dto.setCustomer("");
            }
            if (paymentOrder.getAmount() != null) {
                dto.setAmount(paymentOrder.getAmount().toString());
            } else {
                dto.setAmount("");
            }
            if (paymentOrder.getOkeyFirst() != null) {
                dto.setOkeyFirst(paymentOrder.getOkeyFirst().toString());
            } else {
                dto.setOkeyFirst("");
            }
            if (paymentOrder.getOkeySecond() != null) {
                dto.setOkeySecond(paymentOrder.getOkeySecond().toString());
            } else {
                dto.setOkeySecond("");
            }
            if (paymentOrder.getStatus() != null) {
                dto.setStatus(paymentOrder.getStatus().getLabel());
            } else {
                dto.setStatus("");
            }
            returnList.add(dto);
        }
        return returnList;
    }*/

    /*public PaymentOrderDTO mobileAllList2 () {
        List<PaymentOrder> paymentOrders = repository.findAll();
        List<PaymentOrderDTO> returnList = new ArrayList<>();
        PaymentOrderDTO dto = new PaymentOrderDTO();

        for (PaymentOrder paymentOrder : paymentOrders) {
            if (paymentOrder.getSirket() != null) {
                dto.setCompany(paymentOrder.getSirket().getLabel());
            } else {
                dto.setCompany("");
            }
            if (paymentOrder.getCustomer() != null) {
                dto.setCustomer(paymentOrder.getCustomer().getName());
            } else {
                dto.setCustomer("");
            }
            if (paymentOrder.getAmount() != null) {
                dto.setAmount(paymentOrder.getAmount().toString());
            } else {
                dto.setAmount("");
            }
            if (paymentOrder.getOkeyFirst() != null) {
                dto.setOkeyFirst(paymentOrder.getOkeyFirst().toString());
            } else {
                dto.setOkeyFirst("");
            }
            if (paymentOrder.getOkeySecond() != null) {
                dto.setOkeySecond(paymentOrder.getOkeySecond().toString());
            } else {
                dto.setOkeySecond("");
            }
            if (paymentOrder.getStatus() != null) {
                dto.setStatus(paymentOrder.getStatus().getLabel());
            } else {
                dto.setStatus("");
            }
            returnList.add(dto);
            for (PaymentOrderDTO returnLi : returnList) {
                System.out.println(returnLi.getCustomer());
                System.out.println(returnLi.getAmount());
                System.out.println(returnLi.getCompany());
                System.out.println(returnLi.getOkeyFirst());
                System.out.println(returnLi.getOkeySecond());
                System.out.println(returnLi.getStatus());
            }
        }
        return returnList;
    }*/
}
