package br.com.amaro.demo.parameters;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.forms.ProductRegisterForm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Class responsible for maintaining the objects of the new product registration process
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Getter
@Setter
public class ProductRegisterParameter {

    /**
     * List of new product registration forms
     */
    private List<ProductRegisterForm> productRegisterForms;

    /**
     * Product list (generic utilization)
     */
    private List<Product> productList;

    /**
     * Class that contains the list of errors found during the validation process
     */
    private BindingResult bindingResult;
}