package br.com.amaro.demo.controllers;

import br.com.amaro.demo.DemoApplication;
import br.com.amaro.demo.dtos.ProductCreatedResponseData;
import br.com.amaro.demo.dtos.SimilarListProductResponseData;
import br.com.amaro.demo.facades.ProductFacade;
import br.com.amaro.demo.forms.ProductRegisterListForm;
import br.com.amaro.demo.forms.SearchSimilarProductForm;
import br.com.amaro.demo.parameters.ProductRegisterParameter;
import br.com.amaro.demo.parameters.SearchSimilarProductParameter;
import br.com.amaro.demo.validators.ProductRegisterListFormValidator;
import br.com.amaro.demo.validators.SearchSimilarProductListFormValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {DemoApplication.class})
public class SimilarProductControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    ProductFacade productFacade;
    @MockBean(name = "productRegisterListFormValidator")
    ProductRegisterListFormValidator productRegisterListFormValidator;
    @MockBean(name = "searchSimilarProductListFormValidator")
    SearchSimilarProductListFormValidator searchSimilarProductListFormValidator;

    private MockMvc mockMvc;
    private BeanPropertyBindingResult bindingResult;
    private final ProductRegisterListForm productRegisterListForm = new ProductRegisterListForm();
    private final SearchSimilarProductForm searchSimilarProductForm = new SearchSimilarProductForm();

    @Before
    public void init() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
        this.bindingResult = new BeanPropertyBindingResult(productRegisterListForm, "productRegisterListForm");
    }

    @Test
    public void createProduct() {
        final ProductCreatedResponseData productCreatedResponseData = new ProductCreatedResponseData();
        Mockito
                .doNothing()
                .when(this.productRegisterListFormValidator).validate(this.productRegisterListForm, this.bindingResult);
        Mockito
                .when(this.productFacade.createProducts(Mockito.any(ProductRegisterParameter.class)))
                .thenReturn(productCreatedResponseData);
        try {
            mockMvc.perform(post(URI.create("/product-create"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(this.productRegisterListForm)))
                    .andExpect(status().isOk());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void search() {
        final SimilarListProductResponseData similarListProductResponseData = new SimilarListProductResponseData();
        Mockito
                .when(this.productFacade.getSimilarProducts(Mockito.any(SearchSimilarProductParameter.class)))
                .thenReturn(similarListProductResponseData);
        Mockito
                .doNothing()
                .when(this.productRegisterListFormValidator).validate(this.productRegisterListForm, this.bindingResult);
        try {
            mockMvc.perform(post(URI.create("/similar"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsBytes(this.searchSimilarProductForm)))
                    .andExpect(status().isOk());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}