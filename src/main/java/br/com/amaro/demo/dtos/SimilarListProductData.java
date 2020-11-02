package br.com.amaro.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This DTO is responsible for storing the list of similar products from an original product
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class SimilarListProductData extends ProductData {

    /**
     * List of similar products with similarity values to the original product
     */
    private List<SimilarProductData> similarProducts;
}