package br.com.amaro.demo.services;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.SimilarProduct;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.exceptions.SimilaritySizeComparisonException;
import br.com.amaro.demo.repositories.ProductRepository;
import br.com.amaro.demo.repositories.SimilarProductRepository;
import br.com.amaro.demo.strategy.IndexOfTagsStrategy;
import br.com.amaro.demo.strategy.ProductVectorStrategy;
import br.com.amaro.demo.strategy.SimilarityStrategy;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ProductSimilarService.class)
public class ProductSimilarServiceTest {
    @InjectMocks private ProductSimilarService productSimilarService;

    @Mock private SimilarProductRepository similarProductRepository;
    @Mock private IndexOfTagsStrategy indexOfTagsStrategy;
    @Mock private ProductVectorStrategy productVectorStrategy;
    @Mock private SimilarityStrategy similarityStrategy;
    @Mock private ProductRepository productRepository;

    final List<Tag> productTags = new ArrayList<>();

    @SneakyThrows
    @Before
    public void init() {
        final Tag tag = new Tag();
        final Product product = new Product();
        final String productUid = "1111-1111-1111-1111";
        final Pageable pageable = PageRequest.of(0, 3);
        final List<SimilarProduct> similarProductList = new ArrayList<>();
        final SimilarProduct similarProduct = new SimilarProduct();
        final List<Product> productList = new ArrayList<>();
        final List<Integer> newProductIndex = new ArrayList<>();
        final int[] vectorList = {1,0};

        similarProduct.setId(1);
        similarProduct.setSimilarity(1.5);
        similarProduct.setToken(productUid + "2222-2222-2222-2222");

        similarProductList.add(similarProduct);

        newProductIndex.add(1);

        tag.setId(1);
        tag.setName("couro");
        this.productTags.add(tag);

        product.setUid(productUid);
        product.setId(1);
        product.setName("Test product");
        product.setTags(productTags);

        productList.add(product);

        when(this.productRepository.findAll()).thenReturn(productList);
        when(this.similarProductRepository.findByToken(productUid, pageable)).thenReturn(similarProductList);
        when(this.indexOfTagsStrategy.getIndexOfTags(this.productTags)).thenReturn(newProductIndex);
        when(this.productVectorStrategy.getVectors(newProductIndex)).thenReturn(vectorList);
        when(this.similarityStrategy.calculate(vectorList, vectorList)).thenReturn(1.6);
    }

    @Test
    public void getSimilarProducts() {
        ReflectionTestUtils.setField(this.productSimilarService, "productSimilarQty", 3);
        final List<SimilarProduct> response = this.productSimilarService.getSimilarProducts("1111-1111-1111-1111");
        Assert.assertEquals(1, response.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSimilarProducts_IllegalArgumentException() {
        this.productSimilarService.getSimilarProducts(null);
    }

    @Test
    public void generateSimilarity() {
        Mockito.when(this.similarProductRepository.saveAll(Mockito.any())).thenReturn(null);

        Product p1 = new Product();
        p1.setTags(this.productTags);
        p1.setName("Product teste 2");
        p1.setId(2);

        this.productSimilarService.generateSimilarity(p1);

        Mockito.verify(this.similarProductRepository, Mockito.times(1)).saveAll(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculateSimilarity_newProduct_IllegalArgumentException() {
        this.productSimilarService.calculateSimilarity(null, new Product());
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculateSimilarity_product_IllegalArgumentException() {
        this.productSimilarService.calculateSimilarity(new Product(), null);
    }

    @SneakyThrows
    @Test
    public void calculateSimilarity_product_SimilaritySizeComparisonException() {
        final int[] vectorList = {1,0};
        Product p1 = new Product();
        Product p2 = new Product();

        p1.setTags(this.productTags);
        p2.setTags(this.productTags);

        when(this.similarityStrategy.calculate(vectorList, vectorList))
                .thenThrow(SimilaritySizeComparisonException.class);

        final Double response = this.productSimilarService.calculateSimilarity(p1, p2);
        Assert.assertNull(response);
    }

    @Test
    public void calculateSimilarity() {
        Product p1 = new Product();
        Product p2 = new Product();

        p1.setTags(this.productTags);
        p2.setTags(this.productTags);

        final Double response = this.productSimilarService.calculateSimilarity(p1, p2);
        Assert.assertEquals(response, Double.valueOf(1.6d));
    }
}