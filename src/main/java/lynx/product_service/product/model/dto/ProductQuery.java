package lynx.product_service.product.model.dto;


import lombok.Builder;
import lombok.Getter;
import lynx.product_service.product.enums.SortType;
import lynx.product_service.utils.JsonUtil;

@Builder
@Getter
public class ProductQuery {

    private String categoryId;
    private String brand;
    private String color;
    private String size;
    private String material;
    private String style;
    private String price;
    private SortType sort;
    private int page;
    private int limit;
    private Double priceMin;
    private Double priceMax;
    
    public String jsonPrinter() {
        return JsonUtil.prettyPrinter(this);
    }
}
