package br.com.amaro.demo.services;

import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Service responsible for operations with the tag objects
 *
 * @see TagRepository
 * @see Tag
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation= Propagation.REQUIRED, noRollbackFor=Exception.class)
public class TagService {
    private final TagRepository tagRepository;

    /**
     * Method responsible for recovering the total number of tags registered in the database
     *
     * @return total of the tags registered in the database
     */
    public int countTotalTags() {
        return (int) this.tagRepository.count();
    }

    /**
     * Method responsible for retrieving a list of tags from a list of tag names.
     *
     * @see Tag
     *
     * @param names a list of tag names
     * @return a list of tag entities
     */
    public List<Tag> getByNames(final List<String> names) {
        Assert.notEmpty(names, "names cannot be empty");
        return this.tagRepository.findByNameIn(names);
    }

    /**
     * Method responsible for recovering all registered tags from the database
     *
     * @return list of all the tags registered in the database
     */
    public Iterable<Tag> findAll() {
        return this.tagRepository.findAll();
    }
}