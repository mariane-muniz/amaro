package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.InvalidProductData;
import br.com.amaro.demo.populators.Populator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Class responsible for converting the errors found in the validation of the forms into an object for replying to the
 * requester.
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
public class InvalidProductDataPopulator
        implements Populator<BindingResult, InvalidProductData> {

    /**
     * The main method responsible for converting the error list into a response object for the requester
     *
     * @param bindingResult standard javax validation class with erros found in the validation
     * @return the DTO with validation form error report
     */
    @Override
    public InvalidProductData populate(final BindingResult bindingResult) {
        Assert.notNull(bindingResult, "parameter cannot be null");

        final InvalidProductData invalidProductData = new InvalidProductData();
        final String[] fieldErrorNameList = {"id", "name", "tagsVector", "tags"};

        for (final String fieldErrorName : fieldErrorNameList) {
            final FieldError fieldError = bindingResult.getFieldError(fieldErrorName);

            if(!ObjectUtils.isEmpty(fieldError)) {
                invalidProductData.getErrors().add(fieldError);
            }
        }
        return invalidProductData;
    }
}