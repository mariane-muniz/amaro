package br.com.amaro.demo.facades;

import br.com.amaro.demo.dtos.ProductCreatedData;
import br.com.amaro.demo.dtos.ProductCreatedResponseData;
import br.com.amaro.demo.dtos.SimilarListProductResponseData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.forms.ProductRegisterListForm;
import br.com.amaro.demo.forms.SearchSimilarProductForm;
import br.com.amaro.demo.forms.SearchSimilarProductListForm;
import br.com.amaro.demo.parameters.ProductRegisterParameter;
import br.com.amaro.demo.parameters.SearchSimilarProductParameter;
import br.com.amaro.demo.populators.impl.ProductCreatedResponseDataPopulator;
import br.com.amaro.demo.populators.impl.ProductPopulator;
import br.com.amaro.demo.populators.impl.SimilarListProductResponseDataPopulator;
import br.com.amaro.demo.services.ProductService;
import br.com.amaro.demo.strategy.ElementListToRemoveStrategy;
import br.com.amaro.demo.validators.RegisterFormErrors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ProductFacade.class)
public class ProductFacadeTest {
    @InjectMocks private ProductFacade productFacade;

    @Mock private ProductPopulator productPopulator;
    @Mock private ProductService productService;
    @Mock private ElementListToRemoveStrategy elementListToRemoveStrategy;
    @Mock private ProductCreatedResponseDataPopulator productCreatedResponseDataPopulator;
    @Mock private SimilarListProductResponseDataPopulator similarListProductResponseDataPopulator;

    private final Product product = new Product();
    private final List<String> tagNameList = new ArrayList<>();
    private final ProductRegisterParameter productRegisterParameter = new ProductRegisterParameter();
    private final SearchSimilarProductParameter searchSimilarProductParameter = new SearchSimilarProductParameter();
    private final SimilarListProductResponseData similarListProductResponseData = new SimilarListProductResponseData();

    @Before
    public void init() {
        final Tag tag = new Tag();
        final List<Tag> tagList = new ArrayList<>();
        final ProductRegisterForm productRegisterForm = new ProductRegisterForm();
        final SearchSimilarProductForm searchSimilarProductForm = new SearchSimilarProductForm();
        final List<ProductRegisterForm> productRegisterFormList = new ArrayList<>();
        final List<SearchSimilarProductForm> searchSimilarProductFormList = new ArrayList<>();

        tag.setId(1);
        tag.setName("metal");
        tagList.add(tag);
        productRegisterForm.setId(1);
        productRegisterForm.setName("Product name");
        productRegisterForm.setTags(tagNameList);
        searchSimilarProductForm.setId(1);
        searchSimilarProductForm.setName("Product name");
        searchSimilarProductForm.setTags(Collections.singletonList("metal"));
        searchSimilarProductForm.setTagsVector(new int[] {0,0,1,1});

        productRegisterFormList.add(productRegisterForm);
        searchSimilarProductFormList.add(searchSimilarProductForm);

        this.product.setId(1);
        this.product.setName("Test");
        this.product.setTags(tagList);
        this.tagNameList.add("metal");
        this.productRegisterParameter.setProductRegisterForms(productRegisterFormList);
        this.searchSimilarProductParameter.setSearchSimilarProductFormList(searchSimilarProductFormList);

        Mockito
                .when(this.similarListProductResponseDataPopulator.populate(searchSimilarProductParameter))
                .thenReturn(similarListProductResponseData);
        Mockito
                .when(this.elementListToRemoveStrategy.execute(Mockito.any()))
                .thenReturn(Collections.emptyList());
        Mockito
                .when(this.productService.persistInDatabase(Mockito.anyList()))
                .thenReturn(Collections.singletonList(this.product));
        Mockito
                .when(this.productPopulator.populate(Mockito.any()))
                .thenReturn(this.product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProducts_IllegalArgumentException() {
        this.productFacade.createProducts(null);
    }

    @Test
    public void createProducts_RemoveInvalidForms() {
        final ProductCreatedResponseData productCreatedResponseData = new ProductCreatedResponseData();
        final ProductCreatedData productCreatedData = new ProductCreatedData();
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new ProductRegisterListForm(), "productRegisterListForm");

        this.productRegisterParameter.setBindingResult(bindingResult);

        bindingResult.rejectValue(RegisterFormErrors.INVALID_INDEX, "test", new Object[]{0}, "test");
        BeanUtils.copyProperties(this.product, productCreatedData);
        productCreatedData.setTags(tagNameList);

        Mockito
                .when(this.productCreatedResponseDataPopulator.populate(productRegisterParameter))
                .thenReturn(productCreatedResponseData);
        Mockito
                .when(this.elementListToRemoveStrategy.execute(Mockito.any()))
                .thenReturn(Collections.singletonList(0));

        this.productFacade.createProducts(this.productRegisterParameter);
        final List<ProductRegisterForm> productRegisterForms = this.productRegisterParameter.getProductRegisterForms();

        Assert.assertEquals(0, productRegisterForms.size());
    }

    @Test
    public void createProducts() {
        final ProductCreatedResponseData productCreatedResponseData = new ProductCreatedResponseData();
        final ProductCreatedData productCreatedData = new ProductCreatedData();
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new ProductRegisterListForm(), "productRegisterListForm");

        this.productRegisterParameter.setBindingResult(bindingResult);

        BeanUtils.copyProperties(this.product, productCreatedData);
        productCreatedData.setTags(tagNameList);

        Mockito
                .when(this.productCreatedResponseDataPopulator.populate(productRegisterParameter))
                .thenReturn(productCreatedResponseData);

        final ProductCreatedResponseData response = this.productFacade.createProducts(this.productRegisterParameter);

        Assert.assertNotNull(response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSimilarProducts_IllegalArgumentException() {
        this.productFacade.getSimilarProducts(null);
    }

    @Test
    public void getSimilarProducts() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new SearchSimilarProductListForm(), "searchSimilarProductListForm");

        this.searchSimilarProductParameter.setBindingResult(bindingResult);

        final SimilarListProductResponseData response =
                this.productFacade.getSimilarProducts(this.searchSimilarProductParameter);

        Assert.assertNotNull(response);
    }

    @Test
    public void getSimilarProducts_InvalidProductList() {
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new SearchSimilarProductListForm(), "searchSimilarProductListForm");

        this.searchSimilarProductParameter.setBindingResult(bindingResult);

        Mockito
                .when(this.elementListToRemoveStrategy.execute(Mockito.any()))
                .thenReturn(Collections.singletonList(0));

        this.productFacade.getSimilarProducts(this.searchSimilarProductParameter);

        Assert.assertEquals(0, this.searchSimilarProductParameter.getSearchSimilarProductFormList().size());
    }
}