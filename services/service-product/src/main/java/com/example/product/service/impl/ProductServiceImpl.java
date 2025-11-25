package com.example.product.service.impl;

import com.example.product.bean.Product;
import com.example.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProductById(Long id) {
        // 检查id是否为空或无效
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product id: " + id);
        }
        
        Product product = new Product();
        product.setId(id);
        product.setProductName("Product Name " + id);
        product.setNum(100);
        product.setPrice(new BigDecimal("99.99"));
        return product;
    }
}