package cn.xiaochi.service;

import cn.xiaochi.dto.OrderDto;

/**
 * 推送消息
 */
public interface PushMessageService {

    /**
     * 订单状态变更消息
     * @param orderDto
     */
    void orderStatus(OrderDto orderDto);
}
