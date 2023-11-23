package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileContainerService extends GenericIdNameAuditingEntityService<FileContainer, UUID, FileContainerRepository> {

    private final MailService mailService;

    private final SapSoapRepository sapSoapRepository;

    private final InvoiceListRepository invoiceListRepository;

    private final PaymentOrderRepository paymentOrderRepository;

    private final HolidayRepository holidayRepository;

    public FileContainerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                BaseConfigurationService baseConfigurationService,
                                FileContainerRepository repository, MailService mailService, SapSoapRepository sapSoapRepository, InvoiceListRepository invoiceListRepository, PaymentOrderRepository paymentOrderRepository, HolidayRepository holidayRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            FileContainer.class, repository);
        this.mailService = mailService;
        this.sapSoapRepository = sapSoapRepository;
        this.invoiceListRepository = invoiceListRepository;
        this.paymentOrderRepository = paymentOrderRepository;
        this.holidayRepository = holidayRepository;
    }

    public String getBase64FileCode(String locName, String location, String subject) {
        try {
            UUID findId = UUID.fromString(location);
            if (locName.equals("PaymentOrder")) {
                Optional<PaymentOrder> paymentOrder = paymentOrderRepository.findById(findId);
                if (paymentOrder.get().getKaynak().equals("FATURA LİSTESİ")) {
                    String invoiceNum = paymentOrder.get().getInvoiceNum();
                    String vkn = paymentOrder.get().getCustomer().getTaxNumber();
                    List<SapSoap> sapSoapList = sapSoapRepository.findByFaturanoAndVkn(invoiceNum, vkn);
                    return sapSoapList.get(0).getFpdf();
                } else {
                    List<FileContainer> fileContainers = repository.findByLocNameAndLocationAndSubject(locName, location, subject);
                    return fileContainers.get(0).getCode();
                }
            }
            else if (locName.equals("InvoiceList")) {
                Optional<InvoiceList> selectedInvoice = invoiceListRepository.findById(findId);
                String invoiceNum = selectedInvoice.get().getInvoiceNum();
                String vkn = selectedInvoice.get().getCustomer().getTaxNumber();
                List<SapSoap> sapSoapList = sapSoapRepository.findByFaturanoAndVkn(invoiceNum, vkn);
                return sapSoapList.get(0).getFpdf();
            } else {
                List<FileContainer> fileContainers = repository.findByLocNameAndLocation(locName, location);
                return fileContainers.get(0).getCode();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ÖYLESİNE HATA VERİYORUM!");
        }
        return "";
    }

    public String uploadBase64File(String location, String locName, String code, String name, String subject) throws Exception {
        try {
            if (locName.equals("InvoiceList")) {
                UUID uuid = UUID.fromString(location);
                Optional<InvoiceList> invoiceList = invoiceListRepository.findById(uuid);
                List<SapSoap> sapSoap = sapSoapRepository.findByFaturanoAndVkn(invoiceList.get().getInvoiceNum(),invoiceList.get().getCustomer().getTaxNumber());
                sapSoap.get(0).setFpdf(code);
                sapSoapRepository.save(sapSoap.get(0));
                invoiceList.get().setPdf(true);
                invoiceListRepository.save(invoiceList.get());
            } else {
                FileContainer fileContainer = new FileContainer();
                fileContainer.setOwner(getCurrentUser());
                fileContainer.setName(name);
                fileContainer.setLocation(location);
                fileContainer.setLocName(locName);
                fileContainer.setCode(code);
                fileContainer.setSubject(subject);
                repository.save(fileContainer);
            }
            if (locName.equals("PaymentOrder")) {
                UUID pid = UUID.fromString(location);
                Optional<PaymentOrder> paymentOrder = paymentOrderRepository.findById(pid);
                if (subject.equals("Açık Ödeme")) {
                    paymentOrder.get().setClosePdf(true);
                } else if (subject.equals("Fatura")) {
                    paymentOrder.get().setPdf(true);
                }
                paymentOrderRepository.save(paymentOrder.get());
            }
            if (locName.equals("Holiday")) {
                UUID hid = UUID.fromString(location);
                Optional<Holiday> holiday = holidayRepository.findById(hid);
                holiday.get().setPdf(true);
                holidayRepository.save(holiday.get());
            }
            return "Dosya Yükleme Başarılı!";
        } catch (Exception e) {
            return "Dosya Yükleme Hatası!";
        }
    }
}
