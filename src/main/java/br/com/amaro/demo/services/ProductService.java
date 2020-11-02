package br.com.amaro.demo.services;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Service responsible for operations with the product objects
 *
 * @see ProductRepository
 * @see Product
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation= Propagation.REQUIRED, noRollbackFor=Exception.class)
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * Method responsible for returning products from the database that did not have the similarity calculated
     *
     * @param pageable number of records to be returned
     * @return list of similar products found
     */
    public List<Product> findWithoutSimilarity(final Pageable pageable) {
        Assert.notNull(pageable, "pageable cannot be null");
        return this.productRepository.findWithoutSimilarity(pageable);
    }

    /**
     * Method responsible for recovery product from the database from the product external identifier
     *
     * @param externalId the identifier of the product that is sent by the customer
     * @return the product registered in the base with the external identification
     */
    public Product findByExternalId(final Integer externalId) {
        Assert.notNull(externalId, "externalId cannot be empty");
        Optional<Product> response = this.productRepository.findByExternalId(externalId);
        return response.orElse(null);
    }

    /**
     * Method responsible for recovery products from the database from the product's external identifier
     *
     * @param externalIds the identifiers of the product that is sent by the customer
     * @return the products registered in the base with the external identification
     */
    public List<Product> findByExternalIdIn(final List<Integer> externalIds) {
        Assert.notEmpty(externalIds, "externalIds cannot be empty");
        return this.productRepository.findByExternalIdIn(externalIds);
    }

    /**
     * Method responsible for recovery product from the database from the product uid
     *
     * @param uid product unique identifier
     * @return the product registered in the database with uid
     */
    public Product findByUid(final String uid) {
        Assert.notNull(uid, "uid cannot be empty");
        return this.productRepository.findByUid(uid).orElse(null);
    }

    /**
     * It is the method responsible for persisting the product entity in the database.
     *
     * @see ProductRepository
     * @see Product
     *
     * @param productList the new product entities
     * @return the product entities registered in the database
     */
    public List<Product> persistInDatabase(final List<Product> productList) {
        Assert.notEmpty(productList,"product cannot be null");
        final List<Product> response = new ArrayList<>();
        final Iterable<Product> iterable = this.productRepository.saveAll(productList);
        iterable.forEach(response::add);

        return response;
    }
}