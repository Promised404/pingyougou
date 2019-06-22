package com.pinyougou.cart.service;

import com.pinyougou.dto.Cart;

import java.util.List;

/**
 * 购物车服务接口.
 *
 * @author 邓鹏涛
 * @date 2019/3/3 13:28
 */
public interface CartService {

    /**
     * 添加商品到购物车列表
     * @param list
     * @param itemId
     * @param num
     * @return
     */
    List<Cart> addGoodsToCartList(List<Cart> list, Long itemId, Integer num);

    /**
     * 根据登录用户到缓存redis中查询购物车列表
     * @param username
     * @return
     */
    List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车存入登录用户redis中
     * @param username
     * @param cartList
     */
    void saveCartListToRedis(String username, List<Cart> cartList);

    /**
     * 合并购物车
     * @param cartList1
     * @param cartList2
     * @return
     */
    List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2);

}
