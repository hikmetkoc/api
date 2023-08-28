package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.dto.DashboardSearchDTO;
import tr.com.meteor.crm.service.dto.QuickActivityDTO;
import tr.com.meteor.crm.utils.Documents;
import tr.com.meteor.crm.utils.attributevalues.*;
import tr.com.meteor.crm.utils.configuration.Configurations;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.jasper.PdfTemplates;
import tr.com.meteor.crm.utils.request.Column;
import tr.com.meteor.crm.utils.request.ColumnType;
import tr.com.meteor.crm.utils.request.Request;
import tr.com.meteor.crm.utils.jasper.rest.errors.RecordNotFoundException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class DashboardService extends BaseService {

    private final CustomerService customerService;
    private final ContactService contactService;
    private final CampaignService campaignService;
    private final ActivityService activityService;
    private final QuoteService quoteService;
    private final AddressService addressService;
    private final ContractService contractService;
    private final MailService mailService;
    private final SummaryUserCustomerService summaryUserCustomerService;
    private final SummaryOpetSaleService summaryOpetSaleService;
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    private final ActivityRepository activityRepository;
    private final QuoteRepository quoteRepository;
    private final ContractRepository contractRepository;
    private final TaskRepository taskRepository;
    private final LeadRepository leadRepository;

    public DashboardService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                            BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                            BaseConfigurationService baseConfigurationService,
                            CustomerService customerService, ContactService contactService,
                            CampaignService campaignService, ActivityService activityService,
                            QuoteService quoteService, AddressService addressService, ContractService contractService,
                            MailService mailService, SummaryUserCustomerService summaryUserCustomerService,
                            SummaryOpetSaleService summaryOpetSaleService, CustomerRepository customerRepository,
                            ContactRepository contactRepository, AddressRepository addressRepository,
                            ActivityRepository activityRepository, QuoteRepository quoteRepository,
                            ContractRepository contractRepository, TaskRepository taskRepository,
                            LeadRepository leadRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService);
        this.customerService = customerService;
        this.contactService = contactService;
        this.campaignService = campaignService;
        this.activityService = activityService;
        this.quoteService = quoteService;
        this.addressService = addressService;
        this.contractService = contractService;
        this.mailService = mailService;
        this.summaryUserCustomerService = summaryUserCustomerService;
        this.summaryOpetSaleService = summaryOpetSaleService;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
        this.activityRepository = activityRepository;
        this.quoteRepository = quoteRepository;
        this.contractRepository = contractRepository;
        this.taskRepository = taskRepository;
        this.leadRepository = leadRepository;
    }

    public DashboardSearchDTO searchAll(String search) throws Exception {
        DashboardSearchDTO dashboardSearchDTO = new DashboardSearchDTO();

        Request request = Request.build().search(search).page(0).size(5);

        dashboardSearchDTO.setCustomerList(customerService.getData(getCurrentUser(), request).getBody());
        dashboardSearchDTO.setContactList(contactService.getData(getCurrentUser(), request).getBody());
        dashboardSearchDTO.setCampaignList(campaignService.getData(getCurrentUser(), request).getBody());

        return dashboardSearchDTO;
    }

   /* public QuickActivityDTO quickActivity(QuickActivityDTO quickActivityDTO) throws Exception {
        if (quickActivityDTO == null || quickActivityDTO.getCustomer() == null
            || (StringUtils.isBlank(quickActivityDTO.getCustomer().getName()) && quickActivityDTO.getCustomer().getId() == null)) {
            throw new Exception("Müşteri bilgisi zorunludur.");
        }
        else if (quickActivityDTO.getCustomer().getFleetCode() == null) {
            throw new Exception("Müşteri numarası zorunludur.");
        }

        System.out.println("LOG__1");

        Customer customer = quickActivityDTO.getCustomer();
        if (customer.getId() == null) {
            System.out.println("LOG__2");
            customer.setStatus(CustomerStatus.YENI.getAttributeValue());
            customerRepository.insert(customer);
        } else if (!customerRepository.existsById(customer.getId())) {
            System.out.println("LOG__3");
            throw new RecordNotFoundException("Customer", customer.getId());
        } else {
            System.out.println("LOG__4");
            customerRepository.update(customer);
            customer = customerRepository.findById(customer.getId()).get();
        }
        System.out.println("LOG__5");

        Address address = quickActivityDTO.getAddress();

        System.out.println("LOG__6");

        if (address != null) {
            System.out.println("LOG__7");
            if (StringUtils.isAllBlank(address.getName(), address.getDetail()) && address.getCity() == null && address.getDistrict() == null) {
                System.out.println("LOG__8");
                address = null;
            } else {
                System.out.println("LOG__9");

                if (StringUtils.isBlank(address.getName())) {
                    System.out.println("LOG__10");
                    address.setName("Varsayılan Başlık");
                }

                if (address.getId() == null) {
                    System.out.println("LOG__11");
                    address.setCustomer(customer);
                    addressRepository.insert(address);
                } else {
                    System.out.println("LOG__12");
                    addressRepository.update(address);
                }
            }
            System.out.println("LOG__13");
        }

        System.out.println("LOG__13.5");

        Contact contact = quickActivityDTO.getContact();
        if (contact != null) {
            System.out.println("LOG__14");
            if (contact.getId() != null) {
                System.out.println("LOG__15");
                if (contactRepository.existsById(contact.getId())) {
                    System.out.println("LOG__16");
                    contactRepository.update(contact);
                    contact = contactRepository.findById(contact.getId()).get();
                    if (contact.getCustomer().getId() != customer.getId()) {
                        System.out.println("LOG__17");
                        throw new Exception("Seçilen kişi bu müşteri ile ilişkili değil.");
                    }
                } else {
                    System.out.println("LOG__18");
                    throw new RecordNotFoundException("Contact", contact.getId());
                }
            } else if (StringUtils.isNotBlank(contact.getFirstName())) {
                System.out.println("LOG__19");
                contact.setCustomer(quickActivityDTO.getCustomer());
                contactRepository.insert(contact);
            } else {
                System.out.println("LOG__20");
                contact = null;
            }
        }

        System.out.println("LOG__21");

        Activity activity = quickActivityDTO.getActivity();
        if (activity != null && activity.getCheckInTime() != null) {
            System.out.println("LOG__22");

            activity.setCustomer(customer);
            activity.setCustomerStatus(customer.getStatus());

            if (activity.getCheckInTime() == null && activity.getCheckOutTime() == null) {
                System.out.println("LOG__23");
                activity.setStatus(ActivityStatus.YENI.getAttributeValue());
            } else if (activity.getCheckInTime() != null && activity.getCheckOutTime() != null) {
                System.out.println("LOG__24");
                activity.setStatus(ActivityStatus.TAMAMLANDI.getAttributeValue());
            } else if (activity.getCheckInTime() != null) {
                System.out.println("LOG__25");
                activity.setStatus(ActivityStatus.DEVAM_EDIYOR.getAttributeValue());
            }

            System.out.println("LOG__25.5");

            if (address != null && quickActivityDTO.getCustomerSite() && activity.getCheckInLatitude() != null && activity.getCheckInLongitude() != null) {
                System.out.println("LOG__26");
                address.setLatitude(activity.getCheckInLatitude());
                address.setLongitude(activity.getCheckInLongitude());

                if (address.getId() != null) {
                    System.out.println("LOG__27");
                    addressRepository.update(address);
                }
            }

            System.out.println("LOG__27.5");


            if (!quickActivityDTO.getCustomerSite()) {
                System.out.println("LOG__28");
                activity.setCheckOutLatitude(activity.getCheckInLatitude());
                activity.setCheckOutLongitude(activity.getCheckInLongitude());
            }

            System.out.println("LOG__29");

            if (contact != null) {
                System.out.println("LOG__30");
                activity.setContact(contact);
            }

            activityRepository.insert(activity);
            if (customer.getStatus().getId().equals(CustomerStatus.YENI.getId())) {
                System.out.println("LOG__31");
                customer.setStatus(CustomerStatus.MEVCUT.getAttributeValue());
                customerRepository.save(customer);
            }
        }

        System.out.println("LOG__32");

        Quote quote = quickActivityDTO.getQuote();
        if (quote != null && (quote.getDiscountDiesel() != null || quote.getDiscountGasoline() != null)) {

            //quote.setCustomer(customer);
            quote.setName(customer.getName() + " (Hızlı Aktivite)");
            if (activity != null) {
                if (activity.getType().getId().equals(ActivityType.OTOFILO_SOZLESME.getId())) {
                    quote.setContractType(ContractType.OTOFILO_SOZLESME.getAttributeValue());
                } else if (activity.getType().getId().equals(ActivityType.METEORCARD_SOZLESME.getId())) {
                    quote.setContractType(ContractType.METEORCARD_SOZLESME.getAttributeValue());
                } else {
                    quote.setContractType(ContractType.SOZLESME.getAttributeValue());
                }

                quote.setActivity(activity);
            }

            quote.setStage(QuoteStage.YENI.getAttributeValue());

            quoteRepository.insert(quote);

            if (quickActivityDTO.isCreateContract()) {
                Contract contract = new Contract();
                contract.setCustomer(customer);
                contract.setName(customer.getName() + " (Hızlı Aktivite)");
                contract.setQuote(quote);
                contract.setFuelLt(quote.getFuelLt());
                contract.setFuelTl(quote.getFuelTl());
                contract.setDiscountDiesel(quote.getDiscountDiesel());
                contract.setDiscountGasoline(quote.getDiscountGasoline());
                contract.setPaymentDay(quote.getPaymentDay());
                contract.setPaymentPeriod(quote.getPaymentPeriod());
                contract.setStatus(ContractStatus.TEMINAT_SURECINDE.getAttributeValue());
                contract.setType(quote.getBuyType());

                contractRepository.insert(contract);

                quote.setStage(QuoteStage.KAZANILDI.getAttributeValue());

                if (quote.getId() != null) {
                    quoteRepository.update(quote);
                }
            }

            if (quote.getStage().getId().equals(QuoteStage.KAZANILDI.getId())) {
                customer.setStatus(CustomerStatus.MEVCUT_AKTIF.getAttributeValue());
                customerRepository.save(customer);
            }
        }

        System.out.println("LOG__33");
        Task task = quickActivityDTO.getTask();
        if (task != null && task.getDueTime() != null) {
            System.out.println("LOG__34");

            //task.setCustomer(customer);
            task.setStatus(TaskStatus.YENI.getAttributeValue());

            taskRepository.insert(task);
        } else {
            System.out.println("LOG__35");

            task = null;
        }

        if (activity != null && task != null) {
            System.out.println("LOG__36");

            activity.setTask(task);

            if (activity.getId() != null) {
                System.out.println("LOG__37");

                activityRepository.update(activity);
            }
        }

        System.out.println("LOG__38");

        Lead lead = quickActivityDTO.getLead();

        if (lead != null) {
            if (leadRepository.existsById(lead.getId())) {
                lead = leadRepository.findById(lead.getId()).get();
            } else {
                throw new RecordNotFoundException("Lead", lead.getId());
            }

            lead.setCustomer(customer);

            if (lead.getId() != null) {
                leadRepository.update(lead);
            }
        }

        return quickActivityDTO;
    }*/

    @Async
    public void sendInfoDocument(QuickActivityDTO quickActivityDTO, User user) throws Exception {
        Set<String> mails = checkAndGetMails(quickActivityDTO);

        mailService.sendWithAttachment(mails, null, null, "Otobil Bilgilendirme", "Otobil bilgilendirme içeriği ektedir.",
            true, false, "OtobilBilgilendirme.pdf",
            PdfTemplates.otobilBilgilendirme(user));
    }

   /* @Async
    public void sendProductDocument(QuickActivityDTO quickActivityDTO, User user) throws Exception {
        Set<String> mails = checkAndGetMails(quickActivityDTO);

        mailService.sendWithAttachment(mails, null, null, "Otobil Bilgilendirme", "Otobil bilgilendirme içeriği ektedir.",
            true, false, "OtobilBilgilendirme.pdf",
            PdfTemplates.otobilBilgilendirme(user));
    }*/
    @Async
    public void sendContractDocument(QuickActivityDTO quickActivityDTO, Long userId) throws Exception {
        User user = baseUserService.getUserFullFetched(userId).get();
        Set<String> mails = checkAndGetMails(quickActivityDTO);

        Map<String, InputStreamSource> map = new HashMap<>();

        /*Customer customer = quickActivityDTO.getCustomer();
        String addressStr = quickActivityDTO.getAddress() == null ? null : quickActivityDTO.getAddress().toAddressString();
        if (StringUtils.isBlank(addressStr)) addressStr = null;*/

        /*if (quickActivityDTO.getActivity().getType().getId().equals(ActivityType.OTOFILO_SOZLESME.getId())) {
            map.put(Documents.OTOFILO_SOZLESME.getFileName(), PdfTemplates.otofiloSozlesme(
                customer.getName(),
                addressStr,
                DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.now())
            ));
            map.put(Documents.OTOFILO_KAYIT_FORMU.getFileName(), Documents.OTOFILO_KAYIT_FORMU.getFile());
        } else if (quickActivityDTO.getActivity().getType().getId().equals(ActivityType.METEORCARD_SOZLESME.getId())) {
            map.put("MeteorcardSozlesme.pdf", PdfTemplates.meteorcardSozlesme(
                customer.getName(),
                addressStr,
                DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.now())
            ));
            map.put(Documents.METEORCARD_AKTIVASYON_TALEP_FORMU.getFileName(), Documents.METEORCARD_AKTIVASYON_TALEP_FORMU.getFile());
            map.put(Documents.METEORCARD_TALEP_FORMU.getFileName(), Documents.METEORCARD_TALEP_FORMU.getFile());
        } else {
            map.put(Documents.OTOBIL_SOZLESME.getFileName(), PdfTemplates.otobilSozlesme(
                customer.getName(),
                "% " + quickActivityDTO.getQuote().getDiscountDiesel(),
                "% " + quickActivityDTO.getQuote().getDiscountGasoline(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.now())
            ));
            map.put(Documents.OTOBIL_ARAC_KAYIT_FORMU.getFileName(), Documents.OTOBIL_ARAC_KAYIT_FORMU.getFile());
            map.put(Documents.E_ARSIV_TALEP_DILEKCESI.getFileName(), Documents.E_ARSIV_TALEP_DILEKCESI.getFile());
            map.put(Documents.FIRMA_BILGILERI_FORMU.getFileName(), Documents.FIRMA_BILGILERI_FORMU.getFile());
        }*/

        Set<String> ccs = new HashSet<>();
        String ccMail = baseConfigurationService.getConfigurationById(Configurations.SOZLESME_CC.getId()).getStoredValue();

        if (StringUtils.isNotBlank(ccMail)) {
            ccs.add(ccMail);
        }

        /*if (customer.getId() == null && StringUtils.isNotBlank(user.getEmail())) {
            ccs.add(user.getEmail());
        } else if (customer.getOwner() != null && StringUtils.isNotBlank(customer.getOwner().getEmail())) {
            ccs.add(customer.getOwner().getEmail());
        }*/

        mailService.sendWithAttachments(mails, ccs, null,
            "Sözleşme Dökümanları", "Sözleşme dökümanları ektedir.", true, false, map
        );
    }

    private Set<String> checkAndGetMails(QuickActivityDTO quickActivityDTO) throws Exception {
        boolean customerEmailEmpty = quickActivityDTO == null
            || quickActivityDTO.getCustomer() == null || StringUtils.isBlank(quickActivityDTO.getCustomer().getEmail());

        boolean contactEmailEmpty = quickActivityDTO == null
            || quickActivityDTO.getContact() == null || StringUtils.isBlank(quickActivityDTO.getContact().getEmail());

        if (customerEmailEmpty && contactEmailEmpty) {
            throw new Exception("En az bir mail adresi gereklidir.");
        }

        Set<String> mails = new HashSet<>();
        if (!customerEmailEmpty) mails.add(quickActivityDTO.getCustomer().getEmail());
        if (!contactEmailEmpty) mails.add(quickActivityDTO.getContact().getEmail());

        return mails;
    }

    public Object dashboard(Long userId, Integer year) throws Exception {
        User user = userId == null ? getCurrentUser() : baseUserService.getUserFullFetched(userId).get();
        Map<String, Object> map = new HashMap<>();

        /*map.put(Activity.class.getSimpleName(), getObjectSummary(Activity.class, user));
        map.put(Quote.class.getSimpleName(), getObjectSummary(Quote.class, user));
        map.put(Contract.class.getSimpleName(), getObjectSummary(Contract.class, user));
        map.put(Customer.class.getSimpleName(), getSummaryUserCustomer(user));
        map.put(SummaryOpetSale.class.getSimpleName(), getSummaryOpetSale(user, null, year));
        map.put("pendingQuotes", getPendingQuotes(user));*/

        return map;
    }

    /*private Object getPendingQuotes(User user) throws Exception {
        return quoteService.getData(
            null,
            Request.build().page(0).size(Integer.MAX_VALUE).filter(Filter.And(
                Filter.FilterItem("approvalStatus", FilterItem.Operator.EQUALS, QuoteApprovalStatus.ONAY_BEKLIYOR.getId()),
                Filter.FilterItem(
                    "createdBy.id",
                    FilterItem.Operator.IN,
                    baseUserService.getHierarchicalUsersOnlyDownwards(user)
                        .stream().map(User::getId).collect(Collectors.toList())
                )
            )),
            false
        ).getBody();
    }
*/
   /* private Map<String, Object> getObjectSummary(Class object, User user) throws Exception {
        Column checkInTime = new Column().name("checkInTime").title("checkInTime");
        Column createdDate = new Column().name("createdDate").title("createdDate");
        Column ownerColumn = new Column().name("owner.id").title("ownerId");
        Column createdByColumn = new Column().name("createdBy.id").title("createdById");
        Column customerStatus = new Column().name("customer.status.id").title("customerStatus");

        List<Map<String, Object>> data;

        if (object.equals(Activity.class)) {
            data = activityService
                .getDataWithColumnSelection(user, getSummaryRequest(user, ownerColumn, checkInTime, customerStatus), false)
                .getBody();
            return groupCountByCustomerStatus(data, checkInTime, customerStatus);
        } else if (object.equals(Quote.class)) {
            data = quoteService
                .getDataWithColumnSelection(user, getSummaryRequest(user, createdByColumn, createdDate, customerStatus), false)
                .getBody();
            return groupCountByCustomerStatus(data, createdDate, customerStatus);
        } else if (object.equals(Contract.class)) {
            data = contractService
                .getDataWithColumnSelection(user, getSummaryRequest(user, createdByColumn, createdDate, customerStatus), false)
                .getBody();
            return groupCountByCustomerStatus(data, createdDate, customerStatus);
        } else {
            throw new Exception("Method: getObjectSummary. Unsupported Object.");
        }
    }
*/
    /*private Object getSummaryOpetSale(User user, UUID customerId, Integer year) throws Exception {
        Instant firstDay = Year.of(year).atMonth(1).atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant lastDay = Year.of(year).atMonth(12).atEndOfMonth().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        Filter filter = Filter.And(
            Filter.FilterItem("saleEnd", FilterItem.Operator.GREATER_OR_EQUAL_THAN, firstDay),
            Filter.FilterItem("saleEnd", FilterItem.Operator.LESS_THAN, lastDay)
        );

        if (customerId != null) {
            filter.getFilterList().add(Filter.FilterItem("customer.id", FilterItem.Operator.EQUALS, customerId));
        }

        if (user != null) {
            List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream()
                .map(User::getId).collect(Collectors.toList());
            filter.getFilterList().add(Filter.FilterItem("user.id", FilterItem.Operator.IN, hierarchicalUserIds));
        }

        return summaryOpetSaleService
            .getDataWithColumnSelection(
                user,
                Request.build().page(0).size(Integer.MAX_VALUE)
                    .filter(filter)
                    .addColumn(new Column().name("saleEnd").title("saleEnd"))
                    .addColumn(new Column().name("productName").title("productName"))
                    .addColumn(new Column().name("volume").title("volume").columnType(ColumnType.SUM)),
                false)
            .getBody();
    }
*/
    /*private Object getSummaryUserCustomer(User user) throws Exception {
        Map<Object, Integer> activeCustomer = new TreeMap<>();
        Instant firstDay = Year.now().atMonth(1).atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant lastDay = Year.now().atMonth(12).atEndOfMonth().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream()
            .map(User::getId).collect(Collectors.toList());

        summaryUserCustomerService.getData(
            null,
            Request.build().page(0).size(Integer.MAX_VALUE).filter(
                Filter.And(
                    Filter.FilterItem("date", FilterItem.Operator.GREATER_OR_EQUAL_THAN, firstDay),
                    Filter.FilterItem("date", FilterItem.Operator.LESS_THAN, lastDay),
                    Filter.FilterItem("user.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            ),
            false
        ).getBody().forEach(x -> {
            LocalDate date = x.getDate().atZone(ZoneId.systemDefault()).toLocalDate();

            int activeCount = 0;
            if (activeCustomer.containsKey(date)) {
                activeCount = activeCustomer.get(date);
            }

            activeCustomer.put(date, activeCount + x.getCountActive());
        });

        return activeCustomer;
    }
*/
    /*private Request getSummaryRequest(User user, Column userIdColumn, Column dateColumn, Column customerStatusColumn) {
        Instant firstDay = YearMonth.now().atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant lastDay = YearMonth.now().atEndOfMonth().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsers(user).stream()
            .map(User::getId).collect(Collectors.toList());
        return Request.build().size(Integer.MAX_VALUE).page(0).filter(
            Filter.And(
                Filter.FilterItem(dateColumn.getName(), FilterItem.Operator.GREATER_OR_EQUAL_THAN, firstDay),
                Filter.FilterItem(dateColumn.getName(), FilterItem.Operator.LESS_THAN, lastDay),
                Filter.FilterItem(userIdColumn.getName(), FilterItem.Operator.IN, hierarchicalUserIds),
                Filter.Or(
                    Filter.FilterItem(customerStatusColumn.getName(), FilterItem.Operator.EQUALS, CustomerStatus.YENI.getId()),
                    Filter.FilterItem(customerStatusColumn.getName(), FilterItem.Operator.EQUALS, CustomerStatus.MEVCUT.getId()),
                    Filter.FilterItem(customerStatusColumn.getName(), FilterItem.Operator.EQUALS, CustomerStatus.MEVCUT_AKTIF.getId())
                )
            )
        ).addColumn(dateColumn).addColumn(customerStatusColumn);
    }*/

    /*private Map<String, Object> groupCountByCustomerStatus(List<Map<String, Object>> list, Column dateColumn, Column customerStatusColumn) {
        Map<String, Object> map = new HashMap<>();
        Map<Object, Integer> newCustomer = new TreeMap<>();
        Map<Object, Integer> activeCustomer = new TreeMap<>();

        list.forEach(x -> {
            String status = (String) x.get(customerStatusColumn.getTitle());
            LocalDate date = ((Instant) x.get(dateColumn.getTitle())).atZone(ZoneId.systemDefault()).toLocalDate();

            int count = 1;

            if (status.equals(CustomerStatus.YENI.getId())) {
                if (newCustomer.containsKey(date)) {
                    count = newCustomer.get(date) + 1;
                }

                newCustomer.put(date, count);
            } else if (status.equals(CustomerStatus.MEVCUT.getId()) || status.equals(CustomerStatus.MEVCUT_AKTIF.getId())) {
                if (activeCustomer.containsKey(date)) {
                    count = activeCustomer.get(date) + 1;
                }

                activeCustomer.put(date, count);
            }
        });

        map.put(CustomerStatus.YENI.getId(), newCustomer);
        map.put(CustomerStatus.MEVCUT_AKTIF.getId(), activeCustomer);

        return map;
    }
*/
    /*public Object salesReport(UUID customerId, Integer year) throws Exception {
        return getSummaryOpetSale(null, customerId, year);
    }*/
}
