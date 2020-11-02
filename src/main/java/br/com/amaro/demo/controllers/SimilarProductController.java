package br.com.amaro.demo.controllers;

import br.com.amaro.demo.dtos.*;
import br.com.amaro.demo.facades.ProductFacade;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.forms.ProductRegisterListForm;
import br.com.amaro.demo.forms.SearchSimilarProductListForm;
import br.com.amaro.demo.parameters.ProductRegisterParameter;
import br.com.amaro.demo.parameters.SearchSimilarProductParameter;
import br.com.amaro.demo.validators.ProductRegisterListFormValidator;
import br.com.amaro.demo.validators.SearchSimilarProductListFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This is a functional interface to manage new products and similar products
 * @author Mariane Muniz
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class SimilarProductController {
    private final ProductFacade productFacade;
    private final ProductRegisterListFormValidator productRegisterListFormValidator;
    private final SearchSimilarProductListFormValidator searchSimilarProductListFormValidator;

    /**
     * This method is responsible for receive new product registers.
     * Using the standard javax process of form validation, the form is received through of the variable
     * productForm. Then the standard validation process of the fields per annotation on the form and
     * last in the validation class productFormValidator is executed.
     * If the form is valid, in the sequence it is converted into a parameter and sent to the internal layers of the
     * application for persistence in the database and returns with the incoming payload and the vectors.
     *
     * @see ProductRegisterListForm
     * @see ProductCreatedResponseData
     *
     * @param bindingResult javax default validator object
     * @param productRegisterListForm The payload with information of the new product {@link ProductRegisterForm}
     * @return The payload of inside data and tag vectors
     */
    @PostMapping("/product-create")
    public ResponseEntity<ProductCreatedResponseData> createProduct(
            final @Valid @RequestBody ProductRegisterListForm productRegisterListForm,
            final BindingResult bindingResult)
    {
        this.productRegisterListFormValidator.validate(productRegisterListForm, bindingResult);
        final ProductRegisterParameter parameter = new ProductRegisterParameter();

        parameter.setProductRegisterForms(productRegisterListForm.getProducts());
        parameter.setBindingResult(bindingResult);

        final ProductCreatedResponseData productCreatedResponseData = this.productFacade.createProducts(parameter);
        return ResponseEntity.status(HttpStatus.OK).body(productCreatedResponseData);
    }

    /**
     * This method return the similar products.
     * Using the standard javax process of form validation, the form is received through of the variable
     * similarProductForm. Then the standard validation process of the fields per annotation on the form and last in
     * the validation class similarProductFormValidator is executed.
     *
     * @see SimilarListProductResponseData
     * @see SearchSimilarProductListForm
     *
     * @param bindingResult javax default validator object
     * @param searchSimilarProductListForm the payload of the product you want the similar ones
     * @return A list of products {@link SimilarProductData} with a field showing the similarity with the requested product
     */
    @PostMapping("/similar")
    public ResponseEntity<SimilarListProductResponseData> getSimilarProducts(
            final @Valid @RequestBody SearchSimilarProductListForm searchSimilarProductListForm,
            final BindingResult bindingResult
        ) {
        this.searchSimilarProductListFormValidator.validate(searchSimilarProductListForm, bindingResult);
        final SearchSimilarProductParameter parameter = new SearchSimilarProductParameter();

        parameter.setSearchSimilarProductFormList(searchSimilarProductListForm.getSimilar());
        parameter.setBindingResult(bindingResult);

        final SimilarListProductResponseData similarListProductResponseData = this.productFacade.getSimilarProducts(parameter);
        return ResponseEntity.status(HttpStatus.OK).body(similarListProductResponseData);
    }
}