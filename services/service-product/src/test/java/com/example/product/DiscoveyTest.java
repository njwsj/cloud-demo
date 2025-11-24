package com.example.product;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@SpringBootTest
public class DiscoveyTest {
    @Resource
    private DiscoveryClient discoveryClient;

    @Test
    void discoveryClientTest(){
        for (String service : discoveryClient.getServices()) {
            System.out.println("service = "+service);
            //获取service的实例
            for (ServiceInstance instance : discoveryClient.getInstances(service)) {
                System.out.println(instance.getHost()+"---"+instance.getPort());
            }
        }
    }
}
