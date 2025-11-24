package com.example.order.service;

import com.example.order.bean.Order;

public interface OrderService {
    /**
     * 创建订单
     * @param userId 用户id
     * @param productId 商品id
     * @return 订单
     */
    Order createOrder(Long userId, Long productId);
}
