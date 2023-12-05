package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "Dish interface")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add new dish")
    public Result<Void> save(@RequestBody DishDTO dishDTO) {
        log.info("Add new dish: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

//        clean the local redis cache
        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("Dish Query Page")
    public Result<PageResult> queryDishPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("Dish Query Page: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageDishQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("Delete Dish")
    public Result<Void> deleteDish(@RequestParam List<Long> ids) {
        log.info("Delete Dish(es) ids: {}", ids);
        dishService.deleteDish(ids);

//      clean all local cache in redis where key has prefix "dish_"
        cleanCache("dish_*");

        return  Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get Dish IDs")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("Get Dish by Id: {}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("Update Dish Details")
    public Result<Void> updateDish(@RequestBody DishDTO dishDTO) {
        log.info("Update dish: {}", dishDTO);
        dishService.updateDishWithFlavor(dishDTO);
//      clean all local cache in redis where key has prefix "dish_"
        cleanCache("dish_*");

        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Set dish status")
    public Result<String> startOrStop(Integer status, Long id) {
        dishService.startOrStop(status, id);
//      clean all local cache in redis where key has prefix "dish_"
        cleanCache("dish_*");

        return Result.success();
    }

        /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Select dish by category id")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }


}
