package cn.xiaochi.service.impl;

import cn.xiaochi.dataobject.ProductInfo;
import cn.xiaochi.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {

    @Autowired
    private ProductInfoServiceImpl productInfoServiceImpl;

    @Test
    public void findOne() {
        ProductInfo one = productInfoServiceImpl.findOne("1");
        Assert.assertEquals("1",one.getProductId());
    }

    @Test
    public void findAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "productId");
        PageRequest pageRequest = new PageRequest(0, 2, sort);
        Page<ProductInfo> page = productInfoServiceImpl.findAll(pageRequest);
        Assert.assertNotEquals(0,page.getTotalElements());
        System.out.println("总页数：" + page.getTotalPages());
        System.out.println("总条数：" + page.getTotalElements());
        System.out.println("当前页：" + (page.getNumber()+1));// 从0页开始，+1
        System.out.println("当前页数据：" + page.getContent());
        System.out.println("当前页总条数：" + page.getNumberOfElements());
    }

    @Test
    public void findByProductStatus() {
        List<ProductInfo> productInfos = productInfoServiceImpl.findUpAll();
        Assert.assertNotEquals(0,productInfos.size());
    }

    @Test
    public void save() {
        ProductInfo product = new ProductInfo();
        product.setProductId("3");
        product.setProductName("测试商品2");
        product.setProductPrice(new BigDecimal(2));
        product.setProductStock(100);
        product.setProductDescription("很好吃的虾");
        product.setProductIcon("http://xxxxx.jpg");
        product.setProductStatus(0);
        product.setCategoryType(2);

        ProductInfo result = productInfoServiceImpl.save(product);
        System.out.println(result);
        Assert.assertNotNull(result);
    }
}
