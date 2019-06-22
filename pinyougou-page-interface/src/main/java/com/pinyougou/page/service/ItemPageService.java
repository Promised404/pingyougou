package com.pinyougou.page.service;

/**
 * 商品详情页面服务接口.
 *
 * @author 邓鹏涛
 * @date 2019/2/26 20:16
 */
public interface ItemPageService {

    /**
     * 根据商品id生成商品详情页面.
     * @param goodsId
     * @return
     */
    boolean genItemHtml(Long goodsId);

    /**
     * 根据商品id删除商品详情页面.
     * @param goodsId
     * @return
     */
    boolean deleteItemHtml(Long[] goodsIds);

}
