package cn.xiaochi.service.impl;

import cn.xiaochi.dataobject.OrderDetail;
import cn.xiaochi.dto.OrderDto;
import cn.xiaochi.enums.OrderStatusEnum;
import cn.xiaochi.enums.PayStatusEnum;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderSericeImplTest {

    @Autowired
    private OrderSericeImpl orderSerice;

    private final String BUYER_OPENID = "22222222";

    private final String ORDER_ID = "1575786947243106773";


    @Test
    public void create() {
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName("小二");
        orderDto.setBuyerAddress("幕课网");
        orderDto.setBuyerPhone("123456789012");
        orderDto.setBuyerOpenid(BUYER_OPENID);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProductId("2");
        orderDetail.setProductQuantity(2);
        orderDetailList.add(orderDetail);
        orderDto.setOrderDetailList(orderDetailList);
        orderSerice.create(orderDto);
    }

    @Test
    public void findOne() {
        OrderDto result = orderSerice.findOne(ORDER_ID);
        Assert.assertEquals(ORDER_ID,result.getOrderId());
    }

    @Test
    public void findList() {
        Sort sort = new Sort(Sort.Direction.DESC, "orderId");
        Pageable pageRequest = new PageRequest(0, 1,sort);
        Page<OrderDto> list = orderSerice.findList(BUYER_OPENID, pageRequest);
        Assert.assertNotEquals(0,list.getTotalElements());
    }

    @Test
    public void cancel() {
        OrderDto orderDto = orderSerice.findOne(ORDER_ID);
        OrderDto result = orderSerice.cancel(orderDto);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDto orderDto = orderSerice.findOne(ORDER_ID);
        OrderDto result = orderSerice.finish(orderDto);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(),result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDto orderDto = orderSerice.findOne(ORDER_ID);
        OrderDto result = orderSerice.paid(orderDto);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(),result.getPayStatus());
    }

    @Test
    public void testFindList() {
        PageRequest pageRequest = new PageRequest(0, 5);
        Page<OrderDto> orderSericeAdminList = orderSerice.findAdminList(pageRequest);
        Assert.assertTrue("【查询所有订单列表】",orderSericeAdminList.getTotalElements() > 0);
    }
}