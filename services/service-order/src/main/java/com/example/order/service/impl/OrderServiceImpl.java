package com.example.order.service.impl;

import com.example.order.bean.Order;
import com.example.order.service.OrderService;
import com.example.product.bean.Product;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private LoadBalancerClient loadBalancerClient;

    @Override
    public Order createOrder(Long userId, Long productId) {
        //Product product = getProductFromRemote(productId);
        Product product = getProductFromRemoteWithBalance(productId);
        Order order = new Order();
        order.setUserId(userId);
        //商品列表
        order.setProductList(Collections.singletonList(product));
        //设置订单总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setAddress("上海");
        order.setNickName("张三");
        order.setId(1L);
        return order;
    }

    private Product getProductFromRemote(Long productId) {
        //获取到商品服务的所有机器的ip+port
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance serviceInstance = instances.get(0);
        //远程url地址
        String url = "http://"+serviceInstance.getHost() + ":" + serviceInstance.getPort()+"/product/"+productId;
        log.info("发送请求url:{}",url);
        return restTemplate.getForObject(url, Product.class);
    }

    private Product getProductFromRemoteWithBalance(Long productId) {
        //获取到商品服务的所有机器的ip+port--负载均衡
        ServiceInstance serviceInstance = loadBalancerClient.choose("service-product");
        //远程url地址
        String url = "http://"+serviceInstance.getHost() + ":" + serviceInstance.getPort()+"/product/"+productId;
        log.info("发送请求url:{}",url);
        return restTemplate.getForObject(url, Product.class);
    }
}
