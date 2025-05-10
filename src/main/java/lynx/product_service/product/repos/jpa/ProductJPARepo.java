package lynx.product_service.product.repos.jpa;

import lynx.product_service.product.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJPARepo extends JpaRepository<ProductEntity, String> {
}
