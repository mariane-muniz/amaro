package br.com.amaro.demo.repositories;

import br.com.amaro.demo.entities.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for the database transactions of tag entities
 *
 * @see Tag
 *
 * @author Marine Muniz
 * @version 1.0.0
 */
@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    /**
     * Method responsible for retrieving a list of tags from a list of tag names.
     *
     * @see Tag
     *
     * @param names a list of tag names
     * @return a list of tag entities
     */
    List<Tag> findByNameIn(final List<String> names);
}