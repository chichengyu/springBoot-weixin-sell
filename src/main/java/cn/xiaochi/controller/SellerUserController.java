package cn.xiaochi.controller;

import cn.xiaochi.dataobject.SellerInfo;
import cn.xiaochi.service.SellerSerice;
import cn.xiaochi.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("/seller/user")
public class SellerUserController {

    @Autowired
    private SellerSerice sellerSerice;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Environment environment;

    public static final String TOKEN_KEY = "token_%s";

    /**
     * 登录
     * @param openid
     * @param response
     * @param model
     * @return
     */
    @GetMapping("/login")
    public String login(@RequestParam("openid")String openid, HttpServletResponse response, Model model){
        SellerInfo sellerByOpenid = sellerSerice.findSellerByOpenid(openid);
        if (sellerByOpenid == null){
            model.addAttribute("msg","登录失败");
            return "common/error";
        }
        String token = UUID.randomUUID().toString();
        // 存 redis
        stringRedisTemplate.opsForValue().set(String.format(TOKEN_KEY,token),openid);
        //3. 设置token至cookie
        CookieUtil.set(response,TOKEN_KEY, token,700);
//        return "redirect:/seller/order/list";
        return "redirect:" + environment.getProperty("projectUrl.sell") + "/sell/seller/order/list";
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @GetMapping("logput")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        // 从 cookie 查询
        Cookie cookie = CookieUtil.get(request, TOKEN_KEY);
        if (cookie != null){
            // 清除 redis
            stringRedisTemplate.opsForValue().getOperations().delete(String.format(TOKEN_KEY,cookie.getValue()));
            // 清除 cookie
            CookieUtil.set(response,TOKEN_KEY,null,-1);
        }
        return "redirect:success";
    }
}
