package lynx.product_service.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lynx.product_service.exception.CustomNotFoundException;
import lynx.product_service.product.mapper.ProductMapper;
import lynx.product_service.product.model.dto.ProductQuery;
import lynx.product_service.product.model.dto.request.CreateProductRequest;
import lynx.product_service.product.model.entity.ProductEntity;
import lynx.product_service.product.repos.elasticsearch.ProductESSearchRepoCustomImpl;
import lynx.product_service.product.repos.jpa.ProductJPARepo;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private final ProductJPARepo productRepository;
    private final ProductESSearchRepoCustomImpl productESSearchRepoCustom;
    private final ProductMapper productMapper;
    /**
     * Get products based on the provided query parameters
     * 
     * @param query the query parameters
     * @return list of matching products
     */
    public List<ProductEntity> getProductsWithQueryParams(ProductQuery query) {
        return productESSearchRepoCustom.search(query);
    }

    /**
     * Get a product by its ID
     * 
     * @param productId the ID of the product to retrieve
     * @return the product wrapped in an Optional
     */
    public Optional<ProductEntity> getProductById(String productId) {
        return productRepository.findById(productId);
    }
    
    /**
     * Get products by category ID
     * 
     * @param categoryId the category ID
     * @return list of products in the category
     */
    public List<ProductEntity> getProductsByCategory(String categoryId) {
        return new ArrayList<>();
    }
    
    /**
     * Get products by brand
     * 
     * @param brand the brand name
     * @return list of products of the specified brand
     */
    public List<ProductEntity> getProductsByBrand(String brand) {
        return new ArrayList<>();
    }

    /**
     * Create a new product
     * 
     * @param request the request to create a product
     */
    @Transactional
    public ProductEntity createProduct(CreateProductRequest request) {
        ProductEntity product = request.toEntity(productMapper);
        return productRepository.save(product);
    }

    /**
     * Delete a product by its ID
     * 
     * @param productId the ID of the product to delete
     */
    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new CustomNotFoundException("Product not found");
        }
        productRepository.deleteById(productId);
    }
}