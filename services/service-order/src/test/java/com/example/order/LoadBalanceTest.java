package com.example.order;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

@SpringBootTest
public class LoadBalanceTest {
    @Resource
    private LoadBalancerClient loadBalancerClient;
    @Test
    public void testLoadBalance(){
        // 随机获取服务实例--负载均衡
        //ServiceInstance choose = loadBalancerClient.choose("service-product");
        //System.out.println(choose.getHost() + ":" + choose.getPort());
        for (int i = 0; i < 10; i++) {
            ServiceInstance choose = loadBalancerClient.choose("service-product");
            System.out.println(choose.getHost() + ":" + choose.getPort());
        }
    }
}
