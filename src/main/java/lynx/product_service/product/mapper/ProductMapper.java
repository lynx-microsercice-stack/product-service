package lynx.product_service.product.mapper;

import lynx.product_service.product.model.dto.request.CreateProductRequest;
import lynx.product_service.product.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    ProductEntity toEntityFromCreateProductRequest(CreateProductRequest request);
}
