package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Product;
import tr.com.meteor.crm.repository.ProductRepository;
import tr.com.meteor.crm.service.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController extends GenericIdNameAuditingEntityController<Product, UUID, ProductRepository, ProductService> {

    public ProductController(ProductService service) {
        super(service);
    }
}
