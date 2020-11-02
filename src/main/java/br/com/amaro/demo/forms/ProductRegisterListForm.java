package br.com.amaro.demo.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductRegisterListForm {
    /*
    * Internal form variables
    * used to typify errors found during the validation process
    */
    private String id;
    private String name;
    private String tags;
    private String invalidIndex;

    /**
     *  Product register form list
     */
    @NotEmpty(message = "product list cannot be empty")
    private List<ProductRegisterForm> products = new ArrayList<>();
}