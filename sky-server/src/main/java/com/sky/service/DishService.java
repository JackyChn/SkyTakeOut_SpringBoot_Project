package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * Add new dish with flavor (list)
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * Dish Page Query
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageDishQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * Delete Dish by ids
     * @param ids
     */
    void deleteDish(List<Long> ids);

    /**
     * Show Dish Details by dish id
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * Update Dish with Flavor
     * @param dishDTO
     */
    void updateDishWithFlavor(DishDTO dishDTO);

    /**
     * Set Dish Status
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);

    /**
     * Select Dish Flavor
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
