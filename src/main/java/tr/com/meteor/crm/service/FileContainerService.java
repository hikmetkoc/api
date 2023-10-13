package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.FileContainerRepository;
import tr.com.meteor.crm.repository.InvoiceListRepository;
import tr.com.meteor.crm.repository.SapSoapRepository;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileContainerService extends GenericIdNameAuditingEntityService<FileContainer, UUID, FileContainerRepository> {

    private final MailService mailService;

    private final SapSoapRepository sapSoapRepository;

    private final InvoiceListRepository invoiceListRepository;

    public FileContainerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                BaseConfigurationService baseConfigurationService,
                                FileContainerRepository repository, MailService mailService, SapSoapRepository sapSoapRepository, InvoiceListRepository invoiceListRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            FileContainer.class, repository);
        this.mailService = mailService;
        this.sapSoapRepository = sapSoapRepository;
        this.invoiceListRepository = invoiceListRepository;
    }

    public String getBase64FileCode(String locName, String location) {
        try {
            UUID findId = UUID.fromString(location);
            if(locName.equals("InvoiceList")) {
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

    public String uploadFile(String location, String locName, String code, String name) throws Exception {
        try {
            FileContainer fileContainer = new FileContainer();
            fileContainer.setOwner(getCurrentUser());
            fileContainer.setName(name);
            fileContainer.setLocation(location);
            fileContainer.setLocName(locName);
            fileContainer.setCode(code);
            repository.save(fileContainer);
            return "Dosya Yükleme Başarılı!";
        } catch (Exception e) {
            return "Dosya Yükleme Hatası!";
        }
    }
}
