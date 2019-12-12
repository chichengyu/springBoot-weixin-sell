package cn.xiaochi.controller;

import cn.xiaochi.dataobject.OrderDetail;
import cn.xiaochi.dto.OrderDto;
import cn.xiaochi.enums.ResultEnum;
import cn.xiaochi.form.OrderForm;
import cn.xiaochi.service.OrderSerivce;
import cn.xiaochi.util.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
public class BuyerOrderController {

    private final Logger logger = LoggerFactory.getLogger(BuyerOrderController.class);
    @Autowired
    private OrderSerivce orderSerivce;

    /**
     * 创建订单
     * @param orderForm @Valid绑定验证对象
     * @param bindingResult BindingResult 错误信息对象
     * @return
     */
    @PostMapping("/create")
    public Response create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            logger.error("【创建订单】参数不正确, orderForm={}", orderForm);
            // 获取错误信息
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            // throw new SellException(errorMsg,ResultEnum.PARAM_ERROR.getCode());
            return Response.error(errorMsg);
        }

        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenid());

        // Gson 解析 list 集合
        Gson gson = new Gson();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try{
            orderDetailList = gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());
            if (CollectionUtils.isEmpty(orderDetailList)){
                return Response.error(ResultEnum.CART_EMPTY.getMessage());
            }
            orderDto.setOrderDetailList(orderDetailList);
            OrderDto createResult = orderSerivce.create(orderDto);
            Map<String, String> map = new HashMap<>();
            map.put("orderId",createResult.getOrderId());
            return Response.success(map);
        }catch (Exception e){
            logger.error("【对象转换】错误, string={}", orderForm.getItems());
            return Response.error(ResultEnum.PARAM_ERROR.getMessage());
        }
    }

    /**
     * 查询当前openid订单列表
     * @param openid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public Response list(@RequestParam("openid")String openid,
                         @RequestParam(value = "page",defaultValue = "0")Integer page,
                         @RequestParam(value = "size",defaultValue = "5")Integer size){
        if (StringUtils.isEmpty(openid)){
            logger.error("【查询订单列表】，openid不能为空");
            return Response.error(ResultEnum.PARAM_ERROR.getMessage());
        }
        Sort sort = new Sort(Sort.Direction.DESC, "orderId");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Page<OrderDto> list = orderSerivce.findList(openid, pageRequest);
        return Response.success(list);
    }

    /**
     * 订单详情
     * @param openid
     * @param orderId
     * @return
     */
    @RequestMapping("/detail")
    public Response detail(@RequestParam("openid")String openid,@RequestParam("orderId")String orderId){
        OrderDto orderDto = orderSerivce.findOne(orderId);
        if (orderDto == null){
            logger.error("【取消订单】查不到该订单, orderId={}", orderId);
            return Response.error(ResultEnum.ORDER_NOT_EXIST.getMessage());
        }
        if (!orderDto.getBuyerOpenid().equals(openid)){
            logger.error("【查询订单】订单的openid不一致. openid={}, orderDTO={}", openid, orderDto);
            return Response.error(ResultEnum.ORDER_OWNER_ERROR.getMessage());
        }
        return Response.success(orderDto);
    }

    /**
     * 取消订单
     * @param openid
     * @param orderId
     * @return
     */
    @GetMapping("/cancel")
    public Response cancel(@RequestParam("openid")String openid,@RequestParam("orderId")String orderId){
        OrderDto orderDto = orderSerivce.findOne(orderId);
        if (orderDto == null){
            logger.error("【取消订单】查不到该订单, orderId={}", orderId);
            return Response.error(ResultEnum.ORDER_NOT_EXIST.getMessage());
        }
        if (!orderDto.getBuyerOpenid().equals(openid)){
            logger.error("【查询订单】订单的openid不一致. openid={}, orderDTO={}", openid, orderDto);
            return Response.error(ResultEnum.ORDER_OWNER_ERROR.getMessage());
        }
        orderSerivce.cancel(orderDto);
        return Response.success();
    }
}
