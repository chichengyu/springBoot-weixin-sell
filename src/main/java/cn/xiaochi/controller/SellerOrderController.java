package cn.xiaochi.controller;

import cn.xiaochi.dto.OrderDto;
import cn.xiaochi.enums.OrderStatusEnum;
import cn.xiaochi.enums.ResultEnum;
import cn.xiaochi.exception.SellException;
import cn.xiaochi.service.OrderSerivce;
import cn.xiaochi.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/seller/order")
public class SellerOrderController {

    @Autowired
    private OrderSerivce orderSerivce;
    @Autowired
    private Environment environment;

    /**
     * 订单列表
     * @param page
     * @param size
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                       Model model){
        Sort sort = new Sort(Sort.Direction.DESC, "orderId");
        PageRequest pageRequest = new PageRequest(page - 1, size, sort);
        Page<OrderDto> orderSerivceAdminList = orderSerivce.findAdminList(pageRequest);
        model.addAttribute("orderList",orderSerivceAdminList);
        return "order/list";
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @GetMapping("/cancel")
    public String cancel(@RequestParam("orderId")String orderId,Model model){
        try{
            OrderDto orderDto = orderSerivce.findOne(orderId);
            if (orderDto != null){
                orderSerivce.cancel(orderDto);
            }
        }catch (SellException e){
            log.error("【卖家端取消订单】发生异常{}", e);
            model.addAttribute("msg",ResultEnum.ORDER_NOT_EXIST.getMessage());
            model.addAttribute("url","/sell/seller/order/list");
            return "common/error";
        }
        return "redirect:list";
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public String detail(@RequestParam("orderId")String orderId,Model model){
        OrderDto orderDto = new OrderDto();
        try{
            orderDto = orderSerivce.findOne(orderId);
        }catch (SellException e){
            log.error("【卖家端订单详情】发生异常{}", e);
            model.addAttribute("msg",ResultEnum.ORDER_NOT_EXIST.getMessage());
            model.addAttribute("url","/sell/seller/order/list");
            return "common/error";
        }
        model.addAttribute("order",orderDto);
        return "detail";
    }

    /**
     * 完结订单
     * @param orderId
     * @return
     */
    @GetMapping("/finish")
    public String finish(@RequestParam("orderId")String orderId,Model model){
        try{
            OrderDto orderDto = orderSerivce.findOne(orderId);
            orderSerivce.finish(orderDto);
        }catch (SellException e){
            log.error("【卖家端完结订单】发生异常{}", e);
            model.addAttribute("msg",ResultEnum.ORDER_NOT_EXIST.getMessage());
            model.addAttribute("url","/sell/seller/order/list");
            return "common/error";
        }
        return "common/success";
    }
}
