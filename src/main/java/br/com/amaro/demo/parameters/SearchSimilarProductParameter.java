package br.com.amaro.demo.parameters;

import br.com.amaro.demo.forms.SearchSimilarProductForm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Class responsible for maintaining objects in the search process for similar products
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class SearchSimilarProductParameter {

    /**
     * List with the forms of the original products for the search process
     */
    private List<SearchSimilarProductForm> searchSimilarProductFormList;

    /**
     * Class that contains the list of errors found during the validation process
     */
    private BindingResult bindingResult;
}
