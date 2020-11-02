package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.ProductData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.populators.Populator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Class responsible for converting the product object into the basic product response object.
 *
 * @see Product
 * @see ProductData
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
public class ProductDataPopulator implements Populator<Product, ProductData> {

    /**
     * Main method of populator implementation.
     * Through this implementation the object to be converted is received and directed to the additional methods to make
     * the data conversion and in sequence return the new object that was populated.
     *
     * @see Product
     * @see ProductData
     *
     * @param product the logical representation of product object
     * @return the basic product object
     */
    @Override
    public ProductData populate(final Product product) {
        Assert.notNull(product, "product cannot be null");
        final ProductData data = new ProductData();

        data.setId(product.getExternalId());
        data.setName(product.getName());

        return data;
    }
}