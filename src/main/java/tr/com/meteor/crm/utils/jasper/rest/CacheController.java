package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.service.CacheService;

@RestController
@RequestMapping("/api/clear-cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping
    public void clearCache() {
        cacheService.clearCache();
    }
}
