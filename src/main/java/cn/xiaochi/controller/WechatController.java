package cn.xiaochi.controller;


import cn.xiaochi.enums.ResultEnum;
import cn.xiaochi.exception.SellException;
import cn.xiaochi.util.Response;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//@RestController // 不能跳转
@Controller
@RequestMapping("/auth")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxMpService wxMpOpenService;
    @Autowired // Environment 读取配置
    private Environment environment;

    // =============================微信支付===========================
    /**
     * 微信网页授权
     * @param returnUrl
     * @return
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){
        // String url = "http://www.baidu.com/sell/wx/auth/userInfo";// 获取 code 的重定向地址
        String url = environment.getProperty("projectUrl.wechatMpAuthorize");
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        String redirectUrl = null;
        try {
            redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("【微信网页授权】获取code,result{}", returnUrl);
            // e.printStackTrace();
        }
        return "redirect:" + redirectUrl;
    }

    /**
     * 获取用户信息
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,@RequestParam("state") String state){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            // e.printStackTrace();
            log.error("【微信网页授权】{}", e);
            throw new SellException("获取微信授权code失败"+ new Gson().toJson(e.getError()));
        }
        return "redirect:" + state + "?openid=" + wxMpOAuth2AccessToken.getOpenId();
    }

    // =============================微信扫码登录===========================

    @GetMapping("/qrAuthorize")
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl){
        // String url = "http://www.baidu.com/sell/wx/auth/qrUserInfo";// 获取 code 的重定向地址
        String url = environment.getProperty("projectUrl.wechatOpenAuthorize");
        String redirectUrl = wxMpService.buildQrConnectUrl(url,WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN,URLEncoder.encode(returnUrl));
        return "redirect:"+redirectUrl;
    }

    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code,@RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        try {
            wxMpOAuth2AccessToken = wxMpOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            // e.printStackTrace();
            log.error("【微信网页授权】{}", e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getMessage(), e.getError().getErrorCode());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl + "?openid=" + openId;
    }
}