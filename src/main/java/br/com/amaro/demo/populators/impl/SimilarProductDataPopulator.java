package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.ProductData;
import br.com.amaro.demo.dtos.SimilarProductData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.parameters.SimilarProductParameter;
import br.com.amaro.demo.populators.Populator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Class responsible for generating an object with the product basic information and the similarity value of the
 * original product of the search
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SimilarProductDataPopulator implements Populator<SimilarProductParameter, SimilarProductData> {
    private final ProductDataPopulator productDataPopulator;

    /**
     *
     * @param similarProductParameter object with the similar product and the value of similarity
     * @return object with the product basic information and the similarity value
     */
    @Override
    public SimilarProductData populate(final SimilarProductParameter similarProductParameter) {
        Assert.notNull(similarProductParameter, "similarProductParameter cannot be null");
        final SimilarProductData data = new SimilarProductData();

        final Double similarity = similarProductParameter.getSimilarity();
        final Product product = similarProductParameter.getSimilarProduct();
        final ProductData productData = this.productDataPopulator.populate(product);

        BeanUtils.copyProperties(productData, data);
        data.setSimilarity(similarity);

        return data;
    }
}