package br.com.amaro.demo.services;

import br.com.amaro.demo.entities.Tag;
import br.com.amaro.demo.repositories.TagRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = TagService.class)
public class TagServiceTest {
    @InjectMocks private TagService tagService;

    @Mock private TagRepository tagRepository;
    @Mock final private List<Tag> tagList = new ArrayList<>();
    @Mock final private List<String> tagNameList = new ArrayList<>();

    @Before
    public void init() {
        final Tag tag = new Tag();

        tag.setId(1);
        tag.setName("veludo");

        tagNameList.add("veludo");
        tagList.add(tag);

        when(this.tagRepository.findByNameIn(tagNameList)).thenReturn(tagList);
        when(this.tagRepository.count()).thenReturn(Long.valueOf(1));
    }

    @Test
    public void countTotalTags() {
        final int response = this.tagService.countTotalTags();
        Assert.assertEquals(1, response);
    }

    @Test
    public void getByNames() {
        final List<Tag> response = this.tagService.getByNames(this.tagNameList);
        Assert.assertEquals(response, this.tagList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByNames_IllegalArgumentException() {
        this.tagService.getByNames(null);
    }
}