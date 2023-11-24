package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "Shop Interface")
@Slf4j
public class ShopController {

    public static final String Key = "shop_status";

    @Autowired
    private RedisTemplate redisTemplate;

//    not set method, user can only check shop status

    @GetMapping("/status")
    @ApiOperation("Get Shop Status")
    public Result<Integer> getStatus() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(Key);
        log.info("Shop is: {}", shopStatus == 1 ? "Open" : "Closed");
        return Result.success();
    }
}
