package br.com.amaro.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This DTO is responsible for storing the feedback objects for new product registration requests
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class ProductCreatedResponseData {

    /**
     * List with forms that contain errors and were not registered in the database
     */
    private InvalidProductData invalidProducts;

    /**
     * List with the products that have been correctly corrected in the database
     */
    private List<ProductCreatedData> createdList;
}