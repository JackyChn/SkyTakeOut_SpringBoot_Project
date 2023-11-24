package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * Add new dish with flavor(list)
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
//        insert one dish at a time
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        Long dishId = dish.getId();

//        insert flavor(s) (list)
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0) {
//        then assign dishId to dishFlavorId
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
//            insert flavor list
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * Dish Page Query
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageDishQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageDishQuery(dishPageQueryDTO);
        long total = page.getTotal();
        List<DishVO> result = page.getResult();
        return new PageResult(total, result);
    }

    /**
     * Delete Dish(es) by ids
     * @param ids
     */
    @Transactional
    public void deleteDish(List<Long> ids) {
//        See if the current dish can be deleted: if_exist
        for (Long id: ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE) { // dish is for sale, cannot be deleted
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

//        See if the current dish are being related to other table(flavor, setmeal)
        List<Long> setMealIdsByDishIds = setMealDishMapper.getSetMealIdsByDishIds(ids);
        if(setMealIdsByDishIds != null && setMealIdsByDishIds.size() > 0) { // yes, setmeal_dish has related relationships, also cannot bge deleted
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

//        logic goes here means the current dish is available to delete
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
////        and delete its related flavor
//            dishFlavorMapper.deleteByDishId(id);
//        }

//        Delete dishBatch based on dishes
        dishMapper.deleteByIds(ids);
//        Delete dishBatch flavors
        dishFlavorMapper.deleteByDishIds(ids);


    }

    /**
     * Get dish by dish id
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
//        Get dish by its id
        Dish dish = dishMapper.getById(id);

//        Get dishFlavor by dish id
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishIds(id);

//        Encapsulate dish and flavor as a dishVO object and return
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * Update dish
     * @param dishDTO
     */

    public void updateDishWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
//        update dish set ... where dish_id = ?
        dishMapper.updateDish(dish);

//        delete the original flavors and dishes regarding
        dishMapper.deleteById(dishDTO.getId());

//        then add the new flavors passed from dishDTO
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 1){
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishDTO.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }


    }


}
