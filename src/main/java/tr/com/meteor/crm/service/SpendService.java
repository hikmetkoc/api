package tr.com.meteor.crm.service;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.utils.attributevalues.FaturaSirketleri;
import tr.com.meteor.crm.utils.attributevalues.PaymentStatus;
import tr.com.meteor.crm.utils.attributevalues.TaskType;
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


    public byte[] excelReport(User currentUser) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.Or(
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("finance.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<Spend> spends = getData(null, request, false).getBody();

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

        XSSFCell odemeYapanSirket = headerRow.createCell(columnIndex++);     //Ödeme Yapan Şirket
        XSSFCell odemeYapilacakSirket = headerRow.createCell(columnIndex++);     //Ödeme Yapılacak Şirket
        XSSFCell odemeYapilanParaBirimi = headerRow.createCell(columnIndex++);  //Ödeme Yapılan Para Birimi
        XSSFCell odenecekTutar = headerRow.createCell(columnIndex++);  // Ödenecek Tutar
        XSSFCell odenenTlTutari = headerRow.createCell(columnIndex++);  //Ödenen TL Tutarı (Fatura Dolar ise)
        XSSFCell kurTutari = headerRow.createCell(columnIndex++);  //Kur Tutarı
        XSSFCell odemeTipi = headerRow.createCell(columnIndex++);  //Ödeme Tipi
        XSSFCell vadeTarihi = headerRow.createCell(columnIndex++);  //Vade Tarihi
        XSSFCell islemYapan = headerRow.createCell(columnIndex++);  //İşlem Yapan
        XSSFCell islemTarihi = headerRow.createCell(columnIndex++);  //İşlem Tarihi
        XSSFCell odemeDurumu = headerRow.createCell(columnIndex++);  //Ödeme Durumu
        XSSFCell talimatOnayDurumu = headerRow.createCell(columnIndex++);  //Talimat Onay Durumu
        XSSFCell odemeSirasi = headerRow.createCell(columnIndex++);  //Ödeme Sırası
        XSSFCell olusturan = headerRow.createCell(columnIndex++);  //Oluşturan
        XSSFCell aciklama = headerRow.createCell(columnIndex++);  //Açıklama
        XSSFCell faturadakiParaBirimi = headerRow.createCell(columnIndex++);  //Faturadaki Para Birimi
        XSSFCell faturadakiToplamTutar = headerRow.createCell(columnIndex++);  //Faturadaki Toplam Tutar
        XSSFCell toplamOdenenTutar = headerRow.createCell(columnIndex++);  //Toplam Ödenen Tutar
        XSSFCell kalanTutar = headerRow.createCell(columnIndex++);  //Kalan Tutar

        odemeYapanSirket.setCellStyle(headerCellStyle);
        odemeYapilacakSirket.setCellStyle(headerCellStyle);
        odemeYapilanParaBirimi.setCellStyle(headerCellStyle);
        odenecekTutar.setCellStyle(headerCellStyle);
        odenenTlTutari.setCellStyle(headerCellStyle);
        kurTutari.setCellStyle(headerCellStyle);
        odemeTipi.setCellStyle(headerCellStyle);
        vadeTarihi.setCellStyle(headerCellStyle);
        islemYapan.setCellStyle(headerCellStyle);
        islemTarihi.setCellStyle(headerCellStyle);
        odemeDurumu.setCellStyle(headerCellStyle);
        talimatOnayDurumu.setCellStyle(headerCellStyle);
        odemeSirasi.setCellStyle(headerCellStyle);
        olusturan.setCellStyle(headerCellStyle);
        aciklama.setCellStyle(headerCellStyle);
        faturadakiParaBirimi.setCellStyle(headerCellStyle);
        faturadakiToplamTutar.setCellStyle(headerCellStyle);
        toplamOdenenTutar.setCellStyle(headerCellStyle);
        kalanTutar.setCellStyle(headerCellStyle);

        odemeYapanSirket.setCellValue("Ödeme Yapan Şirket");
        odemeYapilacakSirket.setCellValue("Ödeme Yapılacak Şirket");
        odemeYapilanParaBirimi.setCellValue("Ödeme Yapılan Para Birimi");
        odenecekTutar.setCellValue("Ödenecek Tutar");
        odenenTlTutari.setCellValue("Ödenen TL Tutarı(Ödeme $ ise)");
        kurTutari.setCellValue("Kur Tutarı");
        odemeTipi.setCellValue("Ödeme Tipi");
        vadeTarihi.setCellValue("Vade Tarihi");
        islemYapan.setCellValue("İşlem Yapan");
        islemTarihi.setCellValue("İşlem Tarihi");
        odemeDurumu.setCellValue("Ödeme Durumu");
        talimatOnayDurumu.setCellValue("Talimat Onay Durumu");
        odemeSirasi.setCellValue("Ödeme Sırası");
        olusturan.setCellValue("Oluşturan");
        aciklama.setCellValue("Açıklama");
        faturadakiParaBirimi.setCellValue("Faturadaki Para Birimi");
        faturadakiToplamTutar.setCellValue("Faturadaki Toplam Tutar");
        toplamOdenenTutar.setCellValue("Toplam Ödenen Tutar");
        kalanTutar.setCellValue("Kalan Tutar");


        for (Spend spend : spends) {
            columnIndex = 0;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell odemeYapanSirketRow = row.createCell(columnIndex++);     //Ödeme Yapan Şirket
            XSSFCell odemeYapilacakSirketRow = row.createCell(columnIndex++);     //Ödeme Yapılacak Şirket
            XSSFCell odemeYapilanParaBirimiRow = row.createCell(columnIndex++);  //Ödeme Yapılan Para Birimi
            XSSFCell odenecekTutarRow = row.createCell(columnIndex++);  // Ödenecek Tutar
            XSSFCell odenenTlTutariRow = row.createCell(columnIndex++);  //Ödenen TL Tutarı (Fatura Dolar ise)
            XSSFCell kurTutariRow = row.createCell(columnIndex++);  //Kur Tutarı
            XSSFCell odemeTipiRow = row.createCell(columnIndex++);  //Ödeme Tipi
            XSSFCell vadeTarihiRow = row.createCell(columnIndex++);  //Vade Tarihi
            XSSFCell islemYapanRow = row.createCell(columnIndex++);  //İşlem Yapan
            XSSFCell islemTarihiRow = row.createCell(columnIndex++);  //İşlem Tarihi
            XSSFCell odemeDurumuRow = row.createCell(columnIndex++);  //Ödeme Durumu
            XSSFCell talimatOnayDurumuRow = row.createCell(columnIndex++);  //Talimat Onay Durumu
            XSSFCell odemeSirasiRow = row.createCell(columnIndex++);  //Ödeme Sırası
            XSSFCell olusturanRow = row.createCell(columnIndex++);  //Oluşturan
            XSSFCell aciklamaRow = row.createCell(columnIndex++);  //Açıklama
            XSSFCell faturadakiParaBirimiRow = row.createCell(columnIndex++);  //Faturadaki Para Birimi
            XSSFCell faturadakiToplamTutarRow = row.createCell(columnIndex++);  //Faturadaki Toplam Tutar
            XSSFCell toplamOdenenTutarRow = row.createCell(columnIndex++);  //Toplam Ödenen Tutar
            XSSFCell kalanTutarRow = row.createCell(columnIndex++);  //Kalan Tutar



            if (spend.getPaymentorder().getOdemeYapanSirket() != null) {
                odemeYapanSirketRow.setCellValue(spend.getPaymentorder().getOdemeYapanSirket().getLabel());
            }
            if (spend.getPaymentorder().getCustomer() != null) {
                odemeYapilacakSirketRow.setCellValue(spend.getPaymentorder().getCustomer().getName());
            }
            if (spend.getPaymentorder().getPaymentStyle() != null) {
                odemeYapilanParaBirimiRow.setCellValue(spend.getPaymentorder().getPaymentStyle().getLabel());
            }
            if (spend.getAmount() != null) {
                odenecekTutarRow.setCellValue(spend.getAmount().floatValue());
            }
            if (spend.getPayTl() != null) {
                odenenTlTutariRow.setCellValue(spend.getPayTl().floatValue());
            }
            if (spend.getExchangeMoney() != null) {
                kurTutariRow.setCellValue(spend.getExchangeMoney().floatValue());
            }
            if (spend.getPaymentorder().getPaymentType() != null) {
                odemeTipiRow.setCellValue(spend.getPaymentorder().getMoneyType().getLabel());
            }
            if (spend.getMaturityDate() != null) {
                vadeTarihiRow.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(spend.getMaturityDate()));
            }
            if (spend.getOwner() != null) {
                islemYapanRow.setCellValue(spend.getOwner().getFullName());
            }
            if (spend.getSpendDate() != null) {
                islemTarihiRow.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(spend.getSpendDate()));
            }
            if (spend.getStatus() != null) {
                odemeDurumuRow.setCellValue(spend.getStatus().getLabel());
            }
            if (spend.getPaymentStatus() != null) {
                talimatOnayDurumuRow.setCellValue(spend.getPaymentStatus());
            }
            if (spend.getPaymentNum() != null) {
                odemeSirasiRow.setCellValue(spend.getPaymentNum());
            }
            if (spend.getCreatedBy() != null) {
                olusturanRow.setCellValue(spend.getCreatedBy().getFullName());
            }
            if (spend.getDescription() != null) {
                aciklamaRow.setCellValue(spend.getDescription());
            }
            if (spend.getPaymentorder().getMoneyType() != null) {
                faturadakiParaBirimiRow.setCellValue(spend.getPaymentorder().getMoneyType().getLabel());
            }
            if (spend.getPaymentorder().getAmount() != null) {
                faturadakiToplamTutarRow.setCellValue(spend.getPaymentorder().getAmount().floatValue());
            }
            if (spend.getPaymentorder().getPayamount() != null) {
                toplamOdenenTutarRow.setCellValue(spend.getPaymentorder().getPayamount().floatValue());
            }
            if (spend.getPaymentorder().getNextamount() != null) {
                kalanTutarRow.setCellValue(spend.getPaymentorder().getNextamount().floatValue());
            }
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

    public Spend uploadPDF(UUID id, String base64file) throws Exception {
        System.out.println(id.toString() + " ID SI ALINDI");
        try {
            List<Spend> spendList = repository.findAll();
            for (Spend spend : spendList) {
                if (spend.getId().equals(id)) {
                    spend.setDekont(base64file);
                    return repository.save(spend);
                }
            }
        } catch (Exception e) {
            System.out.println("HATA MESAJI: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("HATA!");
        }
        // Eğer id ile eşleşen bir PaymentOrder bulunamazsa burada null döndürmelisiniz.
        return null;
    }

    public String getShowDekont(UUID id) throws Exception {
        List<Spend> spendList = repository.findAll();
        String veri = "";
        for (Spend spend: spendList) {
            if (spend.getId().equals(id)) {
                veri = spend.getDekont();
                break;
            }
        }
        return veri;
    }

    public String controlTotal() throws Exception {
        BigDecimal mpetroltoplamtl = BigDecimal.ZERO;
        BigDecimal mpetroltoplamdl = BigDecimal.ZERO;
        BigDecimal terminaltoplamtl = BigDecimal.ZERO;
        BigDecimal terminaltoplamdl = BigDecimal.ZERO;
        BigDecimal insaattoplamtl = BigDecimal.ZERO;
        BigDecimal insaattoplamdl = BigDecimal.ZERO;
        BigDecimal cemcantoplamtl = BigDecimal.ZERO;
        BigDecimal cemcantoplamdl = BigDecimal.ZERO;
        BigDecimal ncctoplamtl = BigDecimal.ZERO;
        BigDecimal ncctoplamdl = BigDecimal.ZERO;
        BigDecimal izmirtoplamtl = BigDecimal.ZERO;
        BigDecimal izmirtoplamdl = BigDecimal.ZERO;
        BigDecimal igdirtoplamtl = BigDecimal.ZERO;
        BigDecimal igdirtoplamdl = BigDecimal.ZERO;
        BigDecimal simyatoplamtl = BigDecimal.ZERO;
        BigDecimal simyatoplamdl = BigDecimal.ZERO;
        BigDecimal bircetoplamtl = BigDecimal.ZERO;
        BigDecimal bircetoplamdl = BigDecimal.ZERO;
        BigDecimal mudanyatoplamtl = BigDecimal.ZERO;
        BigDecimal mudanyatoplamdl = BigDecimal.ZERO;
        BigDecimal startoplamtl = BigDecimal.ZERO;
        BigDecimal startoplamdl = BigDecimal.ZERO;
        BigDecimal chargetoplamtl = BigDecimal.ZERO;
        BigDecimal chargetoplamdl = BigDecimal.ZERO;
        BigDecimal avelicetoplamtl = BigDecimal.ZERO;
        BigDecimal avelicetoplamdl = BigDecimal.ZERO;
        List<Spend> spendList = repository.findAll();

        for (Spend spend : spendList) {
            if (spend.getStatus().getLabel().equals("Ödendi")) continue;
            if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.METEOR.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    mpetroltoplamtl = mpetroltoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        mpetroltoplamtl = mpetroltoplamtl.add(spend.getPayTl());
                    } else {
                        mpetroltoplamdl = mpetroltoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.TERMINAL.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    terminaltoplamtl = terminaltoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        terminaltoplamtl = terminaltoplamtl.add(spend.getPayTl());
                    } else {
                        terminaltoplamdl = terminaltoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.INSAAT.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    insaattoplamtl = insaattoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        insaattoplamtl = insaattoplamtl.add(spend.getPayTl());
                    } else {
                        insaattoplamdl = insaattoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.CEMCAN.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    cemcantoplamtl = cemcantoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        cemcantoplamtl = cemcantoplamtl.add(spend.getPayTl());
                    } else {
                        cemcantoplamdl = cemcantoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.NCC.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    ncctoplamtl = ncctoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        ncctoplamtl = ncctoplamtl.add(spend.getPayTl());
                    } else {
                        ncctoplamdl = ncctoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.IZMIR.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    izmirtoplamtl = izmirtoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        izmirtoplamtl = izmirtoplamtl.add(spend.getPayTl());
                    } else {
                        izmirtoplamdl = izmirtoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.IGDIR.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    igdirtoplamtl = igdirtoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        igdirtoplamtl = igdirtoplamtl.add(spend.getPayTl());
                    } else {
                        igdirtoplamdl = igdirtoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.SIMYA.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    simyatoplamtl = simyatoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        simyatoplamtl = simyatoplamtl.add(spend.getPayTl());
                    } else {
                        simyatoplamdl = simyatoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.BIRCE.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    bircetoplamtl = bircetoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        bircetoplamtl = bircetoplamtl.add(spend.getPayTl());
                    } else {
                        bircetoplamdl = bircetoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.MUDANYA.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    mudanyatoplamtl = mudanyatoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        mudanyatoplamtl = mudanyatoplamtl.add(spend.getPayTl());
                    } else {
                        mudanyatoplamdl = mudanyatoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.STAR.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    startoplamtl = startoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        startoplamtl = startoplamtl.add(spend.getPayTl());
                    } else {
                        startoplamdl = startoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.CHARGE.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    chargetoplamtl = chargetoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        chargetoplamtl = chargetoplamtl.add(spend.getPayTl());
                    } else {
                        chargetoplamdl = chargetoplamdl.add(spend.getAmount());
                    }
                }
            } else if (spend.getPaymentorder().getOdemeYapanSirket().getId().equals(FaturaSirketleri.AVELICE.getId())) {
                if (spend.getPaymentorder().getMoneyType().getId().equals("Par_Bir_Tl")) {
                    avelicetoplamtl = avelicetoplamtl.add(spend.getAmount());
                } else {
                    if (spend.getPaymentorder().getPaymentStyle().getId().equals("Payment_Style_Tl")) {
                        avelicetoplamtl = avelicetoplamtl.add(spend.getPayTl());
                    } else {
                        avelicetoplamdl = avelicetoplamdl.add(spend.getAmount());
                    }
                }
            }

        }
        String meteorpetrol = mpetroltoplamtl + "-" + mpetroltoplamdl;
        String terminal = terminaltoplamtl + "-" + terminaltoplamdl;
        String meteorinsaat = insaattoplamtl + "-" + insaattoplamdl;
        String cemcanpetrol = cemcantoplamtl + "-" + cemcantoplamdl;
        String nccpetrol = ncctoplamtl + "-" + ncctoplamdl;
        String izmirsube = izmirtoplamtl + "-" + izmirtoplamdl;
        String igdirsube = igdirtoplamtl + "-" + igdirtoplamdl;
        String simya = simyatoplamtl + "-" + simyatoplamdl;
        String birce = bircetoplamtl + "-" + bircetoplamdl;
        String mudanya = mudanyatoplamtl + "-" + mudanyatoplamdl;
        String star = startoplamtl + "-" + startoplamdl;
        String charge = chargetoplamtl + "-" + chargetoplamdl;
        String avelice = avelicetoplamtl + "-" + avelicetoplamdl;
        return meteorpetrol + "&" + terminal + "&" + meteorinsaat + "&" + cemcanpetrol + "&" + nccpetrol + "&" +
            izmirsube + "&" + igdirsube + "&" + simya + "&" + birce + "&" + mudanya + "&" +
            star + "&" + charge + "&" + avelice;
    }
}
