package br.com.amaro.demo.dtos;

import br.com.amaro.demo.controllers.SimilarProductController;
import lombok.Getter;
import lombok.Setter;

/**
 * This DTO is responsible for the basic information of the product that will be exposed to the customer
 * through the controller {@link br.com.amaro.demo.controllers.SimilarProductController}.
 *
 * @see SimilarProductController
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class ProductData {

    /**
     * This is a unique self-generated identifier for register control by the database.
     */
    private Integer id;

    /**
     * This field stores the name of the product
     */
    private String name;
}