package br.com.amaro.demo.services;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.repositories.ProductRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ProductService.class)
public class ProductServiceTest {
    @InjectMocks private ProductService productService;

    @Mock private ProductRepository productRepository;

    private final Product product = new Product();

    @Before
    public void init() {
        this.product.setId(1);
        this.product.setExternalId(2);
        this.product.setName("Product name");
        this.product.setUid("1111-1111");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findWithoutSimilarity_IllegalArgumentException() {
        this.productService.findWithoutSimilarity(null);
    }

    @Test
    public void findWithoutSimilarity() {
        final PageRequest pageable = PageRequest.of(0, 1);
        Mockito.when(this.productRepository.findWithoutSimilarity(pageable)).thenReturn(Collections.singletonList(this.product));
        final List<Product> response = this.productService.findWithoutSimilarity(pageable);
        Assert.assertNotNull(response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByExternalId_IllegalArgumentException() {
        this.productService.findByExternalId(null);
    }

    @Test
    public void findByExternalId() {
        Mockito.when(this.productRepository.findByExternalId(1)).thenReturn(java.util.Optional.of(this.product));
        Product response = this.productService.findByExternalId(1);
        Assert.assertNotNull(response);
    }

    @Test
    public void findByExternalIdIn() {
        Mockito.when(this.productRepository.findByExternalIdIn(Collections.singletonList(1)))
                .thenReturn(Collections.singletonList(this.product));
        final List<Product> response = this.productService.findByExternalIdIn(Collections.singletonList(1));
        Assert.assertEquals(1, response.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByExternalIdIn_IllegalArgumentException() {
        this.productService.findByExternalIdIn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByUid_IllegalArgumentException() {
        this.productService.findByUid(null);
    }

    @Test
    public void findByUid() {
        Mockito.when(this.productRepository.findByUid(product.getUid())).thenReturn(Optional.of(this.product));
        final Product response = this.productService.findByUid(product.getUid());
        Assert.assertNotNull(response);
    }

    @Test
    public void persistInDatabase() {
        final List<Product> productList = Collections.singletonList(this.product);
        Mockito.when(this.productRepository.saveAll(productList)).thenReturn(productList);
        final List<Product> response = this.productService.persistInDatabase(Collections.singletonList(this.product));
        Assert.assertEquals(1, response.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void persistInDatabase_IllegalArgumentException() {
        this.productService.persistInDatabase(null);
    }
}