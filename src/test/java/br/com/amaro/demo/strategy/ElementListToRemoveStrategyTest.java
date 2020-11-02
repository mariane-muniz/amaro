package br.com.amaro.demo.strategy;

import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.forms.ProductRegisterListForm;
import br.com.amaro.demo.validators.RegisterFormErrors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = IndexOfTagsStrategy.class)
public class ElementListToRemoveStrategyTest {
    @InjectMocks private ElementListToRemoveStrategy elementListToRemoveStrategy;

    private final ProductRegisterListForm productRegisterListForm = new ProductRegisterListForm();

    @Before
    public void init() {
        final ProductRegisterForm productRegisterForm1 = new ProductRegisterForm();
        final ProductRegisterForm productRegisterForm2 = new ProductRegisterForm();
        final ProductRegisterForm productRegisterForm3 = new ProductRegisterForm();

        productRegisterListForm.getProducts().add(productRegisterForm1);
        productRegisterListForm.getProducts().add(productRegisterForm2);
        productRegisterListForm.getProducts().add(productRegisterForm3);
    }

    @Test
    public void execute_NullInputData() {
        final List<Integer> response = this.elementListToRemoveStrategy.execute(null);
        Assert.assertEquals(Collections.emptyList(), response);
    }

    @Test
    public void execute() {
        final Object[] args = {1};
        final String message = "rejection test";

        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");

        bindingResult.rejectValue(ProductRegisterForm.NAME, RegisterFormErrors.EMPTY_NAME, args, message);

        final FieldError fieldError = bindingResult.getFieldError(ProductRegisterForm.NAME);
        final List<Integer> response = this.elementListToRemoveStrategy.execute(fieldError);

        Assert.assertEquals(1, response.get(0).intValue());
    }

    @Test
    public void execute_WithoutRejection() {
        final String message = "rejection test";
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");

        bindingResult.rejectValue(ProductRegisterForm.NAME, RegisterFormErrors.EMPTY_NAME, null, message);

        final FieldError fieldError = bindingResult.getFieldError(ProductRegisterForm.NAME);
        final List<Integer> response = this.elementListToRemoveStrategy.execute(fieldError);

        Assert.assertEquals(0, response.size());
    }
}