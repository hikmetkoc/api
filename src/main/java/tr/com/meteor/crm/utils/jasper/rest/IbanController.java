package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.Iban;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.repository.IbanRepository;
import tr.com.meteor.crm.service.AddressService;
import tr.com.meteor.crm.service.IbanService;

import java.util.UUID;

@RestController
@RequestMapping("/api/ibans")
public class IbanController extends GenericIdNameAuditingEntityController<Iban, UUID, IbanRepository, IbanService> {

    public IbanController(IbanService service) {
        super(service);
    }

    @PutMapping("/newIban")
    public ResponseEntity<String> addNewPerson(
        @RequestParam String bank,
        @RequestParam String moneyType,
        @RequestParam String customer,
        @RequestParam String name
    ) throws Exception{

        try {
            // Diğer işlemleri burada gerçekleştirin
            service.newIban(bank, moneyType, customer, name);
            return ResponseEntity.ok().build();
        } catch (NullPointerException exception) {
            // Hata durumunda uygun bir hata yanıtı döndürün
            return ResponseEntity.badRequest().body("Eksik veya hatalı veri girişi yapıldı!");
        }
    }
}
