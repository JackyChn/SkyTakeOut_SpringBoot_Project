package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * add new dish/set meal
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * select cart
     * @return
     */
    List<ShoppingCart> showShoppingCart();

    /**
     * clean cart
     */
    void cleanShoppingCart();

    /**
     * delete a dish from cart
     * @param shoppingCartDTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
