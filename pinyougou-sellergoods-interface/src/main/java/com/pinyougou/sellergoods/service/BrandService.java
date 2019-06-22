package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口.
 *
 * @author 邓鹏涛
 * @date 2019/2/9 14:48
 */
public interface BrandService {

    /**
     * 查询所有品牌.
     * @return
     */
    List<TbBrand> findAll();

    /**
     *分页查询品牌.
     * @param pageNum 当前页
     * @param pageSize 每页显示数量
     * @return
     */
    PageResult findPage(int pageNum, int pageSize);

    /**
     * 新增品牌.
     * @param brand
     */
    void add(TbBrand brand);

    /**
     * 根据主键查找id.
     * @param id
     * @return
     */
    TbBrand findOne(Long id);

    /**
     * 更新品牌.
     * @param brand
     */
    void update(TbBrand brand);

    /**
     * 删除品牌.
     * @param id
     */
    void delete(long id);

    /**
     * 批量删除品牌.
     * @param ids
     */
    void deleteBatch(Long[] ids);

    /**
     * 分页查询条件品牌.
     * @param brand
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult findPage(TbBrand brand, int pageNum, int pageSize);

    /**
     * 前端下拉框需要数据
     * @return
     */
    List<Map> selectOptionList();
}
