package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.InvalidProductData;
import br.com.amaro.demo.dtos.ProductCreatedData;
import br.com.amaro.demo.dtos.ProductCreatedResponseData;
import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.parameters.ProductRegisterParameter;
import br.com.amaro.demo.populators.Populator;
import br.com.amaro.demo.validators.RegisterFormErrors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class responsible for creating the person responsible for the products that have been created in the database and
 * the forms that are with errors
 *
 * @see ProductRegisterParameter
 * @see ProductCreatedResponseData
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ProductCreatedResponseDataPopulator
        implements Populator<ProductRegisterParameter, ProductCreatedResponseData> {
    private final ProductCreatedDataPopulator productCreatedDataPopulator;
    private final InvalidProductDataPopulator invalidProductDataPopulator;

    /**
     * The object that will be returned to the customer containing the list of products registered in the database and
     * also the details of the forms that are with errors
     *
     * @param productRegisterParameter object with the list of similar products recovered from the database
     * @return DTO with the summary of the operation that will be returned to the customer
     */
    @Override
    public ProductCreatedResponseData populate(final ProductRegisterParameter productRegisterParameter) {
        Assert.notNull(productRegisterParameter, "parameter cannot be null");

        final ProductCreatedResponseData productCreatedResponseData = new ProductCreatedResponseData();
        final List<ProductCreatedData> productCreatedDataList = this.getProductCreatedDataList(productRegisterParameter);
        final InvalidProductData invalidProductData = this.getInvalidProductData(productRegisterParameter);

        productCreatedResponseData.setInvalidProducts(invalidProductData);
        productCreatedResponseData.setCreatedList(productCreatedDataList);

        return productCreatedResponseData;
    }

    /**
     * Method responsible for creating a DTO with the information from the forms that had errors during the validation
     *
     * @param productRegisterParameter object with the list of similar products recovered from the database
     * @return the DTO with the error details of the forms
     */
    private InvalidProductData getInvalidProductData(final ProductRegisterParameter productRegisterParameter) {
        final BindingResult bindingResult = productRegisterParameter.getBindingResult();
        final FieldError fieldError = bindingResult.getFieldError(RegisterFormErrors.INVALID_INDEX);

        if (Objects.nonNull(fieldError)){
            return this.invalidProductDataPopulator.populate(bindingResult);
        }
        return null;
    }

    /**
     * Method responsible for converting similar products into DTOs
     *
     * @param productRegisterParameter object with the list of similar products recovered from the database
     * @return list of the similar products in DTO format
     */
    private List<ProductCreatedData> getProductCreatedDataList(final ProductRegisterParameter productRegisterParameter) {
        final List<Product> productList = productRegisterParameter.getProductList();

        if (!CollectionUtils.isEmpty(productList)) {
            return productList.parallelStream()
                    .map(this.productCreatedDataPopulator::populate)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}