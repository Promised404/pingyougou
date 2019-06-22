package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * solr搜索接口.
 * @author 邓鹏涛
 * @date 2019/2/20 22:15
 */
public interface ItemSearchService {

    /**
     * 搜索方法
     * @param searchMap
     * @return
     */
    Map search(Map searchMap);

    /**
     * 导入列表
     * @param list
     */
    void importList(List list);

    /**
     * 删除商品列表
     * @param goodsIds
     */
    void deleteByGoodsIds(List goodsIds);
}
