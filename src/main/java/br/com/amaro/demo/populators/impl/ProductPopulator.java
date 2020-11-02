package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.populators.Populator;
import br.com.amaro.demo.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Class responsible for converting the product parameter in the product entity object
 *
 * @see Product
 * @see ProductRegisterForm
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ProductPopulator implements Populator<ProductRegisterForm, Product> {
    private final TagService tagService;

    /**
     * Main method of populator implementation.
     * Through this implementation the object to be converted is received and directed to the additional methods to make
     * the data conversion and in sequence return the new object that was populated.
     *
     * @param productRegisterForm the product parameter with product basic information (name and id)
     * @return the product entity object
     */
    @Override
    public Product populate(final ProductRegisterForm productRegisterForm) {
        Assert.notNull(productRegisterForm, "productParameter cannot be null");

        final Product product = new Product();
        final List<Tag> tags = this.getTags(productRegisterForm);

        product.setUid(UUID.randomUUID().toString());
        product.setExternalId(productRegisterForm.getId());
        product.setName(productRegisterForm.getName());
        product.setTags(tags);

        return product;
    }

    /**
     * Internal method responsible for locating tag entities based on a list of tag names.
     *
     * @param productRegisterForm the product parameter with product basic information (name and id)
     * @return list of tag entities
     */
    private List<Tag> getTags(final ProductRegisterForm productRegisterForm) {
        Assert.notNull(productRegisterForm, "productParameter cannot be null");

        final List<String> tagNames = productRegisterForm.getTags();
        return this.tagService.getByNames(tagNames);
    }
}