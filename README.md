##启动sentinel  
java -Dserver.port=8080 -jar sentinel-dashboard-1.8.9.jar  

##熔断机制  
最大RT--最大request time  
比例阈值--触发熔断的错误百分比  
熔断时长--熔断时长  
最小请求数--熔断的最小请求数  
统计时长--统计时长  
熔断规则-- 在统计时长内 有比例阈值的响应时间超过最大rt 则触发熔断时长的时间的熔断  
例如 在5s的时间中 有80%的响应时间超过1s 则触发熔断  
但是在这5s内 请求数需要满足大于等于最小请求数  

##Sentinel 的热点限流  
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