package br.com.amaro.demo.jobs;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.services.ProductService;
import br.com.amaro.demo.services.ProductSimilarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = GenerateProductSimilarityJob.class)
public class GenerateProductSimilarityJobTest {
    @InjectMocks GenerateProductSimilarityJob generateProductSimilarityJob;

    @Mock private ProductService productService;
    @Mock private ProductSimilarService productSimilarService;

    @Before
    public void init() {
        final Tag tag = new Tag();
        final Product product = new Product();

        tag.setId(1);
        tag.setName("metal");

        product.setId(1);
        product.setExternalId(1);
        product.setName("Product name");
        product.setUid("1111-1111");
        product.setTags(Collections.singletonList(tag));

        Mockito.doNothing().when(this.productSimilarService).generateSimilarity(product);
        Mockito.when(this.productService.findWithoutSimilarity(Mockito.any())).thenReturn(Collections.singletonList(product));
        Mockito.when(this.productService.persistInDatabase(Mockito.anyList())).thenReturn(Collections.singletonList(product));
    }

    @Test
    public void execute() {
        ReflectionTestUtils.setField(this.generateProductSimilarityJob, "quantity", 10);
        this.generateProductSimilarityJob.execute();
        Mockito.verify(this.productService, Mockito.times(1)).persistInDatabase(Mockito.anyList());
    }
}