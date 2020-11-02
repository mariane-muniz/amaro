package br.com.amaro.demo.strategy;

import br.com.amaro.demo.services.TagService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ProductVectorStrategy.class)
public class ProductVectorStrategyTest {
    @InjectMocks private ProductVectorStrategy productVectorStrategy;

    @Mock private TagService tagService;

    private final List<Integer> indexes = new ArrayList<>();

    @Before
    public void init() {
        this.indexes.add(2);

        when(this.tagService.countTotalTags()).thenReturn(10);
    }

    @Test
    public void getVectors() {
        int[] response = this.productVectorStrategy.getVectors(this.indexes);
        Assert.assertEquals(10, response.length);
        Assert.assertEquals(1, response[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getVectors_IllegalArgumentException() {
        this.productVectorStrategy.getVectors(null);
    }
}