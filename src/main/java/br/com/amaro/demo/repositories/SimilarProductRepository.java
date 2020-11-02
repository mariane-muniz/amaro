package br.com.amaro.demo.repositories;

import br.com.amaro.demo.entities.SimilarProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for the database transactions of similar product entities
 *
 * @see SimilarProduct
 *
 * @author Marine Muniz
 * @version 1.0.0
 */
@Repository
public interface SimilarProductRepository extends CrudRepository<SimilarProduct, Integer> {

    /**
     * Method responsible for searching similar products in token using the UID product.
     *
     * @see Pageable
     * @see SimilarProduct
     *
     * @param pageable the limit resources object
     * @param productUid the unique product identifier
     * @return the database entity of product
     */
    @Query(value = "SELECT sp FROM SimilarProduct sp WHERE sp.token LIKE %:productUid% ORDER BY sp.similarity DESC")
    List<SimilarProduct> findByToken(final String productUid, final Pageable pageable);

    /**
     * Responsible method in returning the similarity between two products
     *
     * @param newProductUid uid of the new producer
     * @param productUid uid of the product already registered in the database
     * @param pageable quantity of products to be returned
     * @return list of similar products
     */
    @Query(value = "SELECT sp FROM SimilarProduct sp WHERE sp.token LIKE %:newProductUid% AND sp.token LIKE %:productUid% ORDER BY sp.similarity DESC")
    List<SimilarProduct> findByTokens(final String newProductUid, final String productUid, final Pageable pageable);
}