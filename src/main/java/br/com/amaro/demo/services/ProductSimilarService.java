package br.com.amaro.demo.services;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.SimilarProduct;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.exceptions.SimilaritySizeComparisonException;
import br.com.amaro.demo.repositories.ProductRepository;
import br.com.amaro.demo.repositories.SimilarProductRepository;
import br.com.amaro.demo.strategy.IndexOfTagsStrategy;
import br.com.amaro.demo.strategy.ProductVectorStrategy;
import br.com.amaro.demo.strategy.SimilarityStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Service responsible for operations with the similar product objects
 *
 * @see SimilarProductRepository
 * @see SimilarProduct
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation= Propagation.REQUIRED, noRollbackFor=Exception.class)
public class ProductSimilarService {
    @Value("${product.similar.quantity}")
    private Integer productSimilarQty;
    private final SimilarProductRepository similarProductRepository;
    private final ProductRepository productRepository;
    private final SimilarityStrategy similarityStrategy;
    private final ProductVectorStrategy productVectorStrategy;
    private final IndexOfTagsStrategy indexOfTagsStrategy;

    /**
     * Method responsible for recovering the similarities of products from the uid of the product.
     * To create a similarity of products was considered the concept of bidirectional, the distance from A to B is the
     * same as B to A. The link between both products is the token that has the UID of both products, which allows this
     * field to be used for independent search of the original product.
     *
     * @see Pageable
     *
     * @param productUid is the UID of the product entity
     * @return a list of similar product entities
     */
    public List<SimilarProduct> getSimilarProducts(final String productUid) {
        Assert.notNull(productUid, "productId cannot be null");

        final Pageable pageable = PageRequest.of(0, this.productSimilarQty);
        return this.similarProductRepository.findByToken(productUid, pageable);
    }

    /**
     * The method responsible for generating the similarities of a given product to all the products in the database
     *
     * @see Product
     *
     * @param newProduct the product database entity
     */
    public void generateSimilarity(final Product newProduct) {
        Assert.notNull(newProduct, "productId cannot be null");
        final List<SimilarProduct> similarProductList = new ArrayList<>();

        /* The similarity generation process starts by taking all the products from the base and comparing
        them with the original product. */
        this.productRepository.findAll().forEach(product -> {

            if(!product.getId().equals(newProduct.getId())) {
                final String newProductUid = product.getUid();
                final String productUid = newProduct.getUid();
                final Pageable pageable = PageRequest.of(0, 1);
                List<SimilarProduct> existentSimilarity = this.similarProductRepository
                        .findByTokens(newProductUid, productUid, pageable);

                /* If there is no similarity between the original product and the product redeemed from the base,
                start the calculation process */
                if (CollectionUtils.isEmpty(existentSimilarity)) {
                    final Double similarity = this.calculateSimilarity(newProduct, product);

                    /* If the similarity value is valid, a similarity record object is generated and added to the list
                    of similarity objects. */
                    if (Objects.nonNull(similarity)) {
                        final SimilarProduct model = new SimilarProduct();
                        final String token = newProduct.getUid() + "--" + product.getUid();

                        model.setSimilarity(similarity);
                        model.setToken(token);
                        similarProductList.add(model);
                    }
                }
            }
        });

        /* If the list of new similarity objects is not empty, the new objects are sent to the database. */
        if (!CollectionUtils.isEmpty(similarProductList)) {
            this.similarProductRepository.saveAll(similarProductList);
        }
    }

    /**
     * This method is responsible for calculating the similarity between two products that are already
     * registered in the base
     *
     * @see Product
     * @see IndexOfTagsStrategy
     * @see ProductVectorStrategy
     *
     * @param newProduct the product database entity
     * @param product the product database entity
     * @return the value of similarity between products
     */
    public Double calculateSimilarity(final Product newProduct, final Product product) {
        Assert.notNull(newProduct, "newProduct cannot be null");
        Assert.notNull(product, "product cannot be null");
        final List<Tag> newProductTags = newProduct.getTags();
        final List<Tag> productTags = product.getTags();

        if (!CollectionUtils.isEmpty(newProductTags) && !CollectionUtils.isEmpty(productTags)) {
            /* Create a list with the indices of the vectors from the list of product tags */
            final List<Integer> newProductIndex = this.indexOfTagsStrategy.getIndexOfTags(newProductTags);
            final List<Integer> productIndex = this.indexOfTagsStrategy.getIndexOfTags(productTags);

            /* Create a list of vectors from the indexes of the tags */
            final int[] newProductVector = this.productVectorStrategy.getVectors(newProductIndex);
            final int[] productVector = this.productVectorStrategy.getVectors(productIndex);

            try {
                return this.similarityStrategy.calculate(newProductVector, productVector);
            }
            catch (SimilaritySizeComparisonException e) {
                log.error(e.getMessage());
                log.error("its not possible to calculate similarity between products :"
                        + newProduct.getId() + " and " + product.getId());
            }
        }
        return null;
    }
}