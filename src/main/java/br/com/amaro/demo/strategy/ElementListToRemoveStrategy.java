package br.com.amaro.demo.strategy;

import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class responsible for listing the indexes of forms that are in error
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
public class ElementListToRemoveStrategy {

    /**
     * Method responsible for finding the indexes of forms with errors in the list of forms
     *
     * @param fieldError javax object with rejection form information
     * @return list with list form indexes with errors
     */
    public List<Integer> execute(final FieldError fieldError) {
        final List<Integer> elementIndexToRemoveList = new ArrayList<>();

        if (Objects.nonNull(fieldError)) {
            final Object[] args = fieldError.getArguments();

            if (Objects.nonNull(args) && args.length > 0) {
                for (Object i : args) {
                    elementIndexToRemoveList.add((Integer) i);
                }
            }
        }
        return elementIndexToRemoveList;
    }
}