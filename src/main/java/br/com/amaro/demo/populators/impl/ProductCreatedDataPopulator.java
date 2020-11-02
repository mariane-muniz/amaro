package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.ProductCreatedData;
import br.com.amaro.demo.dtos.ProductData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.strategy.IndexOfTagsStrategy;
import br.com.amaro.demo.strategy.ProductVectorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for converting the product object into the product response object created.
 * This class uses the standard implementation of the populator that registers the basic information of the product.
 *
 * @see Product
 * @see ProductCreatedData
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ProductCreatedDataPopulator extends ProductDataPopulator {
    private final IndexOfTagsStrategy indexOfTagsStrategy;
    private final ProductVectorStrategy productVectorStrategy;

    /**
     * Main method of populator implementation.
     * Through this implementation the object to be converted is received and directed to the additional methods to make
     * the data conversion and in sequence return the new object that was populated.
     *
     * @param product the logical representation of product object
     * @return the product object data with vector information
     */
    @Override
    public ProductCreatedData populate(final Product product) {
        Assert.notNull(product, "parameter cannot be null");

        final ProductData productData = super.populate(product);
        final ProductCreatedData productCreatedData = new ProductCreatedData();
        final List<Tag> tags = product.getTags();

        BeanUtils.copyProperties(productData, productCreatedData);
        this.populateTags(tags, productCreatedData);
        this.populateVectors(tags, productCreatedData);

        return productCreatedData;
    }

    /**
     * Internal method responsible for converting a list of tag objects {@link Tag} to a list of tag names.
     *
     * @param tags list of tags that are linked to the product
     * @param productCreatedData object of product exposure after the registration process.
     */
    private void populateTags(final List<Tag> tags, final ProductCreatedData productCreatedData) {
        productCreatedData.setTags(new ArrayList<>());
        tags.forEach(tag -> productCreatedData.getTags().add(tag.getName()));
    }

    /**
     *Method responsible for retrieving from the product tags the vectors of the product tags and adding
     * to the output object.
     *
     * @param tags list of tags that are linked to the product
     * @param productCreatedData object of product exposure after the registration process.
     */
    private void populateVectors(final List<Tag> tags, final ProductCreatedData productCreatedData) {
        List<Integer> indexes = this.indexOfTagsStrategy.getIndexOfTags(tags);
        final int[] vectors = this.productVectorStrategy.getVectors(indexes);
        productCreatedData.setTagsVector(vectors);
    }
}