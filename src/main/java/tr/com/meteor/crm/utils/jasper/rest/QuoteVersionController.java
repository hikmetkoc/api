package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.QuoteVersion;
import tr.com.meteor.crm.repository.QuoteVersionRepository;
import tr.com.meteor.crm.service.QuoteVersionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/quote-versions")
public class QuoteVersionController extends GenericIdNameAuditingEntityController<QuoteVersion, UUID, QuoteVersionRepository, QuoteVersionService> {

    public QuoteVersionController(QuoteVersionService service) {
        super(service);
    }
}
