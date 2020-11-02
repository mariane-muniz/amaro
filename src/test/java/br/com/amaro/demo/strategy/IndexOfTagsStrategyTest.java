package br.com.amaro.demo.strategy;

import br.com.amaro.demo.entities.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = IndexOfTagsStrategy.class)
public class IndexOfTagsStrategyTest {
    @InjectMocks private IndexOfTagsStrategy indexOfTagsStrategy;

    private final List<Tag> tagNameList = new ArrayList<>();

    @Before
    public void init() {
        final Tag tag = new Tag();
        tag.setName("metal");
        tag.setId(1);

        this.tagNameList.add(tag);
    }

    @Test
    public void getIndexOfTags() {
        List<Integer> response = this.indexOfTagsStrategy.getIndexOfTags(this.tagNameList);
        Assert.assertEquals(1, response.size());
    }
}