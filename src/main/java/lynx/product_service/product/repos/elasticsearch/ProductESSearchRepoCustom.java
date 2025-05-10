package lynx.product_service.product.repos.elasticsearch;

import java.util.List;

import lynx.product_service.product.model.dto.ProductQuery;
import lynx.product_service.product.model.entity.ProductEntity;

public interface ProductESSearchRepoCustom {
    List<ProductEntity> search(ProductQuery queryParams);
}
