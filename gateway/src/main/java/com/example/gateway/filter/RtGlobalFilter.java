package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway网关全局过滤器
 */


@Slf4j
@Component
public class RtGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().toString();
        long start = System.currentTimeMillis();
        //==========前置处理==============
        log.info("uri: {},开始时间:{}", uri, start);
        Mono<Void> filter = chain.filter(exchange)
                .doFinally((result) -> {
                    long end = System.currentTimeMillis();
                    log.info("uri: {},结束时间:{},耗时:{}ms", uri, end, end - start);
                });
        return filter;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
