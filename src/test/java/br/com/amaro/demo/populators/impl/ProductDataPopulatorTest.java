package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.ProductData;
import br.com.amaro.demo.entities.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ProductDataPopulator.class)
public class ProductDataPopulatorTest {
    @InjectMocks private ProductDataPopulator productDataPopulator;

    private final Product product = new Product();

    @Before
    public void init() {
        this.product.setName("Product name");
        this.product.setExternalId(1);
        this.product.setUid("1111-1111-1111");
    }

    @Test
    public void populate() {
        final ProductData response = this.productDataPopulator.populate(this.product);
        Assert.assertEquals(response.getId(), this.product.getExternalId());
        Assert.assertEquals(response.getName(), this.product.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_IllegalArgumentException() {
        this.productDataPopulator.populate(null);
    }
}