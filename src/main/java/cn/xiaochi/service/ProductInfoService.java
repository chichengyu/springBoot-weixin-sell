package cn.xiaochi.service;

import cn.xiaochi.dataobject.ProductInfo;
import cn.xiaochi.dto.CartDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ProductInfoService {

    ProductInfo findOne(String productId);

    /**
     * 查询分页
     * @param pageable
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    List<ProductInfo> findUpAll();

    ProductInfo save(ProductInfo product);

    // 加库存
    void increaseStock(List<CartDto> cartDtos);

    // 减库存
    void decreaseStock(List<CartDto> cartDtos);
}
