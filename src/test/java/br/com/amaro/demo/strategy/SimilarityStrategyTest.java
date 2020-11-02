package br.com.amaro.demo.strategy;

import br.com.amaro.demo.exceptions.SimilaritySizeComparisonException;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = SimilarityStrategy.class)
public class SimilarityStrategyTest {
    @InjectMocks private SimilarityStrategy similarityStrategy;

    private final int[] vectors1 = {0,1,0,0,1,1};
    private final int[] vectors2 = {0,1,0,1,0,1};

    @SneakyThrows
    @Test
    public void calculate() {
        final double response = this.similarityStrategy.calculate(vectors1, vectors2);
        Assert.assertEquals(Double.valueOf(response), Double.valueOf(1.4142135623730951));
    }

    @SneakyThrows
    @Test(expected = IllegalArgumentException.class)
    public void calculate_vector1_IllegalArgumentException() {
        this.similarityStrategy.calculate(null, vectors2);
    }

    @SneakyThrows
    @Test(expected = IllegalArgumentException.class)
    public void calculate_vector2_IllegalArgumentException() {
        this.similarityStrategy.calculate(vectors1, null);
    }

    @SneakyThrows
    @Test(expected = SimilaritySizeComparisonException.class)
    public void calculate_SimilaritySizeComparisonException() {
        final int[] vectors3 = {0,1,0,0,1,1,0};
        this.similarityStrategy.calculate(vectors1, vectors3);
    }
}