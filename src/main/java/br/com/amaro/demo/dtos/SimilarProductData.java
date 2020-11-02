package br.com.amaro.demo.dtos;

import br.com.amaro.demo.controllers.SimilarProductController;
import lombok.Getter;
import lombok.Setter;

/**
 * This DTO is responsible for the similarity information that will be exposed to the customer
 * through the controller {@link br.com.amaro.demo.controllers.SimilarProductController}
 *
 * @see SimilarProductController
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class SimilarProductData extends ProductData {

    /**
     * This field brings the similarity between the original product and the product returned by the system
     */
    private Double similarity;
}