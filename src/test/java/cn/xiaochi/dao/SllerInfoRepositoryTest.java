package cn.xiaochi.dao;

import cn.xiaochi.dataobject.SellerInfo;
import cn.xiaochi.util.KeyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SllerInfoRepositoryTest {

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    private final String openId = "123456";

    @Test
    public void findByOpenid() {
        SellerInfo result = sellerInfoRepository.findByOpenid(openId);
        Assert.assertNotNull(result);
    }

    @Test
    public void save() {
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setId(KeyUtil.genUniqueKey());
        sellerInfo.setUsername("admin");
        sellerInfo.setPassword("admin");
        sellerInfo.setOpenid(openId);
        SellerInfo result = sellerInfoRepository.save(sellerInfo);
        Assert.assertNotNull(result);
    }
}