package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "Shop Interface")
@Slf4j
public class ShopController {

    public static final String Key = "shop_status";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("Set Shop Status")
    public Result<Void> setStatus(@PathVariable Integer status) {
        log.info("Setting status as: {}", status == 1 ? "Open" : "Closed");
        redisTemplate.opsForValue().set(Key, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("Get Shop Status")
    public Result<Integer> getStatus() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(Key);
        log.info("Shop is: {}", shopStatus == 1 ? "Open" : "Closed");
        return Result.success();
    }
}
