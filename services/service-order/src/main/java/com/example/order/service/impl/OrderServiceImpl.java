package com.example.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.order.bean.Order;
import com.example.order.feign.ProductFeignClient;
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
    @Resource
    private ProductFeignClient productFeignClient;

    @Override
    @SentinelResource(value = "createOrder",blockHandler = "createOrderFallback")
    //在@SentinelResource中有blockHandler则违反规则的时候调用blockHandler方法，
    // 有fallback则违反规则的时候调用fallback方法，两个都没有时候，会调用defaultFallback方法，抛出最原始的异常
    public Order createOrder(Long userId, Long productId) {
        //Product product = getProductFromRemote(productId);
        //Product product = getProductFromRemoteWithBa lance(productId);
        Product product = productFeignClient.getProductById(productId);
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

    /**
     * 兜底回调--当违反sentinel（SentinelResource）规则的时候，会调用此方法
     * 此方法名和 @SentinelResource(value = "createOrder",blockHandler = "createOrderFallback")中的blockHandler的方法名一致
     */
    public Order createOrderFallback(Long userId, Long productId, BlockException e){
        Order order = new Order();
        order.setUserId(userId);
        order.setProductList(Arrays.asList(new Product()));
        order.setTotalAmount(new BigDecimal(0));
        order.setAddress("上海");
        order.setNickName("张三");
        order.setId(0L);
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
        ServiceInstance serviceInstance = loadBalancerClient.choose(" -product");
        //远程url地址
        String url = "http://"+serviceInstance.getHost() + ":" + serviceInstance.getPort()+"/product/"+productId;
        log.info("发送请求url:{}",url);
        return restTemplate.getForObject(url, Product.class);
    }
}
