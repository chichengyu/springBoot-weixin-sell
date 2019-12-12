package cn.xiaochi.controller;

import cn.xiaochi.dataobject.ProductInfo;
import cn.xiaochi.enums.ResultEnum;
import cn.xiaochi.exception.SellException;
import cn.xiaochi.form.ProductForm;
import cn.xiaochi.service.ProductInfoService;
import cn.xiaochi.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    /**
     * 商品列表
     * @param page
     * @param size
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                       Model model){
        Sort sort = new Sort(Sort.Direction.DESC,"productId");
        Pageable pageRequest = new PageRequest(page - 1, size, sort);
        Page<ProductInfo> productInfos = productInfoService.findAll(pageRequest);
        model.addAttribute("productList",productInfos);
        return "product/list";
    }

    /**
     * 添加Y与修改商品
     * @param productForm
     * @return
     */
    public String save(@Valid ProductForm productForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new SellException(ResultEnum.PARAM_ERROR.getMessage(),ResultEnum.PARAM_ERROR.getCode());
        }
        ProductInfo productInfo = null;
        if(!StringUtils.isEmpty(productForm.getProductId())){
            productInfo = productInfoService.findOne(productForm.getProductId());
        }else {
            productForm.setProductId(KeyUtil.genUniqueKey());
        }
        BeanUtils.copyProperties(productForm,productInfo);
        ProductInfo save = productInfoService.save(productInfo);
        return "redirect:list";
    }

    /**
     * 商品详情
     * @param productId
     * @return
     */
    @GetMapping("/detail")
    public String detail(@RequestParam("productId")String productId,Model model){
        ProductInfo productInfo = new ProductInfo();
        try{
            productInfo = productInfoService.findOne(productId);
        }catch (SellException e){
            log.error("【卖家端订单详情】发生异常{}", e);
            model.addAttribute("msg",ResultEnum.ORDER_NOT_EXIST.getMessage());
            model.addAttribute("url","/sell/seller/order/list");
            return "common/error";
        }
        model.addAttribute("product",productInfo);
        return "detail";
    }
}
