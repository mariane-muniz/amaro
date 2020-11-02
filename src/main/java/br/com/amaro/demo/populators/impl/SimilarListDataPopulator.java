package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.ProductData;
import br.com.amaro.demo.dtos.SimilarListProductData;
import br.com.amaro.demo.dtos.SimilarProductData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.SimilarProduct;
import br.com.amaro.demo.forms.SearchSimilarProductForm;
import br.com.amaro.demo.parameters.SimilarProductParameter;
import br.com.amaro.demo.populators.Populator;
import br.com.amaro.demo.services.ProductService;
import br.com.amaro.demo.services.ProductSimilarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for converting the product parameter into the similar product response object created.
 * This class uses the standard implementation of the populator that registers the basic information of the product.
 *
 * @see SearchSimilarProductForm
 * @see SimilarListProductData
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SimilarListDataPopulator implements Populator<SearchSimilarProductForm, SimilarListProductData> {
    private final ProductSimilarService productSimilarService;
    private final ProductService productService;
    private final SimilarProductDataPopulator similarProductDataPopulator;
    private final ProductDataPopulator productDataPopulator;

    /**
     * Main method of populator implementation.
     * Through this implementation the object to be converted is received and directed to the additional methods to make
     * the data conversion and in sequence return the new object that was populated.
     *
     * @param searchSimilarProductForm the parameter with complex product information (entity, similarity)
     * @return the product object data with basic product information and similarity between original
     *        product and the product in the DTO.
     */
    @Override
    public SimilarListProductData populate(final SearchSimilarProductForm searchSimilarProductForm) {
        Assert.notNull(searchSimilarProductForm, "searchSimilarProductForm cannot be null");

        final SimilarListProductData similarProductData = new SimilarListProductData();
        final Integer productExternalId = searchSimilarProductForm.getId();
        final Product product = this.productService.findByExternalId(productExternalId);
        final ProductData productData = this.productDataPopulator.populate(product);
        final List<SimilarProductData> similarProductList = this.getSimilarProductList(product);

        BeanUtils.copyProperties(productData, similarProductData);
        similarProductData.setSimilarProducts(similarProductList);
        return similarProductData;
    }

    /**
     * Method responsible for searching similar products from the main product and return a list with the DTOs of similar
     * products
     *
     * @param product the entity database fo product
     * @return a list similar products
     */
    private List<SimilarProductData> getSimilarProductList(final Product product) {
        Assert.notNull(product, "parameter cannot be null");

        final String productUid = product.getUid();
        final List<SimilarProduct> similarities = this.productSimilarService.getSimilarProducts(productUid);

        if (!CollectionUtils.isEmpty(similarities)) {

            return similarities.parallelStream()
                    .map(similar -> this.getSimilarProductData(similar, productUid))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Method responsible for creating product similarities through a service tree
     *
     * @param similar database product entity for comparision
     * @param originalProductUid original product UID
     * @return the product object data with basic product information and similarity between original
     *        product and the product in the DTO.
     */
    private SimilarProductData getSimilarProductData(
            final SimilarProduct similar,
            final String originalProductUid
    ) {
        Assert.notNull(similar, "similar cannot be null");
        Assert.notNull(originalProductUid, "originalProductUid cannot be null");

        final String token = similar.getToken();
        final Double similarity = similar.getSimilarity();
        final String similarProductUid = token.replace(originalProductUid, "").replace("--","");
        final Product similarProduct = this.productService.findByUid(similarProductUid);
        final SimilarProductParameter parameter = new SimilarProductParameter();

        parameter.setSimilarProduct(similarProduct);
        parameter.setSimilarity(similarity);

        return this.similarProductDataPopulator.populate(parameter);
    }
}