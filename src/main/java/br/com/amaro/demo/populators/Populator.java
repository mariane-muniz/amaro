package br.com.amaro.demo.populators;

/**
 *Standardization interface for the populator standard.
 *
 * @param <I> the input object that will serve as the basis of information
 * @param <O> The output object that will be built from the input data plus the transformations within the
 *           implementation of the populator
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
public interface Populator<I,O> {

    /**
     *Implementation responsible for requiring the implementation of the main method of converting the input object
     * into the output object
     *
     * @param input the input object that will serve as the basis of information
     * @return The output object that will be built from the input data plus the transformations within the
     *      implementation of the populator
     */
    O populate(I input);
}