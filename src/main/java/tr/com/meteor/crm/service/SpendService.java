package tr.com.meteor.crm.service;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.utils.attributevalues.PaymentStatus;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class SpendService extends GenericIdNameAuditingEntityService<Spend, UUID, SpendRepository> {

    private final PaymentOrderRepository paymentOrderRepository;

    private final PaymentOrderService paymentOrderService;
    private final MailService mailService;

    public SpendService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                        BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                        BaseConfigurationService baseConfigurationService, SpendRepository repository,
                        PaymentOrderRepository paymentOrderRepository, PaymentOrderService paymentOrderService, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Spend.class, repository);
        this.paymentOrderRepository = paymentOrderRepository;
        this.paymentOrderService = paymentOrderService;
        this.mailService = mailService;
    }

    public byte[] generateExcelSpendReportForUser(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
            )
        );

        List<Spend> activities = getData(null, request, false).getBody();
        Map<Long, List<Spend>> userIdActivities = new HashMap<>();

        for (Spend activity : activities) {
            List<Spend> userActivities = new ArrayList<>();

            if (userIdActivities.containsKey(activity.getOwner().getId())) {
                userActivities = userIdActivities.get(activity.getOwner().getId());
            }

            userActivities.add(activity);

            userIdActivities.put(activity.getOwner().getId(), userActivities);
        }

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
        headerCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 0}, null));
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCell supplierHeaderCell = headerRow.createCell(columnIndex++);  //Tedarikçi
        XSSFCell taskHeaderCell = headerRow.createCell(columnIndex++);  //Görev
        XSSFCell usernameHeaderCell = headerRow.createCell(columnIndex++);  //İşlem Yapan
        XSSFCell taskSubjectHeaderCell = headerRow.createCell(columnIndex++);  //Konu
        XSSFCell taskSubjdescHeaderCell = headerRow.createCell(columnIndex++);  //Konu Başlığı
        XSSFCell activityStatusHeaderCell = headerRow.createCell(columnIndex++);    //Durum
        XSSFCell planningHeaderCell = headerRow.createCell(columnIndex++);   //Planlanan Tarih
        XSSFCell creatorHeaderCell = headerRow.createCell(columnIndex++);  //Oluşturan
        XSSFCell creatorTimeHeaderCell = headerRow.createCell(columnIndex++);      //Oluşturma Tarihi
        XSSFCell descriptionHeaderCell = headerRow.createCell(columnIndex++); //Açıklama

        supplierHeaderCell.setCellStyle(headerCellStyle);
        supplierHeaderCell.setCellValue("Tedarikçi");
        taskHeaderCell.setCellStyle(headerCellStyle);
        taskHeaderCell.setCellValue("Görev ID");
        usernameHeaderCell.setCellStyle(headerCellStyle);
        usernameHeaderCell.setCellValue("İşlem Yapan");
        taskSubjectHeaderCell.setCellStyle(headerCellStyle);
        taskSubjectHeaderCell.setCellValue("Konu");
        taskSubjdescHeaderCell.setCellStyle(headerCellStyle);
        taskSubjdescHeaderCell.setCellValue("Konu Başlığı");
        activityStatusHeaderCell.setCellStyle(headerCellStyle);
        activityStatusHeaderCell.setCellValue("Durum");
        planningHeaderCell.setCellStyle(headerCellStyle);
        planningHeaderCell.setCellValue("Planlanan Tarih");
        creatorHeaderCell.setCellStyle(headerCellStyle);
        creatorHeaderCell.setCellValue("Oluşturan");
        creatorTimeHeaderCell.setCellStyle(headerCellStyle);
        creatorTimeHeaderCell.setCellValue("Oluşturma Zamanı");
        descriptionHeaderCell.setCellStyle(headerCellStyle);
        descriptionHeaderCell.setCellValue("Açıklama");

        for (User user : hierarchicalUsers) {
            if (!userIdActivities.containsKey(user.getId())) continue;
            List<Spend> sortedUserActivities = userIdActivities.get(user.getId()).stream()
                .sorted(Comparator.comparing(Spend::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
            for (Spend activity : sortedUserActivities) {
                XSSFRow row = sheet.createRow(rowIndex++);
                columnIndex = 1;

                XSSFCell supplierCell = row.createCell(columnIndex++);  //Tedarikçi
                XSSFCell taskIdCell = row.createCell(columnIndex++);  //Task ID
                XSSFCell usernameCell = row.createCell(columnIndex++);  //İşlem Yapan
                XSSFCell taskSubjectCell = row.createCell(columnIndex++);  //Konu
                XSSFCell taskSubjdescCell = row.createCell(columnIndex++);  //Konu Başlığı
                XSSFCell activityStatusCell = row.createCell(columnIndex++);    //Durum
                XSSFCell planningCell = row.createCell(columnIndex++);   //Planlanan Tarih
                XSSFCell creatorCell = row.createCell(columnIndex++);  //Oluşturan
                XSSFCell creatorTimeCell = row.createCell(columnIndex++);      //Oluşturma Tarihi
                XSSFCell descriptionCell = row.createCell(columnIndex++); //Açıklama

                if (activity.getStatus() != null) {
                    activityStatusCell.setCellValue(activity.getStatus().getLabel());
                }

                if (activity.getCreatedBy() != null) {
                    creatorCell.setCellValue(activity.getCreatedBy().getFullName());
                }

                if (activity.getCreatedDate() != null) {
                    creatorTimeCell.setCellValue(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()).format(activity.getCreatedDate()));
                }

                if (activity.getDescription() != null) {
                    descriptionCell.setCellValue(activity.getDescription());
                }

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
    public byte[] generateSelectedExcelSpendReport(List<UUID> ids, User currentUser, String type, String qualification, String description) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("id", FilterItem.Operator.IN, ids)
                /*Filter.Or(
                    Filter.FilterItem("finance.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )*/
            )
        );

        List<Spend> spendList = getData(null, request, false).getBody();

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

        XSSFCell headerCodeCell = headerRow.createCell(columnIndex++);     //Sıra No
        XSSFCell headerTypeCell = headerRow.createCell(columnIndex++);     //Ödeme Tipi
        XSSFCell headerQualificationCell = headerRow.createCell(columnIndex++);  //Ödeme Niteliği
        XSSFCell headerIbanCell = headerRow.createCell(columnIndex++);  //IBAN
        XSSFCell headerCustomerCell = headerRow.createCell(columnIndex++);  //ADI/UNVANI
        XSSFCell headerLastNameCell = headerRow.createCell(columnIndex++);  //SOYADI/UNVANI
        XSSFCell headerTcknCell = headerRow.createCell(columnIndex++);  //TCKN/VKN
        XSSFCell headerAmountCell = headerRow.createCell(columnIndex++);  //TUTAR
        XSSFCell headerDescriptionCell = headerRow.createCell(columnIndex++);  //AÇIKLAMA

        headerCodeCell.setCellStyle(headerCellStyle);
        headerTypeCell.setCellStyle(headerCellStyle);
        headerQualificationCell.setCellStyle(headerCellStyle);
        headerIbanCell.setCellStyle(headerCellStyle);
        headerCustomerCell.setCellStyle(headerCellStyle);
        headerLastNameCell.setCellStyle(headerCellStyle);
        headerTcknCell.setCellStyle(headerCellStyle);
        headerAmountCell.setCellStyle(headerCellStyle);
        headerDescriptionCell.setCellStyle(headerCellStyle);

        headerCodeCell.setCellValue("Sıra No");
        headerTypeCell.setCellValue("Ödeme Tipi");
        headerQualificationCell.setCellValue("Ödeme Niteliği");
        headerIbanCell.setCellValue("IBAN");
        headerCustomerCell.setCellValue("ADI/UNVANI");
        headerLastNameCell.setCellValue("SOYADI/UNVANI");
        headerTcknCell.setCellValue("TCKN/VKN");
        headerAmountCell.setCellValue("TUTAR");
        headerDescriptionCell.setCellValue("AÇIKLAMA");


        for (Spend spend : spendList) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell codeCell = row.createCell(columnIndex++);
            XSSFCell TypeCell = row.createCell(columnIndex++);     //Ödeme Tipi
            XSSFCell QualificationCell = row.createCell(columnIndex++);  //Ödeme Niteliği
            XSSFCell IbanCell = row.createCell(columnIndex++);  //IBAN
            XSSFCell CustomerCell = row.createCell(columnIndex++);  //ADI/UNVANI
            XSSFCell LastNameCell = row.createCell(columnIndex++);  //SOYADI/UNVANI
            XSSFCell TcknCell = row.createCell(columnIndex++);  //TCKN/VKN
            XSSFCell AmountCell = row.createCell(columnIndex++);  //TUTAR
            XSSFCell DescriptionCell = row.createCell(columnIndex++);  //AÇIKLAMA

            codeCell.setCellValue(rowIndex-2);
            TypeCell.setCellValue(type);
            QualificationCell.setCellValue(qualification);
            if (spend.getPaymentorder().getIban().getName() != null) {
                IbanCell.setCellValue(spend.getPaymentorder().getIban().getName());
            }
            if (spend.getPaymentorder().getCustomer() != null) {
                CustomerCell.setCellValue(spend.getPaymentorder().getCustomer().getCommercialTitle());
            }
            LastNameCell.setCellValue("");
            TcknCell.setCellValue("");
            if (spend.getAmount() != null) {
                /*DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
                String formattedAmount = decimalFormat.format(paymentOrder.getAmount());*/
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Dl") && spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                    AmountCell.setCellValue(spend.getPayTl().floatValue());
                } else {
                    AmountCell.setCellValue(spend.getAmount().floatValue());
                }
            }
            DescriptionCell.setCellValue(description);
        }

        for (int i = 0; i < 20; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
    public void updateSpendStatus(UUID id, AttributeValue status, String description) throws Exception{
        Instant okeyFirst = null;
        Boolean lock = false;
        UUID paymentID = null;
        BigDecimal payMoney = BigDecimal.ZERO;
        BigDecimal toplam = BigDecimal.ZERO;
        BigDecimal odenen = BigDecimal.ZERO;
        BigDecimal kalan = BigDecimal.ZERO;
        BigDecimal odenecek = BigDecimal.ZERO;
        AttributeValue paymentStatus = null;
        String spendPaymentStatus = null;
        String odemeNo = "";


        List <Spend> spendList = repository.findAll();
        for (Spend spend : spendList) {
            if (spend.getId().equals(id)) {
                okeyFirst = spend.getSpendDate();
                lock = spend.getLock();
                paymentID = spend.getPaymentorder().getId();
                odenecek = spend.getAmount();
                payMoney = spend.getPayTl();
                odemeNo = spend.getPaymentNum();
                break;
            }
        }
        List <PaymentOrder> paymentOrders = paymentOrderRepository.findAll();
        for (PaymentOrder paymentOrder : paymentOrders) {
            if (paymentOrder.getId().equals(paymentID)) {
                odenen = paymentOrder.getPayamount();
                kalan = paymentOrder.getNextamount();
                paymentStatus = paymentOrder.getStatus();
                spendPaymentStatus = paymentStatus.getLabel();
                break;
            }
        }

        if (status.getId().equals("Spend_Status_Yes")) {
            lock = true;
            okeyFirst = Instant.now();

            List <PaymentOrder> paymentOrders1 = paymentOrderRepository.findAll();
            for (PaymentOrder paymentOrder : paymentOrders1) {
                if (paymentOrder.getId().equals(paymentID)) {
                    odenen = paymentOrder.getPayamount().add(odenecek);
                    kalan = paymentOrder.getNextamount().subtract(odenecek);
                    if (kalan.compareTo(BigDecimal.ZERO)>0) {
                        paymentStatus = PaymentStatus.KISMI.getAttributeValue();
                        spendPaymentStatus = "Kısmi Ödendi";
                    } else {
                        paymentStatus = PaymentStatus.ODENDI.getAttributeValue();
                        spendPaymentStatus = "Ödendi";
                    }
                    break;
                }
            }
            paymentOrderRepository.updateValuesById(paymentStatus, paymentID, odenen, kalan);
            repository.updateStatusById(status, id, okeyFirst, lock, spendPaymentStatus, getCurrentUser());
        }
        if (status.getId().equals("Spend_Status_Red")) {
            lock = true;
            okeyFirst = Instant.now();


            updateSpendPay(id, "0");
            if (odemeNo.equals("Tek Ödeme")) {
                List<PaymentStatus> paymentStatuses = Arrays.asList(PaymentStatus.values());
                for (PaymentStatus paymentStatus2: paymentStatuses) {
                    if (paymentStatus2.getId().equals("Payment_Status_Red")) {
                        paymentOrderService.updatePaymentOrderStatus(paymentID,paymentStatus2.getAttributeValue(),"Talimat Reddedildi...");
                    }
                }

            } else {
                paymentOrderRepository.updateValuesById(paymentStatus, paymentID, odenen, kalan);
            }

            repository.updateCancelStatusById(status, id, okeyFirst, lock, spendPaymentStatus, getCurrentUser(), description);
        }
        List <Spend> spendList2 = repository.findAll();
        for (Spend spend : spendList2) {
            if (spend.getPaymentorder().getId().equals(paymentID)) {
                repository.updatePaymentStatus(spendPaymentStatus, spend.getId());
            }
        }
    }

    public void updateSpendPaymentStatus(UUID id, String status) throws Exception{
        List <Spend> spendList = repository.findAll();
        for (Spend spend : spendList) {
            if (spend.getPaymentorder().getId().equals(id)) {
                repository.updatePaymentStatus(status, spend.getId());
            }
        }
    }

    public void updateSpendPay(UUID id, String money) throws Exception{
        BigDecimal paymentOrderPay = BigDecimal.ZERO;
        BigDecimal payMoney = BigDecimal.ZERO;
        BigDecimal exchangeMoney = BigDecimal.ZERO;
        exchangeMoney = new BigDecimal(money);

        UUID paymentOrderId = id;
        List <Spend> spendList = repository.findAll();
        for (Spend spend : spendList) {
            if (spend.getId().equals(id)) {
                payMoney = spend.getAmount().multiply(exchangeMoney);
                repository.updatePay(exchangeMoney, payMoney, id);
                paymentOrderId = spend.getPaymentorder().getId();
                break;
            }
        }
        List <Spend> spendList2 = repository.findAll();
        for (Spend spend2 : spendList2) {
            if (spend2.getPaymentorder().getId().equals(paymentOrderId) && !spend2.getStatus().getId().equals("Spend_Status_Red")) {
                if (spend2.getId().equals(id)) {
                    paymentOrderPay = paymentOrderPay.add(payMoney);
                } else {
                    paymentOrderPay = paymentOrderPay.add(spend2.getPayTl());
                }
            }
        }
        paymentOrderRepository.updatePay(paymentOrderPay, paymentOrderId);
    }
}
