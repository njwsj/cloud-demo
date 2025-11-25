package com.example.order.feign;

import com.example.order.feign.fallback.ProductFeignClientFallBack;
import com.example.product.bean.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-product",fallback = ProductFeignClientFallBack.class) //feign客户端:开启兜底回调
public interface ProductFeignClient {

    //mvc注解的两套使用逻辑
    //在controller上是接受get请求
    //在feign接口上是发送这样的请求
    @GetMapping("/product/{id}")
    Product getProductById(@PathVariable("id") Long id);
}
