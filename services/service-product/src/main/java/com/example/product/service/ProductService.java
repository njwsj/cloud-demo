package com.example.product.service;

import com.example.product.bean.Product;

public interface ProductService {

    /**
     * 根据id查询商品信息
     * @param id 商品id
     * @return 商品信息
     */
    public Product getProductById(Long id);
}
