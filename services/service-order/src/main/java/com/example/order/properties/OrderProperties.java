package com.example.order.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "order")//通过该注解 也能引入nacos的配置文件，并且自动刷新
@Component
public class OrderProperties {
    private String timeout;
    private String autoConfirm;
    private String dbUrl;
}