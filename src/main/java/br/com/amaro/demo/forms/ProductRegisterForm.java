package br.com.amaro.demo.forms;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Form responsible for storing the basic information of the product and the validation rules of its attributes.
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class ProductRegisterForm {
    /**
     * The public name for ID field
     */
    public static final String ID = "id";
    /**
     * The public name for NAME field
     */
    public static final String NAME = "name";
    /**
     * The public name for TAGS field
     */
    public static final String TAGS = "tags";

    /**
     * The database sequential unique identifier
     */
    private Integer id; //NOSONAR

    /**
     * This field stores the name of the product
     */
    private String name; //NOSONAR

    /**
     * This field brings the list of the tags
     */
    private List<String> tags;  //NOSONAR
}