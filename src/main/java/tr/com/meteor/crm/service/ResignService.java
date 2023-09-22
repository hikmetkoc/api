package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.utils.attributevalues.ResignStatus;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResignService extends GenericIdNameAuditingEntityService<Resign, UUID, ResignRepository> {
    private final MailService mailService;

    private final HolManagerRepository holManagerRepository;

    public ResignService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                         BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                         BaseConfigurationService baseConfigurationService, ResignRepository repository,
                         MailService mailService, HolManagerRepository holManagerRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Resign.class, repository);
        this.mailService = mailService;
        this.holManagerRepository = holManagerRepository;
    }

    public void saveAnket(Resign resign, String a1, String a2, String a3, String a4, String a5, String a6, String a7, String a8, String a9) throws Exception {
        List<ResignStatus> resignStatus = Arrays.asList(ResignStatus.values());
        for (ResignStatus resignStatus1: resignStatus) {
            if (resignStatus1.getId().equals(a1)) {
                resign.setSorumluluk(resignStatus1.getAttributeValue());
            }
            if (resignStatus1.getId().equals(a2)) {
                resign.setCalismaSaat(resignStatus1.getAttributeValue());
            }
            if (resignStatus1.getId().equals(a3)) {
                resign.setCalismaOrtam(resignStatus1.getAttributeValue());
            }
            if (resignStatus1.getId().equals(a4)) {
                resign.setOdeme(resignStatus1.getAttributeValue());
            }
            if (resignStatus1.getId().equals(a5)) {
                resign.setTakdir(resignStatus1.getAttributeValue());
            }
            if (resignStatus1.getId().equals(a6)) {
                resign.setGelistirme(resignStatus1.getAttributeValue());
            }
            if (resignStatus1.getId().equals(a7)) {
                resign.setIliski(resignStatus1.getAttributeValue());
            }
            if (resignStatus1.getId().equals(a8)) {
                resign.setKariyer(resignStatus1.getAttributeValue());
            }
            if (resignStatus1.getId().equals(a9)) {
                resign.setIletisim(resignStatus1.getAttributeValue());
            }
        }
        List <HolManager> holManager = holManagerRepository.findByUserId(resign.getOwner().getId());
        for (HolManager holManager1 : holManager) {
            if (holManager1.getUser().getId().equals(resign.getOwner().getId())) {
                resign.setAssigner(holManager1.getChief());
            }
        }
        repository.save(resign);
        mailService.sendEmail(resign.getAssigner().getEposta(), "meteorpanel - İşten Çıkış Anketi","Çalışanlardan " +
            resign.getOwner().getFullName() + " işten çıkış anketini doldurdu. Personeller bölümünden anketi inceleyebilirsiniz.", false, false);

        mailService.sendEmail("bt@meteorgrup.com.tr", "meteorpanel - İşten Çıkış Anketi","Çalışanlardan " +
            resign.getOwner().getFullName() + " işten çıkış anketini doldurdu. Personeller bölümünden anketi inceleyebilirsiniz.", false, false);

    }
}
