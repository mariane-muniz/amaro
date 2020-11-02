//package br.com.amaro.demo.mappers;
//
//import br.com.amaro.demo.entities.Tag;
//import br.com.amaro.demo.forms.SimilarProductForm;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest(classes = SimilarProductMapper.class)
//public class SimilarProductMapperTest {
//    @InjectMocks private SimilarProductMapper similarProductMapper;
//
//    private final SimilarProductForm form = new SimilarProductForm();
//    private final List<String> tagNameList = new ArrayList<>();
//    private final int[] tagsVector = {0,1,0,0};
//
//    @Before
//    public void init() {
//        final Tag tag = new Tag();
//
//        tag.setId(1);
//        tag.setName("metal");
//
//        tagNameList.add("metal");
//
//        this.form.setId(1);
//        this.form.setName("Product name");
//        this.form.setTags(tagNameList);
//        this.form.setTagsVector(tagsVector);
//    }
//
//    @org.junit.Test
//    public void map() {
//        final ProductParameter response = this.similarProductMapper.map(this.form);
//        Assert.assertEquals(response.getName(), this.form.getName());
//        Assert.assertEquals(response.getId(), this.form.getId());
//        Assert.assertEquals(response.getTags(), this.form.getTags());
//        Assert.assertEquals(response.getTagsVector(), this.form.getTagsVector());
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void map_IllegalArgumentException() {
//        this.similarProductMapper.map(null);
//    }
//}