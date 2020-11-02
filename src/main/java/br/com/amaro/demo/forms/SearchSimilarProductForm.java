package br.com.amaro.demo.forms;

import lombok.Getter;
import lombok.Setter;

/**
 * Form responsible for storing the vectors for similarity calculation
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class SearchSimilarProductForm extends ProductRegisterForm {
    public static final String TAGS_VECTOR = "tagsVector";

    /**
     * This field brings the list of vectors of the tags
     */
    private int[] tagsVector;
}