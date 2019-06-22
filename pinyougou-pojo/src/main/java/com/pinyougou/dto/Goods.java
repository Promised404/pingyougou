package com.pinyougou.dto;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 商品组合表.
 * @author 邓鹏涛
 * @date 2019/2/15 21:06
 */
@Getter
@Setter
@ToString
public class Goods implements Serializable {

    private TbGoods goods;

    private TbGoodsDesc goodsDesc;

    private List<TbItem> itemList;

}
