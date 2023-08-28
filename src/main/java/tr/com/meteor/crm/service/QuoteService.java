package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.service.dto.QuoteSendDocumentDTO;
import tr.com.meteor.crm.utils.Documents;
import tr.com.meteor.crm.utils.configuration.Configurations;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.ContactRepository;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.repository.QuoteRepository;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class QuoteService extends GenericIdNameAuditingEntityService<Quote, UUID, QuoteRepository> {

    private static Map<String, String> documents = new HashMap<>();

    static {
        for (Documents d : Documents.values()) {
            documents.put(d.name(), d.getLabel());
        }
    }

    private final ContactRepository contactRepository;
    private final MailService mailService;
    private final ContractRepository contractRepository;
    private final ActivityRepository activityRepository;

    public QuoteService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                        BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                        BaseConfigurationService baseConfigurationService,
                        QuoteRepository repository, ContactRepository contactRepository, MailService mailService, ContractRepository contractRepository, ActivityRepository activityRepository, QuoteRepository quoteRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Quote.class, repository);
        this.contactRepository = contactRepository;
        this.mailService = mailService;
        this.contractRepository = contractRepository;
        this.activityRepository = activityRepository;
    }

    public Map<String, String> getDocumentList() {
        return documents;
    }

    public Optional<Quote> findLatestQuote() {
        return repository.findTopByOrderByCreatedDateDesc();
    }
    @Async
    public void sendDocuments(QuoteSendDocumentDTO dto, User user) throws Exception {
        Optional<Quote> quote = repository.findById(dto.getQuoteId());

        if (!quote.isPresent()) {
            throw new Exception("Teklif bulunamadı.");
        }

        /*Customer customer = quote.get().getCustomer();

        if (customer == null) {
            throw new Exception("Bağlı müşteri bulunamadı.");
        }

        List<Address> addresses = customer.getAddresses();

        String addressStr = null;
        if (addresses != null && !addresses.isEmpty() && StringUtils.isNotBlank(addresses.get(0).toAddressString())) {
            addressStr = addresses.get(0).toAddressString();
        }
*/
        /*Set<String> mails = new HashSet<>();
        if (StringUtils.isNotBlank(customer.getEmail())) {
            mails.add(customer.getEmail());
        }*/

        /*Optional<Contact> contact = contactRepository.findAllByCustomerId(customer.getId())
            .stream()
            .filter(x -> x.getType() != null && x.getType().getId().equals(ContactType.BIRINCIL.getId()))
            .findFirst();*/

        /*if (contact.isPresent() && StringUtils.isNotBlank(contact.get().getEmail())) {
            mails.add(contact.get().getEmail());
        }

        if (mails.isEmpty()) {
            throw new Exception("Müşteri ve ya birincil kişiye ait mail adresi bulunamadı.");
        }*/

        Map<String, InputStreamSource> map = new HashMap<>();
        boolean sendBackOffice = false;
        /*for (Documents document : dto.getDocs()) {
            switch (document) {
                case OTOBIL_BILGILENDIRME:
                    map.put(document.getFileName(), PdfTemplates.otobilBilgilendirme(user));
                    break;
                case OTOBIL_SOZLESME:
                    sendBackOffice = true;
                    map.put(document.getFileName(), PdfTemplates.otobilSozlesme(
                        customer.getName(),
                        "% " + quote.get().getDiscountDiesel(),
                        "% " + quote.get().getDiscountGasoline(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.now())
                    ));
                    break;
                case METEORCARD_SOZLESME:
                    sendBackOffice = true;
                    map.put(document.getFileName(), PdfTemplates.meteorcardSozlesme(
                        customer.getName(),
                        addressStr,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.now())
                    ));
                    break;
                case OTOFILO_SOZLESME:
                    sendBackOffice = true;
                    map.put(document.getFileName(), PdfTemplates.otofiloSozlesme(
                        customer.getName(),
                        addressStr,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.now())
                    ));
                    break;
                case E_ARSIV_TALEP_DILEKCESI:
                case FIRMA_BILGILERI_FORMU:
                case OTOBIL_ARAC_KAYIT_FORMU:
                case OTOFILO_KAYIT_FORMU:
                case METEORCARD_AKTIVASYON_TALEP_FORMU:
                case METEORCARD_TALEP_FORMU:
                    map.put(document.getFileName(), document.getFile());
                    break;
            }
        }*/

        if (map.isEmpty()) {
            throw new Exception("Gönderilecek dosya seçilmedi.");
        }

        Set<String> ccs = new HashSet<>();
        String ccMail = baseConfigurationService.getConfigurationById(Configurations.SOZLESME_CC.getId()).getStoredValue();

        if (sendBackOffice && StringUtils.isNotBlank(ccMail)) {
            ccs.add(ccMail);
        }

        /*if (customer.getOwner() != null && StringUtils.isNotBlank(customer.getOwner().getEmail())) {
            ccs.add(customer.getOwner().getEmail());
        }

        mailService.sendWithAttachments(mails, ccs, null, "Dökümanlar", "Dökümanlar ektedir.",
            true, false, map
        );*/
    }

    /*public Contract createContract(UUID quoteId) throws Exception {
        if (contractRepository.existsByQuoteIdAndStatus_IdNotAndEndDateGreaterThanEqual(quoteId, ContractStatus.PASIF.getId(), Instant.now())) {
            throw new Exception("Bu müşteriye ait pasif olmayan bir sözleşme mevcut.");
        }

        Quote quote = repository.getOne(quoteId);

        if (quote.getApprovalStatus() != null) {
            if (quote.getApprovalStatus().getId().equals(QuoteApprovalStatus.ONAY_BEKLIYOR.getId())) {
                throw new Exception("Onay Bekleyen tekliflerden sözleşme oluşturulamaz.");
            }

            if (quote.getApprovalStatus().getId().equals(QuoteApprovalStatus.REDDEDILDI.getId())) {
                throw new Exception("Reddedilmiş tekliflerden sözleşme oluşturulamaz.");
            }
        }

        Contract contract = new Contract();
        contract.setQuote(quote);
        contract.setDescription(quote.getDescription());
        //contract.setCustomer(quote.getCustomer());
        contract.setStatus(ContractStatus.AKTIF.getAttributeValue());*/
        /*contract.setFuelTl(quote.getFuelTl());
        contract.setFuelLt(quote.getFuelLt());
        contract.setDiscountDiesel(quote.getDiscountDiesel());
        contract.setDiscountGasoline(quote.getDiscountGasoline());
        contract.setPaymentDay(quote.getPaymentDay());
        contract.setPaymentDay(quote.getPaymentDay());
        contract.setPaymentMethod(quote.getPaymentMethod());
        contract.setCustomerStatus(quote.getCustomer() == null ? null : quote.getCustomer().getStatus());
        contract.setType(quote.getBuyType());

        contract.setStartDate(Instant.now());
        Instant endDate = Instant.now()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime().plusYears(3)
            .atZone(ZoneId.systemDefault())
            .toInstant();
        contract.setEndDate(endDate);

        contractRepository.insert(contract);

        return contract;
    }*/

    /*public void updateApprovalStatus(UUID quoteId, boolean isApproved) throws Exception {
        if (!isUserHaveOperation(getCurrentUserId(), Operations.TEKLIF_ONAYLAYABILME.getId())) {
            throw new Exception("Teklif onaylayabilme yetkiniz yok.");
        }

        Quote quote = repository.getOne(quoteId);

        if (quote.getApprovalStatus() != null && !quote.getApprovalStatus().getId().equals(QuoteApprovalStatus.ONAY_BEKLIYOR.getId())) {
            throw new Exception("Sadece onay bekleyen teklifler onaylanabilir ve reddedilebilir.");
        }

        List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(getCurrentUser()).stream()
            .map(User::getId).collect(Collectors.toList());

        if (!hierarchicalUserIds.contains(quote.getCreatedBy().getId())) {
            throw new Exception("Bu kullanıcının tekliflerini onaylama yetkiniz bulunmamakta.");
        }

        if (isApproved) {
            quote.setApprovalStatus(QuoteApprovalStatus.ONAYLANDI.getAttributeValue());
        } else {
            quote.setApprovalStatus(QuoteApprovalStatus.REDDEDILDI.getAttributeValue());
        }

        quote.setApprovalUser(getCurrentUser());

        repository.save(quote);
    }*/

    public byte[] generateExcelQuoteReportForUser(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds)
            )
        );

        List<Quote> quotes = getData(null, request, false).getBody();
        //List<Contract> contracts = contractRepository.findAllByQuoteIdIn(quotes.stream().map(Quote::getId).collect(Collectors.toList()));

        Map<UUID, Quote> activityIdQuoteMap = new HashMap<>();
        //Map<UUID, Contract> quoteIdContractMap = new HashMap<>();

        /*for (Contract contract : contracts) {
            if (activityIdQuoteMap.containsKey(contract.getQuote().getId())) {
                Contract q = quoteIdContractMap.get(contract.getQuote().getId());

                if (q.getCreatedDate().isAfter(contract.getCreatedDate())) {
                    quoteIdContractMap.put(contract.getQuote().getId(), contract);
                }
            } else {
                quoteIdContractMap.put(contract.getQuote().getId(), contract);
            }
        }*/

        Map<Long, List<Quote>> userIdQuotes = new HashMap<>();

        for (Quote quote : quotes) {
            List<Quote> userQuotes = new ArrayList<>();

            if (userIdQuotes.containsKey(quote.getOwner().getId())) {
                userQuotes = userIdQuotes.get(quote.getOwner().getId());
            }

            userQuotes.add(quote);

            userIdQuotes.put(quote.getOwner().getId(), userQuotes);
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

        XSSFCell quoteIdHeaderCell = headerRow.createCell(columnIndex++);  //Rapor ID
        XSSFCell quoteOwnerHeaderCell = headerRow.createCell(columnIndex++);  //Hazırlayan
        XSSFCell quoteAssignerHeaderCell = headerRow.createCell(columnIndex++);    //Üst Yönetici

        quoteIdHeaderCell.setCellStyle(headerCellStyle);
        quoteIdHeaderCell.setCellValue("Rapor ID");
        quoteOwnerHeaderCell.setCellStyle(headerCellStyle);
        quoteOwnerHeaderCell.setCellValue("Hazırlayan");
        quoteAssignerHeaderCell.setCellStyle(headerCellStyle);
        quoteAssignerHeaderCell.setCellValue("Üst Yönetici");

        for (User user : hierarchicalUsers) {
            if (!userIdQuotes.containsKey(user.getId())) continue;
            List<Quote> sortedUserActivities = userIdQuotes.get(user.getId()).stream()
                .sorted(Comparator.comparing(Quote::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
            for (Quote quote : sortedUserActivities) {
                //Contract contract = quoteIdContractMap.get(quote.getId());
                XSSFRow row = sheet.createRow(rowIndex++);
                columnIndex = 1;

                XSSFCell quoteIdCell = row.createCell(columnIndex++);  //Rapor ID
                XSSFCell quoteOwnerCell = row.createCell(columnIndex++);  //Hazırlayan
                XSSFCell quoteAssignerCell = row.createCell(columnIndex++);    //Üst Yönetici

                quoteIdCell.setCellValue(quote.getId().toString());
                quoteOwnerCell.setCellValue(quote.getOwner().getFullName());
                quoteAssignerCell.setCellValue(quote.getAssigner().getFullName());
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
