package cn.xiaochi.dao;

import cn.xiaochi.dataobject.ProductInfo;
import cn.xiaochi.dto.CartDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {

    /**
     * 根据上下架状态查询商品
     * @param productStatus
     * @return
     */
    List<ProductInfo> findByProductStatus(Integer productStatus);

    List<ProductInfo> findByProductIdIn(List<String> productIds);

}
