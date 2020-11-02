package br.com.amaro.demo.strategy;

import br.com.amaro.demo.exceptions.SimilaritySizeComparisonException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Class responsible for calculating the similarity between two products
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
public class SimilarityStrategy {

    /**
     * The main method responsible for calculating similar products. From this method the secondary methods are
     * called for auxiliary mathematical operations
     *
     * @param firstList a vector array of first product
     * @param secondList a vector array of second product
     * @return the similarity between products
     * @throws SimilaritySizeComparisonException This exception occurs when the amount of elements between
     *      the two vectors is different.
     */
    public double calculate(final int[]firstList, final int[]secondList) throws SimilaritySizeComparisonException {
        Assert.notNull(firstList, "firstList cannot be null");
        Assert.notNull(secondList, "secondList cannot be null");

        if (firstList.length == secondList.length) {

            final int[] vector = this.sumValues(firstList, secondList);
            final int[] vectorMultiplied = this.multiply(vector);
            final int totalValue = this.sumAll(vectorMultiplied);
            return this.squareRoot(totalValue);
        }

        throw new SimilaritySizeComparisonException();
    }

    private double squareRoot(final int totalValue) {
        return Math.sqrt(totalValue);
    }

    /**
     * Internal method responsible for making the sum of a vector for an integer
     *
     * @param vectorMultiplied a vector array
     * @return the sum of vectors in a integer value
     */
    private int sumAll(int[] vectorMultiplied) {
        int totalValue = 0;
        for (int v : vectorMultiplied) {
            totalValue += v;
        }
        return totalValue;
    }

    /**
     * Internal method responsible for multiplying a vector to the square
     *
     * @param vector a vector array
     * @return a vector array
     */
    private int[] multiply(final int[]vector) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i] * vector[i];
        }
        return vector;
    }

    /**
     * Internal method responsible for making the sum of two vectors
     *
     * @param firstList a vector array of first product
     * @param secondList a vector array of second product
     * @return the sum of the both vectors
     */
    private int[] sumValues(final int[]firstList, final int[]secondList) {
        final int size = firstList.length;
        final int[] values = new int[size];
        for (int i = 0; i < size; i++) {
            values[i] = firstList[i] - secondList[i];
        }
        return values;
    }
}
