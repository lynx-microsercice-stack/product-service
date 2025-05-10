package lynx.product_service.product.repos.elasticsearch;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lynx.product_service.exception.GeneralException;
import lynx.product_service.product.model.entity.ProductEntity;
import lynx.product_service.product.service.ProductQuery;

@Repository
@AllArgsConstructor
@Slf4j
public class ProductESSearchRepoCustomImpl implements ProductESSearchRepoCustom {

    private static final String PRODUCTS_INDEX = "product_db.public.products";

    private final ElasticsearchClient elasticsearchClient;

    private SearchRequest createSearchRequest(Query query, int page, int limit) {
        return SearchRequest.of(s -> s
                .index(PRODUCTS_INDEX)
                .query(query)
                .from(page * limit)
                .size(limit));
    }

    /**
     * Creates a search query based on the query parameters.
     * 
     * @param queryParams the query parameters
     * @return the search query
     */
    private Query createSearchQuery(ProductQuery queryParams) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // Map of field names to their values from queryParams
        Map<String, Function<ProductQuery, String>> termQueries = Map.of(
                "categoryId", ProductQuery::getCategoryId,
                "brand", ProductQuery::getBrand,
                "color", ProductQuery::getColor,
                "size", ProductQuery::getSize,
                "material", ProductQuery::getMaterial,
                "style", ProductQuery::getStyle);

        // Add term queries for non-null and non-empty fields
        termQueries.forEach((field, getter) -> {
            String value = getter.apply(queryParams);
            if (value != null && !value.isEmpty()) {
                boolQueryBuilder.must(createTermQuery(field, value));
            }
        });

        // Add price range query if applicable
        if (queryParams.getPriceMin() != null || queryParams.getPriceMax() != null) {
            boolQueryBuilder.must(createPriceRangeQuery(queryParams));
        }

        // add ignore deleted products
        boolQueryBuilder.must(createTermQuery("__deleted", "false"));

        return Query.of(q -> q.bool(boolQueryBuilder.build()));
    }

    /**
     * Creates a term query for a given field and value.
     * 
     * @param field the field to query
     * @param value the value to query
     * @return the term query
     */
    private Query createTermQuery(String field, String value) {
        return Query.of(q -> q.term(TermQuery.of(t -> t.field(field).value(value))));
    }

    /**
     * Creates a range query for the price field based on the query parameters.
     * 
     * @param queryParams the query parameters
     * @return the range query
     */
    private Query createPriceRangeQuery(ProductQuery queryParams) {
        RangeQuery rangeQuery = RangeQuery.of(rq -> rq.number(nrq -> nrq.field("price")
                .gte(Optional.ofNullable(queryParams.getPriceMin()).orElse(0.0))
                .lte(Optional.ofNullable(queryParams.getPriceMax()).orElse(999999999.0))));

        return Query.of(q -> q.range(rangeQuery));
    }

    @Override
    public List<ProductEntity> search(ProductQuery queryParams) {
        try {
            Query query = createSearchQuery(queryParams);
            SearchRequest searchRequest = createSearchRequest(query, queryParams.getPage(), queryParams.getLimit());
            SearchResponse<ProductEntity> response = elasticsearchClient.search(searchRequest, ProductEntity.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error executing Elasticsearch query", e);
            throw new GeneralException(e.getMessage());
        }
    }
}