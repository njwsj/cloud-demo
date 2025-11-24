package com.example.product.service.impl;

import com.example.product.bean.Product;
import com.example.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProductById(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setProductName("productName");
        product.setNum(100);
        product.setPrice(new BigDecimal(100));
        return product;
    }
}
