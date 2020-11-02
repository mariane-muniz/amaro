package br.com.amaro.demo.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * This entity is responsible for storing the product similarity.
 *
 * @see br.com.amaro.demo.services.ProductSimilarService
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Entity
@Getter
@Setter
@Table(name = "similar_products")
public class SimilarProduct {

    /**
     * This is a unique self-generated identifier for register control by the database.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * This field is responsible for saving the token that identifies the products being calculated.
     * To identify the relationship between the objects a token name field was dedicated. The token is formed by joining
     * the two UIDs of the products that are linked in order of execution in the product creation process.
     */
    @NotNull
    private String token;

    /**
     * This field contains the similarities between two products.
     */
    @NotNull
    private Double similarity;
}