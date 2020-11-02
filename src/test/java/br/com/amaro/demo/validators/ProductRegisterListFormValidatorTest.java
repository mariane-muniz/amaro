package br.com.amaro.demo.validators;

import br.com.amaro.demo.dtos.ProductData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.forms.ProductRegisterListForm;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ProductRegisterListFormValidator.class)
public class ProductRegisterListFormValidatorTest {
    @InjectMocks ProductRegisterListFormValidator productRegisterListFormValidator;

    @Mock private TagService tagService;
    @Mock private ProductService productService;

    final ProductRegisterListForm productRegisterListForm = new ProductRegisterListForm();
    final ProductRegisterForm productRegisterForm = new ProductRegisterForm();
    final Product product = new Product();
    final Tag tag = new Tag();

    @Before
    public void init() {
        tag.setId(1);
        tag.setName("metal");

        product.setId(1);
        product.setUid("1111");
        product.setName("Product name");
        product.setWithSimilarity(true);
        product.setExternalId(1);
        product.setTags(Collections.singletonList(tag));

        productRegisterForm.setId(1);
        productRegisterForm.setName("Product Name");
        productRegisterForm.setTags(Collections.singletonList("metal"));

        this.productRegisterListForm.getProducts().add(productRegisterForm);

        Mockito.when(this.tagService.findAll()).thenReturn(Collections.singleton(this.tag));
    }
    @Test
    public void supports() {
        final boolean response = this.productRegisterListFormValidator.supports(ProductRegisterListForm.class);
        Assert.assertTrue(response);
    }

    @Test
    public void supports_invalidObjectToValidate() {
        final boolean response = this.productRegisterListFormValidator.supports(ProductData.class);
        Assert.assertFalse(response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_objectForm_IllegalArgumentException() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        this.productRegisterListFormValidator.validate(null, bindingResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_bindingResult_IllegalArgumentException() {
        this.productRegisterListFormValidator.validate(this.productRegisterForm, null);
    }

    @Test
    public void validate_EmptyList() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        this.productRegisterListForm.setProducts(null);
        this.productRegisterListFormValidator.validate(this.productRegisterListForm, bindingResult);
        final FieldError fieldError = bindingResult.getFieldError(RegisterFormErrors.INVALID_INDEX);
        Assert.assertNull(fieldError);
    }

    @Test
    public void validateNullIds() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        this.productRegisterListFormValidator.validate(this.productRegisterListForm, bindingResult);
        final FieldError fieldError = bindingResult.getFieldError(ProductRegisterForm.ID);
        Assert.assertNull(fieldError);
    }

    @Test
    public void validateNullIds_ProductsWithNullId() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        final ProductRegisterForm productRegisterForm = this.productRegisterListForm.getProducts().get(0);
        productRegisterForm.setId(null);
        this.productRegisterListFormValidator.validate(this.productRegisterListForm, bindingResult);
        final FieldError fieldError = bindingResult.getFieldError(ProductRegisterForm.ID);
        Assert.assertNotNull(fieldError);
        Assert.assertEquals(RegisterFormErrors.EMPTY_ID, fieldError.getCode());
    }

    @Test
    public void validateRegisteredIds() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        this.productRegisterListFormValidator.validate(this.productRegisterListForm, bindingResult);
        final FieldError fieldError = bindingResult.getFieldError(ProductRegisterForm.ID);
        Assert.assertNull(fieldError);
    }

    @Test
    public void validateRegisteredIds_ProductWithIdRegistered() {
        Mockito.when(this.productService.findByExternalIdIn(Collections.singletonList(1)))
                .thenReturn(Collections.singletonList(product));
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        this.productRegisterListFormValidator.validate(this.productRegisterListForm, bindingResult);
        final FieldError fieldError = bindingResult.getFieldError(ProductRegisterForm.ID);
        Assert.assertNotNull(fieldError);
        Assert.assertEquals(RegisterFormErrors.EMPTY_ID, fieldError.getCode());
    }

    @Test
    public void validateNames() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        this.productRegisterListFormValidator.validate(this.productRegisterListForm, bindingResult);
        final FieldError fieldError = bindingResult.getFieldError(ProductRegisterForm.NAME);
        Assert.assertNull(fieldError);
    }

    @Test
    public void validateNames_nullName() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        final ProductRegisterForm productRegisterForm = this.productRegisterListForm.getProducts().get(0);
        productRegisterForm.setName(null);
        this.productRegisterListFormValidator.validate(this.productRegisterListForm, bindingResult);
        final FieldError fieldError = bindingResult.getFieldError(ProductRegisterForm.NAME);
        Assert.assertNotNull(fieldError);
        Assert.assertEquals(RegisterFormErrors.EMPTY_NAME, fieldError.getCode());
    }

    @Test
    public void validateTagValues() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        this.productRegisterListFormValidator.validate(this.productRegisterListForm, bindingResult);
        final FieldError fieldError = bindingResult.getFieldError(RegisterFormErrors.INVALID_INDEX);
        Assert.assertNull(fieldError);
    }

    @Test
    public void validateTagValues_InvalidTags() {
        final List<String> tags = new ArrayList<>();
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
        final ProductRegisterForm firstProduct = this.productRegisterListForm.getProducts().iterator().next();

        tags.add("metal");
        tags.add("wrong");
        firstProduct.setTags(tags);

        this.productRegisterListFormValidator.validate(this.productRegisterListForm, bindingResult);
        final FieldError fieldError = bindingResult.getFieldError(RegisterFormErrors.INVALID_INDEX);
        Assert.assertNotNull(fieldError);
    }
}