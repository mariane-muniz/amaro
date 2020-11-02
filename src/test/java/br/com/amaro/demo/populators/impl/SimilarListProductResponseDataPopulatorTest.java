package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.InvalidProductData;
import br.com.amaro.demo.dtos.SimilarListProductData;
import br.com.amaro.demo.dtos.SimilarListProductResponseData;
import br.com.amaro.demo.forms.SearchSimilarProductForm;
import br.com.amaro.demo.forms.SearchSimilarProductListForm;
import br.com.amaro.demo.parameters.SearchSimilarProductParameter;
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
import org.springframework.validation.BindingResult;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = SimilarListProductResponseDataPopulator.class)
public class SimilarListProductResponseDataPopulatorTest {
    @InjectMocks private SimilarListProductResponseDataPopulator similarListProductResponseDataPopulator;

    @Mock private SimilarListDataPopulator similarListDataPopulator;
    @Mock private InvalidProductDataPopulator invalidProductDataPopulator;

    private final SearchSimilarProductParameter searchSimilarProductParameter = new SearchSimilarProductParameter();
    private final SearchSimilarProductListForm searchSimilarProductListForm = new SearchSimilarProductListForm();

    @Before
    public void init() {
        final BindingResult bindingResult =
                new BeanPropertyBindingResult(this.searchSimilarProductListForm, "productRegisterListForm");
        final SearchSimilarProductForm searchSimilarProductForm = new SearchSimilarProductForm();
        final SimilarListProductData similarListProductData = new SimilarListProductData();
        final InvalidProductData invalidProductData = new InvalidProductData();

        searchSimilarProductForm.setId(1);
        searchSimilarProductForm.setName("Product name");
        searchSimilarProductForm.setTags(Collections.singletonList("metal"));
        searchSimilarProductForm.setTagsVector(new int[]{0,0,1});

        this.searchSimilarProductParameter.setBindingResult(bindingResult);
        this.searchSimilarProductParameter
                .setSearchSimilarProductFormList(Collections.singletonList(searchSimilarProductForm));

        Mockito
                .when(this.similarListDataPopulator.populate(Mockito.any()))
                .thenReturn(similarListProductData);

        Mockito
                .when(this.invalidProductDataPopulator.populate(Mockito.any()))
                .thenReturn(invalidProductData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_IllegalArgumentException() {
        this.similarListProductResponseDataPopulator.populate(null);
    }

    @Test
    public void populate() {
        final SimilarListProductResponseData response =
                this.similarListProductResponseDataPopulator.populate(this.searchSimilarProductParameter);
        Assert.assertNotNull(response);
    }

    @Test
    public void getInvalidProductData() {
        final BindingResult bindingResult = this.searchSimilarProductParameter.getBindingResult();
        bindingResult.rejectValue(RegisterFormErrors.INVALID_INDEX, RegisterFormErrors.EMPTY_NAME, new Object[]{0}, "test");
        final SimilarListProductResponseData response =
                this.similarListProductResponseDataPopulator.populate(this.searchSimilarProductParameter);
        Assert.assertNotNull(response.getInvalidProducts());
    }

    @Test
    public void getSimilarProductDataList() {
        final SimilarListProductResponseData response =
                this.similarListProductResponseDataPopulator.populate(this.searchSimilarProductParameter);
        Assert.assertNotNull(response.getProductSimilar());
    }
}