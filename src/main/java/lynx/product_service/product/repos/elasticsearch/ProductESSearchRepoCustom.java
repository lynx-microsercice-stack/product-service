package lynx.product_service.product.repos.elasticsearch;

import java.util.List;

import lynx.product_service.product.model.entity.ProductEntity;
import lynx.product_service.product.service.ProductQuery;

public interface ProductESSearchRepoCustom {
    List<ProductEntity> search(ProductQuery queryParams);
}
