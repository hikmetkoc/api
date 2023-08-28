package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Product;

import java.util.UUID;

@Repository
public interface ProductRepository extends GenericIdNameAuditingEntityRepository<Product, UUID> {
}
