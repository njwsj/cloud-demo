package com.example.gateway.predicate;

import io.netty.util.internal.StringUtil;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


/**
 * 自定义网关路由断言工厂
 * 这个类名VipRoutePredicateFactory前面的Vip就是断言名--这个必须和配置文件中的对应上
 * 例如在配置中的
 * 短写法 -Vip=user，zhj
 * 长写法：
 *       - name: Vip
 *         args:
 *           name: user
 *           value: zhj
 */
@Component
public class VipRoutePredicateFactory extends AbstractRoutePredicateFactory<VipRoutePredicateFactory.Config> {
    public VipRoutePredicateFactory() {
        super(Config.class);
    }

    /**
     * 重写该方法-指定参数的顺序
     * @return 列表
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("name","value");
    }

    /**
     * 重写该方法-判断逻辑
     * @param config 配置参数
     * @return 断言对象 
     */
    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                //要判断 localhost/search?q=hello&user=zhj 是vip用户--name是user value是zhj
                ServerHttpRequest request = serverWebExchange.getRequest();//获取到请求
                //获取到配置的参数的名称 如user
                String first = request.getQueryParams().getFirst(config.getName());
                //如果通过name 获取的参数值和配置的参数值相等，则返回true
                return StringUtils.hasText(first) && first.equals(config.getValue());
            }
        };
    }

    /**
     * 可以配置的参数
     */
    @Validated
    public static class Config {
        private @NotEmpty String name;
        private @NotEmpty String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Config() {
        }


    }
}
