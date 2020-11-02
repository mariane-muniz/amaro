package br.com.amaro.demo.validators;

import br.com.amaro.demo.dtos.ProductData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.forms.SearchSimilarProductForm;
import br.com.amaro.demo.forms.SearchSimilarProductListForm;
import br.com.amaro.demo.services.ProductService;
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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = SearchSimilarProductListFormValidator.class)
public class SearchSimilarProductListFormValidatorTest {
    @InjectMocks private SearchSimilarProductListFormValidator searchSimilarProductListFormValidator;

    @Mock private TagService tagService;
    @Mock private ProductService productService;

    private final Tag tag = new Tag();
    private final SearchSimilarProductListForm searchSimilarProductListForm = new SearchSimilarProductListForm();
    private BeanPropertyBindingResult bindingResult;

    @Before
    public void init() {
        final Product product = new Product();
        final SearchSimilarProductForm searchSimilarProductForm = new SearchSimilarProductForm();
        this.bindingResult = new BeanPropertyBindingResult(this.searchSimilarProductListForm, "productRegisterListForm");

        tag.setId(1);
        tag.setName("metal");

        product.setId(1);
        product.setExternalId(1);
        product.setName("Product name");
        product.setUid("1111-1111");
        product.setTags(Collections.singletonList(tag));

        searchSimilarProductForm.setId(1);
        searchSimilarProductForm.setName("Product name");
        searchSimilarProductForm.setTags(Collections.singletonList("metal"));
        searchSimilarProductForm.setTagsVector(new int[]{0,1,0,1});

        Mockito
                .when(this.productService.findByExternalId(1))
                .thenReturn(product);

        this.searchSimilarProductListForm.setSimilar(Collections.singletonList(searchSimilarProductForm));
    }

    @Test
    public void validateNotRegisteredIds() {
        Mockito
                .when(this.productService.findByExternalId(1))
                .thenReturn(null);

        this.searchSimilarProductListFormValidator.validate(this.searchSimilarProductListForm, bindingResult);
        final FieldError response = bindingResult.getFieldError(ProductRegisterForm.ID);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getArguments());
    }

    @Test
    public void validateTagsVectorCount() {
        Mockito.when(this.tagService.findAll()).thenReturn(Collections.singleton(this.tag));

        this.searchSimilarProductListFormValidator.validate(this.searchSimilarProductListForm, bindingResult);
        final FieldError response = bindingResult.getFieldError(SearchSimilarProductForm.TAGS_VECTOR);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getArguments());
    }

    @Test
    public void supports() {
        final boolean response = this.searchSimilarProductListFormValidator.supports(SearchSimilarProductListForm.class);
        Assert.assertTrue(response);
    }

    @Test
    public void supports_InvalidObject() {
        final boolean response = this.searchSimilarProductListFormValidator.supports(ProductData.class);
        Assert.assertFalse(response);
    }
}