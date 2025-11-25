package com.example.order.feign.fallback;

import com.example.order.feign.ProductFeignClient;
import com.example.product.bean.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductFeignClientFallBack implements ProductFeignClient {
    @Override
    public Product getProductById(Long id) {
        System.out.println("兜底数据");
        Product product = new Product();
        product.setProductName("商品不存在");
        product.setNum(0);
        product.setPrice(BigDecimal.ZERO);
        product.setId(id);
        return product;
    }
}
