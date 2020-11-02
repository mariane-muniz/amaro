//package br.com.amaro.demo.mappers;
//
//import br.com.amaro.demo.entities.Tag;
//import br.com.amaro.demo.forms.impl.ProductForm;
//import br.com.amaro.demo.strategy.IndexOfTagsStrategy;
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
//@SpringBootTest(classes = IndexOfTagsStrategy.class)
//public class ProductRegisterMapperTest {
//    @InjectMocks private ProductRegisterMapper productRegisterMapper;
//
//    private final ProductForm form = new ProductForm();
//    private final List<String> tagNameList = new ArrayList<>();
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
//    }
//
//    @org.junit.Test
//    public void map() {
//        final ProductParameter response = this.productRegisterMapper.map(this.form);
//        Assert.assertEquals(response.getName(), this.form.getName());
//        Assert.assertEquals(response.getId(), this.form.getId());
//        Assert.assertEquals(response.getTags(), this.form.getTags());
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void map_IllegalArgumentException() {
//        this.productRegisterMapper.map(null);
//    }
//}