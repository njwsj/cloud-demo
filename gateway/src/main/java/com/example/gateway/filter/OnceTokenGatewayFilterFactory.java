package com.example.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 自定义过滤器工厂
 * 会在响应标头中添加：
 * x-response-token：
 * a888cf98-e8cf-4e6f-baa3-28713808debb
 */

@Component
public class OnceTokenGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return new GatewayFilter() {

            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                //每次响应之前 添加一个一次性令牌 支持uuid、jwt等格式
                return chain.filter(exchange).then(
                        Mono.fromRunnable(() -> {
                            ServerHttpResponse response = exchange.getResponse();
                            HttpHeaders headers = response.getHeaders();
                            String value = config.getValue();
                            if ("uuid".equalsIgnoreCase( value)){
                                value = UUID.randomUUID().toString();
                            }
                            if ("jwt".equalsIgnoreCase(value)){
                                value = "jwt" ;
                            }
                            headers.add(config.getName(),value);
                        })
                );
            }
        };
    }
}
