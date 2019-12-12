package cn.xiaochi.controller;

import cn.xiaochi.dataobject.ProductCategory;
import cn.xiaochi.dataobject.ProductInfo;
import cn.xiaochi.service.ProductCategoryService;
import cn.xiaochi.service.ProductInfoService;
import cn.xiaochi.util.Response;
import cn.xiaochi.vo.ProductInfoVo;
import cn.xiaochi.vo.ProductCategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductInfoService productInfoService;

    @GetMapping("/list")
    public Response list(){
        List<ProductInfo> productInfos = productInfoService.findUpAll();
        List<Integer> categoryTypeList = productInfos.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());

        List<ProductCategory> byCategoryTypeList = productCategoryService.findByCategoryTypeIn(categoryTypeList);
        List<ProductCategoryVo> productVoList = new ArrayList<>();
        for (ProductCategory productCategory : byCategoryTypeList){
            ProductCategoryVo productVo = new ProductCategoryVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());
            List<ProductInfoVo> foods = new ArrayList<>();
            productInfos.forEach(productInfo -> {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo,productInfoVo);
                    foods.add(productInfoVo);
                }
            });
            productVo.setFoods(foods);
            productVoList.add(productVo);
        }
        return Response.success(productVoList);
    }
}
