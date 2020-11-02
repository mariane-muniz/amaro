package br.com.amaro.demo.parameters;

import br.com.amaro.demo.entities.Product;
import lombok.Getter;
import lombok.Setter;

/**
 * Class responsible for supporting the creation of a similar product search response
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class SimilarProductParameter {

    /**
     * Similar product found
     */
    private Product similarProduct;

    /**
     * Value of similarity of the original product with the one found
     */
    private Double similarity;
}
