package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.services.TagService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ProductPopulator.class)
public class ProductPopulatorTest {
    @InjectMocks private ProductPopulator productPopulator;

    @Mock private TagService tagService;

    private final ProductRegisterForm productRegisterForm = new ProductRegisterForm();

    @Before
    public void init() {
        final Tag tag = new Tag();
        final List<String> tagNameList = new ArrayList<>();
        final List<Tag> tagList = new ArrayList<>();

        productRegisterForm.setId(1);
        productRegisterForm.setName("Product name");
        productRegisterForm.setTags(tagNameList);

        tag.setId(1);
        tag.setName("metal");

        tagList.add(tag);
        tagNameList.add("metal");

        Mockito.when(this.tagService.getByNames(tagNameList)).thenReturn(tagList);
    }

    @Test
    public void populate() {

        final Product response = this.productPopulator.populate(productRegisterForm);

        Assert.assertEquals(productRegisterForm.getId(), response.getExternalId());
        Assert.assertEquals(productRegisterForm.getName(), response.getName());
        Assert.assertEquals(productRegisterForm.getTags().iterator().next(), response.getTags().iterator().next().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_IllegalArgumentException() {
        this.productPopulator.populate(null);
    }
}