package lynx.product_service.product.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lynx.product_service.common.BaseResponse;
import lynx.product_service.product.model.dto.ProductQuery;
import lynx.product_service.product.model.dto.request.CreateProductRequest;
import lynx.product_service.product.model.entity.ProductEntity;
import lynx.product_service.product.service.ProductService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<String>> createProduct(@RequestBody CreateProductRequest request) {
        var savedProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.created(savedProduct.getId()));
    }

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getProducts(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String style,
            @RequestParam(required = false) String priceMin,
            @RequestParam(required = false) String priceMax,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit) {

        ProductQuery productQuery = ProductQuery.builder()
                .categoryId(categoryId)
                .brand(brand)
                .color(color)
                .size(size)
                .material(material)
                .style(style)
                .priceMin(Optional.ofNullable(priceMin).map(Double::valueOf).orElse(null))
                .priceMax(Optional.ofNullable(priceMax).map(Double::valueOf).orElse(null))
                .page(page)
                .limit(limit)
                .build();

        List<ProductEntity> products = productService.getProductsWithQueryParams(productQuery);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductEntity>> getProductsByCategory(@PathVariable String categoryId) {
        List<ProductEntity> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<ProductEntity>> getProductsByBrand(@PathVariable String brand) {
        List<ProductEntity> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable String productId) {
        Optional<ProductEntity> product = productService.getProductById(productId);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseResponse<String>> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(BaseResponse.success("Product deleted successfully"));
    }
}
