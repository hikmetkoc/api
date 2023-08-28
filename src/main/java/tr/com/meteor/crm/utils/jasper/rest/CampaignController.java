package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Campaign;
import tr.com.meteor.crm.repository.CampaignRepository;
import tr.com.meteor.crm.service.CampaignService;

import java.util.UUID;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController extends GenericIdNameAuditingEntityController<Campaign, UUID, CampaignRepository, CampaignService> {

    public CampaignController(CampaignService service) {
        super(service);
    }
}
