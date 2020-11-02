package br.com.amaro.demo.populators.impl;

import br.com.amaro.demo.dtos.InvalidProductData;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.forms.ProductRegisterListForm;
import br.com.amaro.demo.validators.RegisterFormErrors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = InvalidProductDataPopulator.class)
public class InvalidProductDataPopulatorTest {
    @InjectMocks InvalidProductDataPopulator invalidProductDataPopulator;

    @Test
    public void populate() {
        final Object[] invalidIndex = {0};
        final ProductRegisterListForm productRegisterListForm = new ProductRegisterListForm();
        final BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");

        bindingResult.rejectValue(ProductRegisterForm.NAME, RegisterFormErrors.EMPTY_NAME, invalidIndex, "test");
        final InvalidProductData response = this.invalidProductDataPopulator.populate(bindingResult);

        Assert.assertNotNull(response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_IllegalArgumentException() {
        this.invalidProductDataPopulator.populate(null);
    }
}