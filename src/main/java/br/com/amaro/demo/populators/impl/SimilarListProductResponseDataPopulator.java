package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.InvalidProductData;
import br.com.amaro.demo.dtos.SimilarListProductData;
import br.com.amaro.demo.dtos.SimilarListProductResponseData;
import br.com.amaro.demo.forms.SearchSimilarProductForm;
import br.com.amaro.demo.parameters.SearchSimilarProductParameter;
import br.com.amaro.demo.populators.Populator;
import br.com.amaro.demo.validators.RegisterFormErrors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class responsible for generating the search answer object of similar products
 *
 * @see SearchSimilarProductParameter
 * @see SimilarListProductResponseData
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SimilarListProductResponseDataPopulator
        implements Populator<SearchSimilarProductParameter, SimilarListProductResponseData> {
    private final SimilarListDataPopulator similarListDataPopulator;
    private final InvalidProductDataPopulator invalidProductDataPopulator;

    /**
     * Main method of the DTO creation process.
     * It requests the secondary methods and from the results the DTO is populated and returned
     *
     * @param searchSimilarProductParameter Parameter with the information of forms with rejects and original products
     *                                      for rescue of similar products
     * @return object with the summary of the products that had validation problems and the objects with their similar
     *  products
     */
    @Override
    public SimilarListProductResponseData populate(final SearchSimilarProductParameter searchSimilarProductParameter) {
        Assert.notNull(searchSimilarProductParameter, "parameter cannot be null");

        final SimilarListProductResponseData data = new SimilarListProductResponseData();
        final List<SimilarListProductData> similarProductDataList =
                this.getSimilarProductDataList(searchSimilarProductParameter);
        final InvalidProductData invalidProductData = this.getInvalidProductData(searchSimilarProductParameter);

        data.setProductSimilar(similarProductDataList);
        data.setInvalidProducts(invalidProductData);

        return data;
    }

    /**
     * Responsible method to transform the error lists of the forms into a DTO that will be seen by the client
     *
     * @param parameter parameter with the list of indexes of the forms that have had rejections during the validation
     *                  process
     * @return DTO with the summary of errors that occurred during the process of validation of forms
     */
    private InvalidProductData getInvalidProductData(final SearchSimilarProductParameter parameter) {
        final BindingResult bindingResult = parameter.getBindingResult();
        final FieldError invalidProducts = bindingResult.getFieldError(RegisterFormErrors.INVALID_INDEX);

        if (Objects.nonNull(invalidProducts)) {
            return this.invalidProductDataPopulator.populate(bindingResult);
        }
        return null;
    }

    /**
     * Method responsible for rescuing similar products and also the unification process with its similar products
     * through a DTO
     *
     * @param searchSimilarProductParameter Parameter with the list of original products to rescue similar products
     * @return A list with the original products and their respective similar products
     */
    private List<SimilarListProductData> getSimilarProductDataList(
            final SearchSimilarProductParameter searchSimilarProductParameter
    ) {
        final List<SearchSimilarProductForm> searchSimilarProductFormList =
                searchSimilarProductParameter.getSearchSimilarProductFormList();
        return searchSimilarProductFormList
                .stream().map(this.similarListDataPopulator::populate)
                .collect(Collectors.toList());
    }
}