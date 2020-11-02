package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.ProductData;
import br.com.amaro.demo.dtos.SimilarProductData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.parameters.SimilarProductParameter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = SimilarProductDataPopulator.class)
public class SimilarProductDataPopulatorTest {
    @InjectMocks private SimilarProductDataPopulator similarProductDataPopulator;

    @Mock private ProductDataPopulator productDataPopulator;

    private final Product product = new Product();
    private final SimilarProductParameter similarProductParameter = new SimilarProductParameter();

    @Before
    public void init() {
        final Tag tag = new Tag();
        final ProductData productData = new ProductData();

        tag.setId(1);
        tag.setName("metal");

        product.setId(1);
        product.setExternalId(2);
        product.setUid("1111");
        product.setName("Product name");
        product.setWithSimilarity(true);
        product.setTags(Collections.singletonList(tag));

        productData.setId(2);
        productData.setName("Product name");

        similarProductParameter.setSimilarProduct(product);
        similarProductParameter.setSimilarity(1.0);

        when(this.productDataPopulator.populate(any())).thenReturn(productData);
    }

    @Test
    public void populate() {
        final SimilarProductData response = this.similarProductDataPopulator.populate(this.similarProductParameter);

        Assert.assertEquals(response.getId(), this.product.getExternalId());
        Assert.assertEquals(response.getName(), this.product.getName());
        Assert.assertEquals(response.getSimilarity(), this.similarProductParameter.getSimilarity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_IllegalArgumentException() {
        this.similarProductDataPopulator.populate(null);
    }
}