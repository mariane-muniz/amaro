package br.com.amaro.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This DTO is responsible for storing the feedback objects for similarity search requests
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class SimilarListProductResponseData {

    /**
     * List with forms that contain errors and were not registered in the database
     */
    private InvalidProductData invalidProducts;

    /**
     * List with lists of similar products from a list of original products
     */
    private List<SimilarListProductData> productSimilar;
}