package br.com.amaro.demo.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchSimilarProductListForm extends ProductRegisterListForm {
    /*
     * Internal form variables
     * used to typify errors found during the validation process
     */
    private String tagsVector;

    /**
     * Search form list
     */
    @NotEmpty(message = "Search list cannot be empty")
    private List<SearchSimilarProductForm> similar;

    /**
     * Method responsible for converting the list of search forms into a product registration list
     *
     * @return list of product register form
     */
    @Override
    public List<ProductRegisterForm> getProducts() {
        return new ArrayList<>(this.similar);
    }
}