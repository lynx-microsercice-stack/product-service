package lynx.product_service.product.repos.elasticsearch;

import lynx.product_service.product.model.entity.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductESSearchRepo extends ElasticsearchRepository<ProductEntity, String> {

    List<ProductEntity> searchByColor(String color);

}
