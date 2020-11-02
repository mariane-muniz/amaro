package br.com.amaro.demo.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

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
@Table(name = "tags")
public class Tag {

    /**
     * This is a unique self-generated identifier for register control by the database.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * This field stores the name of the tag
     */
    @NotNull
    private String name;

    /**
     * This field stores the relationship between the tags and the products. The mapping is done through
     * the {@link Product} object in the "tags" field
     *
     * @see Product
     */
    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Product> products;
}
