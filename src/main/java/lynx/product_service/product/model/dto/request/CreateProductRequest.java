package lynx.product_service.product.model.dto.request;

import lombok.Data;
import lynx.product_service.product.mapper.ProductMapper;
import lynx.product_service.product.model.entity.ProductEntity;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private Double price;
    private String categoryId;
    private String imageUrl;
    private String brand;
    private String color;
    private String size;
    private String material;
    private String style;

    public ProductEntity toEntity(ProductMapper productMapper) {
        return productMapper.toEntityFromCreateProductRequest(this);
    }
}
