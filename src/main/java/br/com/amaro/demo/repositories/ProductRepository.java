package br.com.amaro.demo.repositories;

import br.com.amaro.demo.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *  Repository for the database transactions of product entities
 *
 * @see Product
 *
 * @author Marine Muniz
 * @version 1.0.0
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    Optional<Product> findByExternalId(final Integer externalId);
    List<Product> findByExternalIdIn(final List<Integer> externalIds);
    Optional<Product> findByUid(final String uid);

    /**
     * Method responsible for rescuing the products that did not have the similarity calculated
     *
     * @param pageable number of records to be returned
     * @return list with products that did not have the similarity calculated
     */
    @Query(value = "SELECT p FROM Product p WHERE p.withSimilarity = false ORDER BY p.id DESC")
    List<Product> findWithoutSimilarity(final Pageable pageable);
}