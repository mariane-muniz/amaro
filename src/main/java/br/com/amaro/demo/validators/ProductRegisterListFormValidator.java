package br.com.amaro.demo.validators;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.forms.ProductRegisterForm;
import br.com.amaro.demo.forms.ProductRegisterListForm;
import br.com.amaro.demo.services.ProductService;
import br.com.amaro.demo.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class responsible for validating registration forms for new products
 *
 * @see ProductRegisterListForm
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ProductRegisterListFormValidator implements Validator {
    protected final ProductService productService;
    protected final TagService tagService;

    /**
     * This method is responsible for evaluating whether the entry object is compatible with the type of form accepted
     * for validation. In this case the object has to be of type {@link ProductRegisterListForm} or an object that extends
     * this class.
     *
     * @param clazz Entry form of the type {@link ProductRegisterListForm}
     * @return Returns whether or not the form was accepted for validation.
     * @see ProductRegisterListForm
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return ProductRegisterListForm.class.equals(clazz);
    }

    /**
     * Main method of form validation.
     * Through it that the form is received in the class.
     *
     * @param objectForm form with product information
     * @param errors object that stores the errors found during validation
     */
    @Override
    public void validate(final Object objectForm, final Errors errors) {
        Assert.notNull(objectForm, "objectForm cannot be null");
        Assert.notNull(errors, "errors cannot be null");

        final ProductRegisterListForm productListForm = (ProductRegisterListForm) objectForm;
        final List<ProductRegisterForm> products = productListForm.getProducts();
        final List<Integer> invalidIndex = new ArrayList<>();

        if (!CollectionUtils.isEmpty(products)) {
            this.validateFormAttributes((ProductRegisterListForm) objectForm, errors, invalidIndex);

            if (!CollectionUtils.isEmpty(invalidIndex)) {
                errors.rejectValue(RegisterFormErrors.INVALID_INDEX, RegisterFormErrors.INVALID_INDEX,
                        invalidIndex.toArray(), "The list has invalid elements");
            }
        }
    }

    /**
     * Method responsible for calling the validations of the form fields.
     * @param productRegisterListForm form list with products information
     * @param errors object that stores the errors found during validation
     * @param invalidIndex global index of forms with error
     */
    protected void validateFormAttributes(
            final ProductRegisterListForm productRegisterListForm,
            final Errors errors,
            final List<Integer> invalidIndex
    ) {
        this.validateIds(productRegisterListForm, errors, invalidIndex);
        this.validateNames(productRegisterListForm, errors, invalidIndex);
        this.validateTagValues(productRegisterListForm, errors, invalidIndex);
    }

    /**
     * The main method responsible for validating the product registration identifier. It is responsible for executing
     * the methods that validate null fields and also duplicate fields in the database
     * @param productRegisterListForm form list with products information
     * @param errors object that stores the errors found during validation
     * @param invalidIndex global index of forms with error
     */
    protected void validateIds(
            final ProductRegisterListForm productRegisterListForm,
            final Errors errors,
            final List<Integer> invalidIndex
    ) {
        final List<Integer> nullIdIndex = new ArrayList<>();

        this.validateNullIds(productRegisterListForm, nullIdIndex, invalidIndex);
        this.validateRegisteredIds(productRegisterListForm, nullIdIndex, invalidIndex);

        /* If there are product forms with invalid identifiers, a rejection is generated with the form
        indexes of the type 'emptyId */
        if (!CollectionUtils.isEmpty(nullIdIndex)) {
            errors.rejectValue(ProductRegisterForm.ID, RegisterFormErrors.EMPTY_ID,
                    nullIdIndex.toArray(), "Products without id or duplicated");
        }
    }

    /**
     * The method is responsible for validating whether there are products in the list of forms with the null identifier.
     * If there is a product with the null identifier, the index of its form is added to a list of invalid form indexes
     * and also to a list of forms with invalid identifiers.
     *
     * @param productRegisterListForm form list with products information
     * @param formWithoutIdIndexList object that stores invalid form index with duplicated id error type
     * @param invalidIndex global index of forms with error
     * @see ProductRegisterForm
     */
    protected void validateNullIds(
            final ProductRegisterListForm productRegisterListForm,
            final List<Integer> formWithoutIdIndexList,
            final List<Integer> invalidIndex
    ) {
        final List<ProductRegisterForm> productRegisterFormLists = productRegisterListForm.getProducts();

        for(int i = 0; i < productRegisterFormLists.size(); i++) {
            final ProductRegisterForm productRegisterForm = productRegisterFormLists.get(i);
            final Integer productId = productRegisterForm.getId();

            if (Objects.isNull(productId)) {
                formWithoutIdIndexList.add(i);
                if (!invalidIndex.contains(i)) {
                    invalidIndex.add(i);
                }
            }
        }
    }

    /**
     * The method is responsible for validate if there are product identifiers in the forms already registered in the
     * database. If there are products with an identifier already registered, the index of your form is added to a list
     * of invalid forms (nullIdIndex) and also to a list of duplicate error indexes (invalidIndex).
     *
     * @param productRegisterListForm form list with products information
     * @param nullIdIndex object that stores invalid form index with duplicated id error type
     * @param invalidIndex global index of forms with error
     * @see ProductRegisterForm
     */
    protected void validateRegisteredIds(
            final ProductRegisterListForm productRegisterListForm,
            final List<Integer> nullIdIndex,
            final List<Integer> invalidIndex
    ) {
        final List<ProductRegisterForm> productRegisterFormLists = productRegisterListForm.getProducts();
        final List<Integer> ids = productRegisterFormLists.stream().map(ProductRegisterForm::getId).collect(Collectors.toList());
        final List<Product> databaseProductList = this.productService.findByExternalIdIn(ids);

        /* If there are products on the base with these identifiers the indexes of the forms with these products
        are selected and the list of error indexes is added. */
        if (!CollectionUtils.isEmpty(databaseProductList)) {
            final List<Integer> duplicatedIds = databaseProductList.stream()
                    .map(Product::getExternalId)
                    .collect(Collectors.toList());

            productRegisterFormLists.forEach(productForm ->  {
                final Integer productId = productForm.getId();
                if (duplicatedIds.contains(productId)) {
                    final int index = productRegisterFormLists.indexOf(productForm);
                    if(!nullIdIndex.contains(index)) {
                        nullIdIndex.add(index);
                        invalidIndex.add(index);
                    }
                }
            });
        }
    }

    /**
     * The purpose of this validation is to find in the list of forms names of products that are null. If any form has
     * this condition a "emptyName" type rejection is generated
     *
     * @param productRegisterListForm form list with products information
     * @param errors object that stores the errors found during validation
     * @param invalidIndex global index of forms with error
     * @see ProductRegisterForm
     */
    protected void validateNames(
            final ProductRegisterListForm productRegisterListForm,
            final Errors errors,
            final List<Integer> invalidIndex
    ) {
        final List<Integer> invalidProductNameIds = new ArrayList<>();
        final List<ProductRegisterForm> forms = productRegisterListForm.getProducts();

        /* Looking for products with null names. If the name is null the form index is added to a list of form
        indexes with invalid name. */
        for (int i = 0; i < forms.size(); i++) {
            final ProductRegisterForm productRegisterForm = forms.get(i);
            final String productName = productRegisterForm.getName();
            final Integer productId = productRegisterForm.getId();

            if (StringUtils.isEmpty(productName) && !ObjectUtils.isEmpty(productId)) {
                invalidProductNameIds.add(i);
                invalidIndex.add(i);
            }
        }

        /* If there are invalid names in the form list, a rejection is generated with the the indexes of the
        forms with this problem. */
        if (!CollectionUtils.isEmpty(invalidProductNameIds)) {
            errors.rejectValue(ProductRegisterForm.NAME, RegisterFormErrors.EMPTY_NAME,
                    invalidProductNameIds.toArray(), "Product name cannot be null");
        }
    }

    /**
     * The purpose of validation is to search for tags that are not registered in the database. If there is an
     * unregistered tag in one of the forms, a rejection is generated with the index of the item with the problem
     * and also in the message the invalid tags.
     *
     * @param productRegisterListForm form list with products information
     * @param errors object that stores the errors found during validation
     * @param invalidIndex global index of forms with error
     * @see ProductRegisterForm
     */
    protected void validateTagValues(
            final ProductRegisterListForm productRegisterListForm,
            final Errors errors,
            final List<Integer> invalidIndex
    ) {
        final List<ProductRegisterForm> products = productRegisterListForm.getProducts();
        final List<Integer> invalidFormTags = new ArrayList<>();
        final List<String> availableTagNames = new ArrayList<>();
        final Iterable<Tag> availableTags = this.tagService.findAll();
        final List<String> invalidTagNames = new ArrayList<>();
        /* From the bank registered tags a list is created with the names of the tags registered in the base. */
        availableTags.forEach(availableTag -> availableTagNames.add(availableTag.getName()));

        /* Go through the list of forms, select the tag names from the form and compare them with the tag names
        registered in the base. If one does not exist, the tag name is added to a list of invalid names and the form
        index is added to a list of invalid indexes. */
        for (int i =0; i < products.size(); i++) {
            final ProductRegisterForm productRegisterForm = products.get(i);
            final Integer productId = productRegisterForm.getId();
            final List<String> tagNames = productRegisterForm.getTags();
            final List<String> formInvalidTagNames = tagNames.stream()
                    .filter(tagName -> !availableTagNames.contains(tagName))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(formInvalidTagNames) && Objects.nonNull(productId)) {
                invalidFormTags.add(i);
                invalidIndex.add(i);

                /* verify if the tag name not exist in the blacklist */
                formInvalidTagNames.forEach(formInvalidTagName -> {
                    if (!invalidTagNames.contains(formInvalidTagName)) {
                        invalidTagNames.add(formInvalidTagName);
                    }
                });
            }
        }

        /* If there are invalid tags in the form list, a rejection is generated with the invalid tag list and also with
        the indexes of the forms with this problem. */
        if (!CollectionUtils.isEmpty(invalidFormTags)) {
            final Object[] invalidTagNameList =invalidTagNames.toArray();
            final String invalidTagString = Arrays.stream(invalidTagNameList)
                    .map(String::valueOf).collect(Collectors.joining(", "));
            final String message = "Invalid tag names for products [" + invalidTagString + "]";
            errors.rejectValue(ProductRegisterForm.TAGS, RegisterFormErrors.INVALID_TAG_NAME,
                    invalidFormTags.toArray(), message);
        }
    }
}