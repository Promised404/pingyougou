package com.pinyougou.seckill.service;
import java.util.List;

import com.pinyougou.pojo.TbSeckillGoods;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SeckillGoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	List<TbSeckillGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	void add(TbSeckillGoods seckillGoods);
	
	
	/**
	 * 修改
	 */
	void update(TbSeckillGoods seckillGoods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	TbSeckillGoods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageResult findPage(TbSeckillGoods seckillGoods, int pageNum, int pageSize);

	/**
	 * 查询所有秒杀商品
	 * @return
	 */
	List<TbSeckillGoods> findList();

	/**
	 * 从缓存中通过id查询一个商品
	 * @param id
	 * @return
	 */
	TbSeckillGoods findOneFromRedis(Long id);


}
