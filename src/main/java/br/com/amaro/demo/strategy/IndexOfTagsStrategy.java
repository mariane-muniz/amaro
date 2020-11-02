package br.com.amaro.demo.strategy;

import br.com.amaro.demo.entities.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for returning the tag indexes
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class IndexOfTagsStrategy {

    /**
     * Main method responsible for retrieving the tags registered in the bank from the object's unique identifier
     * and return a list with the indexes.
     *
     * @param tags a list of entity tags
     * @return a list of tag indexes
     */
    public List<Integer> getIndexOfTags(final List<Tag> tags) {
        Assert.notEmpty(tags, "Tags cannot be empty");
        final List<Integer> vectors = new ArrayList<>();
        tags.forEach(tag -> vectors.add(tag.getId() - 1));
        return vectors;
    }
}