package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.ProductData;
import br.com.amaro.demo.dtos.SimilarListProductData;
import br.com.amaro.demo.dtos.SimilarProductData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.SimilarProduct;
import br.com.amaro.demo.forms.SearchSimilarProductForm;
import br.com.amaro.demo.parameters.SimilarProductParameter;
import br.com.amaro.demo.services.ProductService;
import br.com.amaro.demo.services.ProductSimilarService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = SimilarListDataPopulator.class)
public class SimilarListDataPopulatorTest {
    @InjectMocks private SimilarListDataPopulator similarListDataPopulator;

    @Mock private ProductService productService;
    @Mock private ProductDataPopulator productDataPopulator;
    @Mock private ProductSimilarService productSimilarService;
    @Mock private SimilarProductDataPopulator similarProductDataPopulator;

    private final Product product = new Product();
    private final SimilarProductData similarProductData = new SimilarProductData();
    private final SearchSimilarProductForm searchSimilarProductForm = new SearchSimilarProductForm();

    @Before
    public void init() {
        final ProductData productData = new ProductData();
        final SimilarProduct similarProduct = new SimilarProduct();
        final SimilarProductParameter similarProductParameter = new SimilarProductParameter();

        similarProduct.setId(1);
        similarProduct.setSimilarity(2.5d);
        similarProduct.setToken("1111-111--2222-222");
        productData.setName("Product name");
        productData.setId(1);
        similarProductData.setId(1);
        similarProductData.setName(productData.getName());
        similarProductData.setSimilarity(similarProduct.getSimilarity());
        similarProductParameter.setSimilarProduct(this.product);
        similarProductParameter.setSimilarity(similarProduct.getSimilarity());

        this.product.setUid("1111-111");
        this.searchSimilarProductForm.setId(productData.getId());
        this.searchSimilarProductForm.setName(productData.getName());
        this.searchSimilarProductForm.setTags(Collections.singletonList("metal"));
        this.searchSimilarProductForm.setTagsVector(new int[]{1});

        Mockito.when(this.productService.findByUid(Mockito.any())).thenReturn(this.product);
        Mockito.when(this.productService.findByExternalId(Mockito.anyInt())).thenReturn(this.product);
        Mockito.when(this.productDataPopulator.populate(Mockito.any())).thenReturn(productData);
        Mockito.when(this.productSimilarService.getSimilarProducts("1111-111")).thenReturn(Collections.singletonList(similarProduct));
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_IllegalArgumentException() {
        this.similarListDataPopulator.populate(null);
    }

    @Test
    public void getSimilarProductData() {
        Mockito.when(this.similarProductDataPopulator.populate(Mockito.any())).thenReturn(similarProductData);
        final SimilarListProductData response = this.similarListDataPopulator.populate(this.searchSimilarProductForm);
        Assert.assertNotNull(response.getSimilarProducts());
    }

    @Test
    public void getSimilarProductData_WithoutProductSimilar() {
        Mockito.when(this.productSimilarService.getSimilarProducts("1111-111")).thenReturn(Collections.emptyList());
        final SimilarListProductData response = this.similarListDataPopulator.populate(this.searchSimilarProductForm);
        Assert.assertEquals(0, response.getSimilarProducts().size());
    }
}