package cn.xiaochi.dao;

import cn.xiaochi.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrdermasterRepositoryTest {

    @Autowired
    private OrderMasterRepository ordermasterRepository;

    private final String OPENID = "110110";


    @Test
    public void testFindByBuyerOpenid() {
        Sort sort = new Sort(Sort.Direction.ASC, "orderId");
        Pageable pageRequest = new PageRequest(0, 2,sort);

        Page<OrderMaster> order =  ordermasterRepository.findByBuyerOpenid(OPENID,pageRequest);
        Assert.assertNotEquals(0,order.getTotalElements());
    }

    @Test
    public void testSave(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("12345678");
        orderMaster.setBuyerName("小二");
        orderMaster.setBuyerPhone("123456789123");
        orderMaster.setBuyerAddress("测试");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setOrderAmount(new BigDecimal(2.5));

        OrderMaster result = ordermasterRepository.save(orderMaster);
        Assert.assertNotNull(result);
    }
}