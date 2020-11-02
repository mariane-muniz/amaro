package br.com.amaro.demo.facades;

import br.com.amaro.demo.dtos.*;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.forms.SearchSimilarProductForm;
import br.com.amaro.demo.parameters.ProductRegisterParameter;
import br.com.amaro.demo.parameters.SearchSimilarProductParameter;
import br.com.amaro.demo.populators.impl.ProductCreatedResponseDataPopulator;
import br.com.amaro.demo.populators.impl.ProductPopulator;
import br.com.amaro.demo.populators.impl.SimilarListProductResponseDataPopulator;
import br.com.amaro.demo.services.ProductService;
import br.com.amaro.demo.strategy.ElementListToRemoveStrategy;
import br.com.amaro.demo.validators.RegisterFormErrors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for operations related to the entire business part of the product
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductPopulator productPopulator;
    private final ProductService productService;
    private final ElementListToRemoveStrategy elementListToRemoveStrategy;
    private final ProductCreatedResponseDataPopulator productCreatedResponseDataPopulator;
    private final SimilarListProductResponseDataPopulator similarListProductResponseDataPopulator;


    /**
     * Method responsible for the operation of the services of creation of the product information tree
     *
     * @see ProductRegisterParameter
     * @see ProductCreatedData
     *
     * @param productRegisterParameter the product parameter with basic information (id, name) and tags
     * @return DTO with customer payload to return to the controller
     */
    public ProductCreatedResponseData createProducts(final ProductRegisterParameter productRegisterParameter) {
        Assert.notNull(productRegisterParameter, "productParameter cannot be null");

        final BindingResult bindingResult = productRegisterParameter.getBindingResult();
        final FieldError fieldError = bindingResult.getFieldError(RegisterFormErrors.INVALID_INDEX);
        final List<Integer> invalidProductList = this.elementListToRemoveStrategy.execute(fieldError);

        /* Removes forms that had problems during the validation process */
        if (!CollectionUtils.isEmpty(invalidProductList)) {
            final List<ProductRegisterForm> formsToRemoveList = new ArrayList<>();
            final List<ProductRegisterForm> productRegisterForms = productRegisterParameter.getProductRegisterForms();
            invalidProductList.forEach(invalidProductIndex -> {
                final ProductRegisterForm productRegisterForm = productRegisterForms.get(invalidProductIndex);
                formsToRemoveList.add(productRegisterForm);
            });
            productRegisterParameter.getProductRegisterForms().removeAll(formsToRemoveList);
        }

        /* Creates the entities, adds to a list and persists the list with the new products in the database */
        final List<ProductRegisterForm> productRegisterFormList = productRegisterParameter.getProductRegisterForms();
        if (!CollectionUtils.isEmpty(productRegisterFormList)) {

            final List<Product> productList = productRegisterFormList.parallelStream()
                    .map(this.productPopulator::populate).collect(Collectors.toList());
            final List<Product> response =this.productService.persistInDatabase(productList);

            productRegisterParameter.setProductList(response);
        }

        /* Returns the list with the operations summary */
        return this.productCreatedResponseDataPopulator.populate(productRegisterParameter);
    }

    /**
     * Method responsible for returning a list of similar products through a service tree
     *
     * @see SearchSimilarProductParameter
     * @see SimilarProductData
     *
     * @param searchSimilarProductParameter the product parameter with basic information (id, name) and tags
     * @return a list of similar product DTOs
     */
    public SimilarListProductResponseData getSimilarProducts(
            final SearchSimilarProductParameter searchSimilarProductParameter
    ) {
        Assert.notNull(searchSimilarProductParameter, "productParameter cannot be null");

        final BindingResult bindingResult = searchSimilarProductParameter.getBindingResult();
        final FieldError fieldError = bindingResult.getFieldError(RegisterFormErrors.INVALID_INDEX);
        final List<Integer> invalidProductList = this.elementListToRemoveStrategy.execute(fieldError);

        /* Removes forms that had problems during the validation process */
        if (!CollectionUtils.isEmpty(invalidProductList)) {
            final List<SearchSimilarProductForm> formsToRemoveList = new ArrayList<>();
            final List<SearchSimilarProductForm> searchSimilarProductFormList =
                    searchSimilarProductParameter.getSearchSimilarProductFormList();
            invalidProductList.forEach(invalidProductIndex -> {
                final SearchSimilarProductForm searchSimilarProductForm =
                        searchSimilarProductFormList.get(invalidProductIndex);
                formsToRemoveList.add(searchSimilarProductForm);
            });
            searchSimilarProductParameter.getSearchSimilarProductFormList().removeAll(formsToRemoveList);
        }

        /* Returns the list with the operations summary */
        return this.similarListProductResponseDataPopulator.populate(searchSimilarProductParameter);
    }
}