package lynx.product_service.product.repos.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import lynx.product_service.exception.GeneralException;
import lynx.product_service.product.model.dto.ProductQuery;
import lynx.product_service.product.model.entity.ProductEntity;

@ExtendWith(MockitoExtension.class)
class ProductESSearchRepoCustomImplTest {

    @Mock
    private ElasticsearchClient elasticsearchClient;

    @Mock
    private SearchResponse<ProductEntity> searchResponse;

    @Mock
    private HitsMetadata<ProductEntity> hitsMetadata;

    @Captor
    private ArgumentCaptor<SearchRequest> searchRequestCaptor;

    private ProductESSearchRepoCustomImpl productESSearchRepo;

    @BeforeEach
    void setUp() {
        productESSearchRepo = new ProductESSearchRepoCustomImpl(elasticsearchClient);
    }

    @Test
    void search_ShouldReturnProductList_WhenAllParametersProvided() throws IOException {
        // Arrange
        ProductQuery query = ProductQuery.builder()
                .categoryId("category1")
                .brand("nike")
                .color("red")
                .size("XL")
                .material("cotton")
                .style("casual")
                .priceMin(10.0)
                .priceMax(100.0)
                .page(0)
                .limit(10)
                .build();

        ProductEntity product1 = new ProductEntity();
        product1.setId("1");
        product1.setName("Product 1");
        
        ProductEntity product2 = new ProductEntity();
        product2.setId("2");
        product2.setName("Product 2");
        
        List<Hit<ProductEntity>> hits = Arrays.asList(
                createHit(product1),
                createHit(product2)
        );
        
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductEntity.class)))
                .thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(hits);
        
        // Act
        List<ProductEntity> result = productESSearchRepo.search(query);
        
        // Assert
        verify(elasticsearchClient).search(searchRequestCaptor.capture(), eq(ProductEntity.class));
        SearchRequest capturedRequest = searchRequestCaptor.getValue();
        
        assertThat(capturedRequest.index()).containsExactly("product_db.public.products");
        assertThat(capturedRequest.from()).isEqualTo(0);
        assertThat(capturedRequest.size()).isEqualTo(10);
        
        // Verify query structure - this is a simplified check
        Query query1 = capturedRequest.query();
        assertThat(query1).isNotNull();
        
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(1).getId()).isEqualTo("2");
    }

    @Test
    void search_ShouldReturnProductList_WhenOnlyCategoryProvided() throws IOException {
        // Arrange
        ProductQuery query = ProductQuery.builder()
                .categoryId("category1")
                .page(0)
                .limit(10)
                .build();

        ProductEntity product = new ProductEntity();
        product.setId("1");
        product.setName("Product 1");
        
        List<Hit<ProductEntity>> hits = List.of(createHit(product));
        
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductEntity.class)))
                .thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(hits);
        
        // Act
        List<ProductEntity> result = productESSearchRepo.search(query);
        
        // Assert
        verify(elasticsearchClient).search(searchRequestCaptor.capture(), eq(ProductEntity.class));
        SearchRequest capturedRequest = searchRequestCaptor.getValue();
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("1");
    }

    @Test
    void search_ShouldReturnProductList_WhenOnlyPriceRangeProvided() throws IOException {
        // Arrange
        ProductQuery query = ProductQuery.builder()
                .priceMin(10.0)
                .priceMax(100.0)
                .page(0)
                .limit(10)
                .build();

        ProductEntity product = new ProductEntity();
        product.setId("1");
        product.setName("Product 1");
        
        List<Hit<ProductEntity>> hits = List.of(createHit(product));
        
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductEntity.class)))
                .thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(hits);
        
        // Act
        List<ProductEntity> result = productESSearchRepo.search(query);
        
        // Assert
        verify(elasticsearchClient).search(searchRequestCaptor.capture(), eq(ProductEntity.class));
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("1");
    }

    @Test
    void search_ShouldReturnProductList_WhenOnlyPriceMinProvided() throws IOException {
        // Arrange
        ProductQuery query = ProductQuery.builder()
                .priceMin(10.0)
                .page(0)
                .limit(10)
                .build();

        ProductEntity product = new ProductEntity();
        product.setId("1");
        product.setName("Product 1");
        
        List<Hit<ProductEntity>> hits = List.of(createHit(product));
        
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductEntity.class)))
                .thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(hits);
        
        // Act
        List<ProductEntity> result = productESSearchRepo.search(query);
        
        // Assert
        verify(elasticsearchClient).search(any(SearchRequest.class), eq(ProductEntity.class));
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("1");
    }

    @Test
    void search_ShouldReturnProductList_WhenOnlyPriceMaxProvided() throws IOException {
        // Arrange
        ProductQuery query = ProductQuery.builder()
                .priceMax(100.0)
                .page(0)
                .limit(10)
                .build();

        ProductEntity product = new ProductEntity();
        product.setId("1");
        product.setName("Product 1");
        
        List<Hit<ProductEntity>> hits = List.of(createHit(product));
        
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductEntity.class)))
                .thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(hits);
        
        // Act
        List<ProductEntity> result = productESSearchRepo.search(query);
        
        // Assert
        verify(elasticsearchClient).search(any(SearchRequest.class), eq(ProductEntity.class));
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("1");
    }

    @Test
    void search_ShouldThrowGeneralException_WhenElasticsearchClientThrowsException() throws IOException {
        // Arrange
        ProductQuery query = ProductQuery.builder()
                .categoryId("category1")
                .page(0)
                .limit(10)
                .build();
        
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductEntity.class)))
                .thenThrow(new IOException("Test exception"));
        
        // Act & Assert
        assertThrows(GeneralException.class, () -> productESSearchRepo.search(query));
    }

    @Test
    void search_ShouldReturnEmptyList_WhenNoHitsReturned() throws IOException {
        // Arrange
        ProductQuery query = ProductQuery.builder()
                .categoryId("category1")
                .page(0)
                .limit(10)
                .build();
        
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductEntity.class)))
                .thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(List.of());
        
        // Act
        List<ProductEntity> result = productESSearchRepo.search(query);
        
        // Assert
        verify(elasticsearchClient).search(any(SearchRequest.class), eq(ProductEntity.class));
        
        assertThat(result).isEmpty();
    }

    @Test
    void search_ShouldWorkCorrectly_WhenNoFilterParametersProvided() throws IOException {
        // Arrange
        ProductQuery query = ProductQuery.builder()
                .page(0)
                .limit(10)
                .build();

        ProductEntity product = new ProductEntity();
        product.setId("1");
        product.setName("Product 1");
        
        List<Hit<ProductEntity>> hits = List.of(createHit(product));
        
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductEntity.class)))
                .thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(hits);
        
        // Act
        List<ProductEntity> result = productESSearchRepo.search(query);
        
        // Assert
        verify(elasticsearchClient).search(searchRequestCaptor.capture(), eq(ProductEntity.class));
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("1");
    }
    
    // Helper method to create a Hit with the given ProductEntity
    @SuppressWarnings("unchecked")
    private Hit<ProductEntity> createHit(ProductEntity product) {
        Hit<ProductEntity> hit = mock(Hit.class);
        when(hit.source()).thenReturn(product);
        return hit;
    }
} 