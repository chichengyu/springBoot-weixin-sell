package cn.xiaochi.service.impl;

import cn.xiaochi.dao.ProductInfoRepository;
import cn.xiaochi.dataobject.ProductInfo;
import cn.xiaochi.dto.CartDto;
import cn.xiaochi.enums.ProductStatusEnum;
import cn.xiaochi.exception.SellException;
import cn.xiaochi.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository productRepository;

    @Override
    public ProductInfo findOne(String productId) {
        return productRepository.findOne(productId);
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public ProductInfo save(ProductInfo product) {
        return productRepository.save(product);
    }

    // 加库存
    @Override
    @Transactional
    public void increaseStock(List<CartDto> cartDtos) {
        for (CartDto cartDto : cartDtos){
            ProductInfo productInfo = productRepository.findOne(cartDto.getProductId());
            if (productInfo == null){
                throw new SellException(ProductStatusEnum.PRODUCT_NOT_EXIST.getMessage(),ProductStatusEnum.PRODUCT_NOT_EXIST.getCode());
            }
            Integer result = cartDto.getProductQuantity() + productInfo.getProductStock();
            productInfo.setProductStock(result);
            productRepository.save(productInfo);
        }
    }

    // 减库存
    @Override
    @Transactional
    public void decreaseStock(List<CartDto> cartDtos) {
        for (CartDto cartDto : cartDtos){
            ProductInfo productInfo = productRepository.findOne(cartDto.getProductId());
            if (productInfo == null){
                throw new SellException(ProductStatusEnum.PRODUCT_NOT_EXIST.getMessage(),ProductStatusEnum.PRODUCT_NOT_EXIST.getCode());
            }
            // 减库存  todo: 超卖问题并发
            int result = productInfo.getProductStock() - cartDto.getProductQuantity();
            if (result < 0){
                throw new SellException(ProductStatusEnum.PRODUCT_STOCK_ERROR.getMessage(),ProductStatusEnum.PRODUCT_STOCK_ERROR.getCode());
            }
            productInfo.setProductStock(result);
            productRepository.save(productInfo);
        }
    }
}
