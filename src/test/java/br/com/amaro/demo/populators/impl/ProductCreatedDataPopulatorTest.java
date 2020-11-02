package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.ProductCreatedData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.strategy.IndexOfTagsStrategy;
import br.com.amaro.demo.strategy.ProductVectorStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ProductCreatedDataPopulator.class)
public class ProductCreatedDataPopulatorTest {
    @InjectMocks private ProductCreatedDataPopulator productCreatedDataPopulator;

    @Mock private IndexOfTagsStrategy indexOfTagsStrategy;
    @Mock private ProductVectorStrategy productVectorStrategy;

    private final Product product = new Product();
    private final int[] vectors = {1};

    @Before
    public void init() {
        final Tag tag = new Tag();
        tag.setId(1);
        tag.setName("metal");

        final List<Tag> tagList = Collections.singletonList(tag);
        final List<Integer> indexes = Collections.singletonList(1);

        this.product.setName("Product name");
        this.product.setExternalId(1);
        this.product.setUid("1111-1111-1111");
        this.product.setTags(tagList);

        when(this.indexOfTagsStrategy.getIndexOfTags(tagList)).thenReturn(indexes);
        when(this.productVectorStrategy.getVectors(indexes)).thenReturn(this.vectors);
    }

    @Test
    public void populate() {
        final ProductCreatedData response = this.productCreatedDataPopulator.populate(this.product);
        Assert.assertEquals(response.getId(), product.getExternalId());
        Assert.assertEquals(response.getName(), product.getName());
        Assert.assertEquals(response.getTags().size(), product.getTags().size());
        Assert.assertEquals(response.getTagsVector(), this.vectors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_IllegalArgumentException() {
        this.productCreatedDataPopulator.populate(null);
    }
}