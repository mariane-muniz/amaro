package br.com.amaro.demo.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * This DTo is responsible of storing form errors
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class InvalidProductData {

    /**
     * List with the errors found in the validations of the forms.
     */
    private List<FieldError> errors = new ArrayList<>();
}