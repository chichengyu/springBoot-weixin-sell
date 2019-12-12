package cn.xiaochi.service;

import cn.xiaochi.dataobject.SellerInfo;

public interface SellerSerice {

    SellerInfo findSellerByOpenid(String openid);
}
