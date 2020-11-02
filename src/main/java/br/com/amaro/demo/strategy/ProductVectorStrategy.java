package br.com.amaro.demo.strategy;

import br.com.amaro.demo.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Class responsible for extracting an array of vectors from a list of indexes
 *
 * @see TagService
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ProductVectorStrategy {
    private final TagService tagService;

    /**
     * Method responsible for creating a new vector with the same number of elements in the database and modify
     * the elements that are active in the index
     *
     * @param index a list of tag indexes
     * @return an array of vectors
     */
    public int[] getVectors(final List<Integer> index) {
        Assert.notNull(index, "vectors cannot be empty");
        final int availableTagsSize = this.tagService.countTotalTags();
        final int[] vectorList = new int[availableTagsSize];
        index.forEach(i -> vectorList[i] = 1);
        return vectorList;
    }
}