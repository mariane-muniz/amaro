package br.com.amaro.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This DTO is responsible for the tag and vector information that will be exposed to the customer
 * through the controller.
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class ProductCreatedData extends ProductData{

    /**
     * This field brings the list of vectors of the tags
     */
    private int[] tagsVector;

    /**
     * This field brings the list of the tags
     */
    private List<String> tags;
}