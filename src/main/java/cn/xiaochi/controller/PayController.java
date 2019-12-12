package cn.xiaochi.controller;

import cn.xiaochi.dto.OrderDto;
import cn.xiaochi.enums.ResultEnum;
import cn.xiaochi.exception.SellException;
import cn.xiaochi.service.OrderSerivce;
import cn.xiaochi.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 支付
 */
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderSerivce orderSerivce;
    @Autowired
    private PayService payService;

    /**
     * 微信公众号(H5)支付
     * @param orderId
     * @param returnUrl
     * @param model
     * @return
     */
    @GetMapping("/create")
    public String create(@RequestParam("orderId")String orderId,
                         @RequestParam("returnUrl")String returnUrl,
                         Model model){
        OrderDto orderDto = orderSerivce.findOne(orderId);
        if (orderDto == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST.getMessage(),ResultEnum.ORDER_NOT_EXIST.getCode());
        }
        PayResponse payResponse = payService.create(orderDto);
        model.addAttribute("payResponse", payResponse);
        model.addAttribute("returnUrl", returnUrl);
        return "index";
    }

    /**
     * 支付回调
     * @return
     */
    @PostMapping("/notify")
    public String payNotify(@RequestBody String notifyData){
        /*  优雅的用页面返回给微信   notify_success.ftl 页面
        <xml>
            <return_code><![CDATA[SUCCESS]]></return_code>
            <return_msg><![CDATA[OK]]></return_msg>
        </xml>*/
         payService.notify(notifyData);
        return "pay/notify_success";
    }
}