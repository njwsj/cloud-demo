package com.example.order.controller;

import com.example.order.bean.Order;
import com.example.order.properties.OrderProperties;
import com.example.order.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@RefreshScope // 动态刷新 当nacos的配置文件修改了之后 自动会刷新
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;
//    @Value("${order.timeout}")
//    private String orderTimeout;
//    @Value("${order.auto-confirm}")
//    private String orderAutoConfirm;
    @Resource
    private OrderProperties orderProperties;

    @GetMapping("/create")
    public Order createOrder(@RequestParam("user_id") Long userId,
                             @RequestParam("product_id") Long productId){
        return orderService.createOrder(userId, productId);
    }

    /**
     * 获取订单配置信息
     * @return 包含订单超时时间和自动确认配置的字符串
     */
    @GetMapping("/config")
    public String getConfig(){
        Map<String, String> configMap = new HashMap<>();
        configMap.put("order.timeout", orderProperties.getTimeout());
        configMap.put("order.auto-confirm", orderProperties.getAutoConfirm());
        configMap.put("order.db-url", orderProperties.getDbUrl());
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(configMap);
        } catch (Exception e) {
            return "Error formatting config: " + e.getMessage();
        }
    }
}
