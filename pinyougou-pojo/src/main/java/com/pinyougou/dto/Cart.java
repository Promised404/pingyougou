package com.pinyougou.dto;

import com.pinyougou.pojo.TbOrderItem;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车对象
 * @author 邓鹏涛
 * @date 2019/3/3 12:23
 */
@Getter
@Setter
public class Cart implements Serializable {

    private String sellerId; //商家ID
    private String sellerName; // 商家名称
    private List<TbOrderItem> orderItemList; //购物车明细列表

}
