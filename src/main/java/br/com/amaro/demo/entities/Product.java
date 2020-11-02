package br.com.amaro.demo.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * This entity is responsible for storing basic information about the product
 *
 * @see br.com.amaro.demo.services.ProductService
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    /**
     * This is a unique self-generated identifier for register control by the database.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * External product identification
     */
    @Column(unique = true, nullable = false)
    private Integer externalId;

    /**
     * This field is the unique product identifier.
     * Is also used for the similar product token.
     */
    @Column(nullable = false, updatable = false)
    private String uid;

    /**
     * This field stores the name of the product
     */
    @NotNull
    private String name;

    /**
     * This field stores the product's relationship with the tags {@link Tag} it uses through the "prod_cat_rel" table.
     *
     * @see Tag
     */
    @ManyToMany
    @JoinTable(
        name = "prod_tag_rel",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    /**
     * Field that validates if there is already similarity of this product with other products
     *
     */
    @Column(name = "with_similarity")
    private boolean withSimilarity;
}