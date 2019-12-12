package cn.xiaochi.dao;

import cn.xiaochi.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Test
    public void testFindByProductStatus() {
    }

    @Test
    public void testFindByProductIdIn() {
//        List<String> ids = new ArrayList<>();
//        ids.add("1");
//        ids.add("2");
//        List<ProductInfo> result = productInfoRepository.findByProductIdIn(ids);
//        Assert.assertNotEquals(0,result.size());
    }
}