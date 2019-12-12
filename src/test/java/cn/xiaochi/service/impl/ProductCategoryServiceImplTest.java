package cn.xiaochi.service.impl;


import cn.xiaochi.dataobject.ProductCategory;
import cn.xiaochi.service.ProductCategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryServiceImplTest {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    public void findOne() {
        ProductCategory one = productCategoryService.findOne(2);
        Assert.assertEquals(2,one.getCategoryId().intValue());
    }

    @Test
    public void findAll() {
        List<ProductCategory> all = productCategoryService.findAll();
        all.forEach(System.out::println);
    }

    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list = new ArrayList<>();
        list.add(5);
        List<ProductCategory> productCategories = productCategoryService.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,productCategories.size());
    }

    @Test
    public void save() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("测试类型");
        productCategory.setCategoryType(10000);
        ProductCategory result = productCategoryService.save(productCategory);
        Assert.assertNotNull(result);
    }
}
