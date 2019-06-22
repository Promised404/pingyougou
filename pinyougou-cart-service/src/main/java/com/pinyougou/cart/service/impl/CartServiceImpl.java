package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.enums.GoodsEnums;
import com.pinyougou.dto.Cart;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 邓鹏涛
 * @date 2019/3/3 13:30
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {

        // 1.根据sku的id查询商品明细SKU的对象
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!GoodsEnums.NORMAL_STATUS.getStatus().equals(item.getStatus())) {
            throw new RuntimeException("商品状态不合法");
        }
        // 2.根据sku对象拿到商家信息
        String sellerId = item.getSellerId();
        String seller = item.getSeller();
        // 3.判断原有购物车中是否存在此商家
        Cart cart = searchCartBySellerId(cartList, sellerId);
        if (cart == null) {// 4.如果不存在此商家
            //  4.1创建一个新的购物车对象
            Cart cartTemp = new Cart();
            cartTemp.setSellerId(sellerId);
            cartTemp.setSellerName(seller);
            List<TbOrderItem> orderItems = new ArrayList<TbOrderItem>();// 构建购物车明细
            TbOrderItem orderItem = createOrderItem(item, num);
            orderItems.add(orderItem);
            cartTemp.setOrderItemList(orderItems);
            //  4.2将此购物车对象加入到购物车列表中
            cartList.add(cartTemp);
        } else {// 5.如果存在此商家
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            //  5.1存在此商家后在此购物车的明细列表中是否已经存在此商品
            TbOrderItem orderItem = searchOrderItemByItemId(orderItemList, itemId);
            if (orderItem != null) {
                //  5.2如果存在此商品，则添加数量num
                orderItem.setNum(orderItem.getNum() + num); //更新数量
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));
                // 当明细的数量小于等于0，即移除此明细
                if (orderItem.getNum() <= 0) {
                    cart.getOrderItemList().remove(orderItem);
                }
                //当购物车的明细列表数量为0，在购物车列表中移除此购物车
                if (cart.getOrderItemList().size() == 0) {
                    cartList.remove(cart);
                }
            } else {
                //  5.3如果不存在则，向明细列表中新增一项
                orderItem = createOrderItem(item, num);
                orderItemList.add(orderItem);
            }
        }
        return cartList;
    }

    @Override
    public List<Cart> findCartListFromRedis(String username) {
        System.out.println("从redis中提取购物车：" + username);
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        System.out.println("保存购物车进入redis中， username:" + username + "保存数据：" + cartList);
        redisTemplate.boundHashOps("cartList").put(username,cartList);
    }

    @Override
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
        // 不能直接加，会有重复记录
        for (Cart cart : cartList2) {
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                cartList1 = addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return cartList1;
    }

    /**
     * 根据商家id查询购物车
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (sellerId.equals(cart.getSellerId())) {
                return cart;
            }
        }
        return null;
    }

    /**
     * 根据skuId在购物车明细列表中查询购物车明细对象
     * @param orderItemList
     * @param itemId
     * @return
     */
    private TbOrderItem searchOrderItemByItemId (List<TbOrderItem> orderItemList,Long itemId) {
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == itemId.longValue()) {
                return orderItem;
            }
        }
        return null;
    }

    private TbOrderItem createOrderItem(TbItem item, Integer num) {
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
        return orderItem;
    }

}
