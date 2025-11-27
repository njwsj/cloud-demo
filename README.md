## 启动sentinel  
java -Dserver.port=8080 -jar sentinel-dashboard-1.8.9.jar  
下载sentinel控制台
[链接](https://sentinelguard.io/zh-cn/docs/dashboard.html)

## 熔断机制
### 参数
- 最大RT--最大request time  
- 比例阈值--触发熔断的错误百分比
- 熔断时长  
- 最小请求数--熔断的最小请求数  
- 统计时长--统计时长  
- 熔断规则-- 在统计时长内 有比例阈值的响应时间超过最大rt 则触发熔断时长的时间的熔断

例如 在5s的时间中 有80%的响应时间超过1s 则触发熔断  
但是在这5s内 请求数需要满足大于等于最小请求数  

## Sentinel的热点限流  
对 userId 参数限流，但我想让 VIP 用户不限流。只能在高级选项中添加例外项吗？”  
推荐方案：使用 自定义 BlockException 处理 + 动态判断 VIP  
```
@RestController
public class OrderController {

    @Resource
    private UserService userService;

    @PostMapping("/seckill/order")
    @SentinelResource(
        value = "seckill-order",
        blockHandler = "handleBlock"
    )
    public Result seckillOrder(@RequestParam("userId") Long userId) {
        // 你的下单逻辑
        return Result.success("下单成功");
    }

    // 限流处理方法
    public Result handleBlock(Long userId, BlockException ex) {
        // 判断是否为 VIP 用户
        if (userService.isVipUser(userId)) {
            // VIP 不限流，直接放行
            return seckillOrder(userId); // 重新调用原方法
        }
        // 普通用户限流
        return Result.error("请求太频繁，请稍后再试");
    }
}
```

## gateway网关

### 配置网关
在配置文件application.yml中可以配置网关 

routes下是一个数组，里面可以配置多个路由
每个路由对象包含属性：
- **id：路由id**
- **uri：路由的uri**
- **predicates：断言**  
    断言支持长写法和短写法  
    也支持自定义断言：在route.yml中配置了Vip自定义断言，同时也需要配置自定义断言的配置类
- **filters：过滤器**  
    过滤器可以实现将/api/order/xxx 转换为 /xxx  
    过滤器同样可以自定义  像断言也需要配置过滤器的配置类

## seata

### seata服务下载

[链接](https://seata.apache.org/zh-cn/release-history/seata-server)  
seata开头的微服务都需要启动seata服务

### 启动seata服务 
./seata-server.sh -p 8091 -m file  
### seata可视化页面端口
7091

需要在每个项目中配置file.conf
