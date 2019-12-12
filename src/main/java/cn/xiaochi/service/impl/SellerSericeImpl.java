package cn.xiaochi.service.impl;

import cn.xiaochi.dao.SellerInfoRepository;
import cn.xiaochi.dataobject.SellerInfo;
import cn.xiaochi.service.SellerSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerSericeImpl implements SellerSerice {

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Override
    public SellerInfo findSellerByOpenid(String openid) {
        return sellerInfoRepository.findByOpenid(openid);
    }
}
