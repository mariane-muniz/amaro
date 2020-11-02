package br.com.amaro.demo.validators;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.forms.ProductRegisterListForm;
import br.com.amaro.demo.forms.SearchSimilarProductForm;
import br.com.amaro.demo.forms.SearchSimilarProductListForm;
import br.com.amaro.demo.services.ProductService;
import br.com.amaro.demo.services.TagService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class responsible for validating the product and vector data list
 *
 * @see SearchSimilarProductListForm
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
public class SearchSimilarProductListFormValidator extends ProductRegisterListFormValidator {

    public SearchSimilarProductListFormValidator(final ProductService productService, final TagService tagService) {
        super(productService, tagService);
    }

    /**
     * It is the method responsible for validating whether the input data is compatible with the validator.
     *
     * @param aClass Entry form of the type {@link SearchSimilarProductListForm}
     * @return Returns whether or not the form was accepted for validation.
     */
    @Override
    public boolean supports(final Class<?> aClass) {
        return SearchSimilarProductListForm.class.equals(aClass);
    }

    /**
     * Method responsible for redefining the methods that will validate the attributes of the forms.
     *
     * @param productRegisterListForm form list with products information
     * @param errors object that stores the errors found during validation
     * @param invalidFormIndex global index of forms with error
     */
    @Override
    protected void validateFormAttributes(
            final ProductRegisterListForm productRegisterListForm,
            final Errors errors,
            final List<Integer> invalidFormIndex
    ) {
        super.validateFormAttributes(productRegisterListForm, errors, invalidFormIndex);
        this.validateTagsVectorCount((SearchSimilarProductListForm) productRegisterListForm, errors, invalidFormIndex);
    }

    /**
     * Method responsible for calling the secondary validation methods of the product identifier. If there is any form
     * with invalid identification, a rejection of type 'emptyId' is generated
     *
     * @param productRegisterListForm form list with products information
     * @param errors object that stores the errors found during validation
     * @param invalidFormIndex global index of forms with error
     */
    @Override
    protected void validateIds(
            final ProductRegisterListForm productRegisterListForm,
            final Errors errors,
            final List<Integer> invalidFormIndex) {

        final List<Integer> nullIdIndex = new ArrayList<>();

        this.validateNullIds(productRegisterListForm, nullIdIndex, invalidFormIndex);
        this.validateNotRegisteredIds(productRegisterListForm, nullIdIndex, invalidFormIndex);

        /* If there are product forms with invalid identifiers, a rejection is generated with the form
        indexes of the type 'emptyId */
        if (!CollectionUtils.isEmpty(nullIdIndex)) {
            errors.rejectValue(ProductRegisterForm.ID, RegisterFormErrors.EMPTY_ID,
                    nullIdIndex.toArray(), "Products without id or duplicated");
        }
    }

    /**
     * Method responsible for checking which forms have product identifiers not registered in the database.
     * If the product not exist in the database, the index of your form is added to the blacklist.
     *
     * @param productRegisterListForm form list with products information
     * @param productFormWithProductRegisteredIndex index of product forms with products not registered in the database
     * @param invalidFormIndex global index of forms with error
     */
    protected void validateNotRegisteredIds(
            final ProductRegisterListForm productRegisterListForm,
            final List<Integer> productFormWithProductRegisteredIndex,
            final List<Integer> invalidFormIndex
    ) {
        final List<ProductRegisterForm> productList = productRegisterListForm.getProducts();

        for(int i = 0; i < productList.size(); i++) {
            final ProductRegisterForm productRegisterForm = productList.get(i);
            final Integer productId = productRegisterForm.getId();

            /* from the product identifier the form is consulted in the database if the product exists.
            If the product not exist, it is added to the blacklist. */
            if (!ObjectUtils.isEmpty(productId)) {
                final Product product = this.productService.findByExternalId(productId);

                if (Objects.isNull(product)) {
                    if (!invalidFormIndex.contains(i)) {
                        invalidFormIndex.add(i);
                    }
                    if (!productFormWithProductRegisteredIndex.contains(i)) {
                        productFormWithProductRegisteredIndex.add(i);
                    }
                }
            }
        }
    }

    /**
     * This method is responsible for validating the amount of elements sent in the vector field. If the amount of
     * vectors sent is smaller than the amount of base tags, a rejection of the type 'invalidTagsVectorName' is
     * generated by the amount of incompatible vectors.
     *
     * @param searchSimilarProductListForm form with product information
     * @param errors object that stores the errors found during validation
     * @param invalidFormList global index of forms with error
     */
    private void validateTagsVectorCount(
            final SearchSimilarProductListForm searchSimilarProductListForm,
            final Errors errors,
            final List<Integer> invalidFormList
    ) {
        final List<Integer> invalidFormVectorSizeList = new ArrayList<>();
        final int availableTagsCount = this.tagService.countTotalTags();
        final List<Integer> invalidProductIdList = new ArrayList<>();
        final List<SearchSimilarProductForm> searchSimilarProductList = searchSimilarProductListForm.getSimilar();

        /* checks if the number of vectors in the form is the same as the number of tags registered in the base. */
        for (int i = 0; i < searchSimilarProductList.size(); i++) {
            final SearchSimilarProductForm form = searchSimilarProductList.get(i);
            final int[] formVector = form.getTagsVector();

            if(Objects.isNull(formVector) || formVector.length != availableTagsCount) {
                invalidFormList.add(i);
                invalidFormVectorSizeList.add(i);
            }
        }

        /* If the number of vectors in the form is different, a rejection is generated with the indexes of the forms */
        if (CollectionUtils.isEmpty(invalidProductIdList)) {
            final String message = "tagsVector need " + availableTagsCount + " elements in the array";
            errors.rejectValue(SearchSimilarProductForm.TAGS_VECTOR, RegisterFormErrors.INVALID_TAGS_VECTOR_NAME,
                    invalidFormVectorSizeList.toArray(), message);
        }
    }
}