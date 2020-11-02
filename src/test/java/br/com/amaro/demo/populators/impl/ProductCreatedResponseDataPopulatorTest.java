package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.ProductCreatedData;
import br.com.amaro.demo.dtos.ProductCreatedResponseData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.forms.ProductRegisterListForm;
import br.com.amaro.demo.parameters.ProductRegisterParameter;
import br.com.amaro.demo.validators.RegisterFormErrors;
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

import java.util.ArrayList;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ProductCreatedResponseDataPopulator.class)
public class ProductCreatedResponseDataPopulatorTest {
    @InjectMocks private ProductCreatedResponseDataPopulator productCreatedResponseDataPopulator;

    @Mock private ProductCreatedDataPopulator productCreatedDataPopulator;
    @Mock private InvalidProductDataPopulator invalidProductDataPopulator;

    private  BeanPropertyBindingResult bindingResult;
    private final ProductRegisterParameter productRegisterParameter = new ProductRegisterParameter();

    @Before
    public void init() {
        final Product product = new Product();
        final ProductCreatedData productCreatedData = new ProductCreatedData();

        product.setId(1);
        product.setName("Product name");
        product.setUid("1111");
        product.setWithSimilarity(true);
        product.setExternalId(123);

        productCreatedData.setId(1);
        productCreatedData.setName("Product name");
        productCreatedData.setTags(Collections.singletonList("tag"));

        this.bindingResult =
                new BeanPropertyBindingResult(new ProductRegisterListForm(), "productRegisterParameter");
        this.productRegisterParameter.setProductList(Collections.singletonList(product));
        this.productRegisterParameter.setBindingResult(this.bindingResult);

        Mockito
                .when(this.productCreatedDataPopulator.populate(Mockito.any(Product.class)))
                .thenReturn(productCreatedData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_IllegalArgumentException() {
        this.productCreatedResponseDataPopulator.populate(null);
    }

    @Test
    public void getInvalidProductData_WithoutRejections() {
        final ProductCreatedResponseData response = this.productCreatedResponseDataPopulator
                .populate(this.productRegisterParameter);
        Assert.assertNull(response.getInvalidProducts());
    }


    @Test
    public void getInvalidProductData() {
        this.bindingResult.rejectValue(RegisterFormErrors.INVALID_INDEX, RegisterFormErrors.EMPTY_ID,
                new Object[]{0}, "Products without id or duplicated");

        this.productCreatedResponseDataPopulator                .populate(this.productRegisterParameter);

        Mockito
                .verify(this.invalidProductDataPopulator, Mockito.times(1))
                .populate(Mockito.any());
    }

    @Test
    public void getInvalidProductData_WithoutErrors() {
        final ProductCreatedResponseData response = this.productCreatedResponseDataPopulator
                .populate(this.productRegisterParameter);

        Assert.assertNull(response.getInvalidProducts());
    }

    @Test
    public void getProductCreatedDataList() {
        ProductCreatedResponseData response = this.productCreatedResponseDataPopulator
                .populate(this.productRegisterParameter);
        Assert.assertEquals(1, response.getCreatedList().size());
    }

    @Test
    public void getProductCreatedDataList_EmptyList() {
        this.productRegisterParameter.setProductList(new ArrayList<>());
        final ProductCreatedResponseData response = this.productCreatedResponseDataPopulator
                .populate(this.productRegisterParameter);
        Assert.assertEquals(0, response.getCreatedList().size());
    }
}