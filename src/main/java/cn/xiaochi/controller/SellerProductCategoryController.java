package cn.xiaochi.controller;


import cn.xiaochi.dataobject.ProductCategory;
import cn.xiaochi.enums.ResultEnum;
import cn.xiaochi.exception.SellException;
import cn.xiaochi.form.CategoryForm;
import cn.xiaochi.service.ProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/seller/category")
public class SellerProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 商品类目列表
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model){
        List<ProductCategory> productCategoryList = productCategoryService.findAll();
        model.addAttribute("categoryList",productCategoryList);
        return "category/list";
    }


    @GetMapping("/save")
    public String save(@Valid CategoryForm categoryForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new SellException(ResultEnum.PARAM_ERROR.getMessage(),ResultEnum.PARAM_ERROR.getCode());
        }
        ProductCategory productCategory = new ProductCategory();
        if (categoryForm.getCategoryId() != null){
            productCategory = productCategoryService.findOne(categoryForm.getCategoryId());
        }
        BeanUtils.copyProperties(categoryForm,productCategory);
        productCategoryService.save(productCategory);
        return "redirect:list";
    }
}
