package tr.com.meteor.crm.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.PersonalContract;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.PersonalContractRepository;
import tr.com.meteor.crm.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class PersonalContractService extends GenericIdNameAuditingEntityService<PersonalContract, UUID, PersonalContractRepository> {
    private final MailService mailService;

    public PersonalContractService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                   BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                   BaseConfigurationService baseConfigurationService, PersonalContractRepository repository,
                                   MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            PersonalContract.class, repository);
        this.mailService = mailService;
    }

    public File ChangeWord(String adsoyad) throws Exception {
        String base64 = repository.findById(UUID.fromString("9c145f2f-a4fd-4d4f-a576-94f2e3d0f16c")).get().getBelge();
        try {
            // Base64 verisini byte dizisine çevirin
            byte[] decodedBytes = Base64.getDecoder().decode(base64);

            // Byte dizisini bir .docx dosyasına yazın
            FileOutputStream fos = new FileOutputStream("input.docx");
            fos.write(decodedBytes);
            fos.close();

            // .docx dosyasını işleyebilirsiniz
            FileInputStream fis = new FileInputStream("input.docx");
            XWPFDocument document = new XWPFDocument(fis);
            // ... belgeyi kullanarak yapmak istediğiniz işlemleri gerçekleştirin

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);
                    if (text != null && text.contains("PANELDEN GELECEK OLAN İSİM")) {
                        text = text.replace("PANELDEN GELECEK OLAN İSİM", adsoyad);
                        run.setText(text, 0);
                    }
                }
            }
            FileOutputStream fos2 = new FileOutputStream("degistirilmis_belge.docx");
            document.write(fos2);
            File changedFile = new File("degistirilmis_belge.docx");
            fis.close();
            fos.close();
            fos2.close();
            return changedFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
