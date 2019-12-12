package cn.xiaochi.service.impl;

import cn.xiaochi.dao.OrderDetailRepository;
import cn.xiaochi.dao.OrderMasterRepository;
import cn.xiaochi.dao.ProductInfoRepository;
import cn.xiaochi.dataobject.OrderDetail;
import cn.xiaochi.dataobject.OrderMaster;
import cn.xiaochi.dataobject.ProductInfo;
import cn.xiaochi.dto.CartDto;
import cn.xiaochi.dto.OrderDto;
import cn.xiaochi.enums.OrderStatusEnum;
import cn.xiaochi.enums.PayStatusEnum;
import cn.xiaochi.enums.ProductStatusEnum;
import cn.xiaochi.enums.ResultEnum;
import cn.xiaochi.exception.SellException;
import cn.xiaochi.service.OrderSerivce;
import cn.xiaochi.service.PayService;
import cn.xiaochi.service.ProductInfoService;
import cn.xiaochi.service.PushMessageService;
import cn.xiaochi.util.KeyUtil;
import cn.xiaochi.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderSericeImpl implements OrderSerivce {

    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository ordermasterRepository;
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private PayService payService;
    @Autowired
    private PushMessageService pushMessageService;
    @Autowired
    private WebSocket webSocket;

    /** 创建订单. */
    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        // 1. 查询商品 (数据、价格)
        for (OrderDetail orderDetail : orderDto.getOrderDetailList()){
            ProductInfo product = productInfoRepository.findOne(orderDetail.getProductId());
            if (product == null){
                throw new SellException(ProductStatusEnum.PRODUCT_NOT_EXIST.getMessage(),ProductStatusEnum.PRODUCT_NOT_EXIST.getCode());
            }
            // 计算总价
            orderAmount = product.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);

            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(product,orderDetail);
            // 订单详情表
            orderDetailRepository.save(orderDetail);
        }
        // 订单表
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderId(orderId);
        orderDto.setOrderAmount(orderAmount);
        orderDto.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderDto.setPayStatus(PayStatusEnum.WAIT.getCode());
        BeanUtils.copyProperties(orderDto,orderMaster);
        ordermasterRepository.save(orderMaster);
        // 2.扣库存
        List<CartDto> cartList = orderDto.getOrderDetailList().stream().map(orderDetail -> new CartDto(orderDetail.getProductId(), orderDetail.getProductQuantity())).collect(Collectors.toList());
        productInfoService.decreaseStock(cartList);

        // 3.发送 websocket 消息
        webSocket.sendMessage("有新的订单，订单号：" + orderDto.getOrderId());
        return orderDto;
    }

    /** 查询单个订单. */
    @Override
    public OrderDto findOne(String orderId) throws SellException {
        OrderMaster orderMaster = ordermasterRepository.findOne(orderId);
        if (orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST.getMessage(),ResultEnum.ORDER_NOT_EXIST.getCode());
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderMaster.getOrderId());
        if (CollectionUtils.isEmpty(orderDetails)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST.getMessage(),ResultEnum.ORDERDETAIL_NOT_EXIST.getCode());
        }
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster,orderDto);
        orderDto.setOrderDetailList(orderDetails);
        return orderDto;
    }

    /** 查询订单列表. 分页 */
    @Override
    public Page<OrderDto> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = ordermasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        // Page<OrderMaster> => Page<OrderDto>
        List<OrderDto> orderDtoList = orderMasterPage.getContent().stream().map(orderMaster -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(orderMaster, orderDto);
            return orderDto;
        }).collect(Collectors.toList());
        return new PageImpl<>(orderDtoList,pageable,orderMasterPage.getTotalElements());
    }

    /** 取消订单. */
    @Override
    @Transactional
    public OrderDto cancel(OrderDto orderDto) {
        OrderMaster orderMaster = new OrderMaster();
        // 1.判断订单状态
        if (!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.info("【取消订单】订单状态不正确, orderId={}, orderStatus={}",orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR.getMessage(),ResultEnum.ORDER_STATUS_ERROR.getCode());
        }
        // 2.更新订单状态
        orderDto.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDto,orderMaster);
        OrderMaster updateResult = ordermasterRepository.save(orderMaster);
        if (updateResult == null){
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL.getMessage(),ResultEnum.ORDER_UPDATE_FAIL.getCode());
        }
        // 3.返回库存
        if (CollectionUtils.isEmpty(orderDto.getOrderDetailList())){
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDto);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY.getMessage(),ResultEnum.ORDER_DETAIL_EMPTY.getCode());
        }
        List<CartDto> cartDtos = orderDto.getOrderDetailList().stream().map(orderDetail -> new CartDto(orderDetail.getProductId(), orderDetail.getProductQuantity())).collect(Collectors.toList());
        productInfoService.increaseStock(cartDtos);
        // 4.如果已支付, 需要退款
        if (orderDto.getOrderStatus().equals(OrderStatusEnum.FINISHED.getCode())){
            // todo:退款
            payService.refund(orderDto);
        }
        return orderDto;
    }

    /** 完结订单. */
    @Override
    @Transactional
    public OrderDto finish(OrderDto orderDto) {
        // 1.判断订单状态
        if (!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR.getMessage(),ResultEnum.ORDER_STATUS_ERROR.getCode());
        }
        // 2.修改订单状态
        orderDto.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDto,orderMaster);
        OrderMaster updateResult = ordermasterRepository.save(orderMaster);
        if (updateResult == null){
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL.getMessage(),ResultEnum.ORDER_UPDATE_FAIL.getCode());
        }
        // 推送模板消息
        pushMessageService.orderStatus(orderDto);
        return orderDto;
    }

    /** 支付订单. */
    @Override
    @Transactional
    public OrderDto paid(OrderDto orderDto) {
        // 1.判断订单状态
        if (!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR.getMessage(),ResultEnum.ORDER_STATUS_ERROR.getCode());
        }
        // 2.判断订单支付状态
        if (!orderDto.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDto);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR.getMessage(),ResultEnum.ORDER_PAY_STATUS_ERROR.getCode());
        }
        orderDto.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDto,orderMaster);
        OrderMaster updateResult = ordermasterRepository.save(orderMaster);
        if (updateResult == null){
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL.getMessage(),ResultEnum.ORDER_UPDATE_FAIL.getCode());
        }
        return orderDto;
    }

    public Page<OrderDto> findAdminList(Pageable pageable){
        Page<OrderMaster> orderMasterPage = ordermasterRepository.findAll(pageable);
        // Page<OrderMaster> => Page<OrderDto>
        List<OrderDto> orderDtoList = orderMasterPage.getContent().stream().map(order -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);
            return orderDto;
        }).collect(Collectors.toList());
        return new PageImpl<>(orderDtoList,pageable,orderMasterPage.getTotalElements());
    }
}
