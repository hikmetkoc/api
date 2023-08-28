package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.QuoteVersionLineItem;
import tr.com.meteor.crm.repository.QuoteVersionLineItemRepository;
import tr.com.meteor.crm.service.QuoteVersionLineItemService;

import java.util.UUID;

@RestController
@RequestMapping("/api/quote-version-line-items")
public class QuoteVersionLineItemController extends GenericIdEntityController<QuoteVersionLineItem, UUID, QuoteVersionLineItemRepository, QuoteVersionLineItemService> {

    public QuoteVersionLineItemController(QuoteVersionLineItemService service) {
        super(service);
    }
}
