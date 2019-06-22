package com.pinyougou.manager.controller;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.enums.GoodsEnums;
import com.pinyougou.dto.Goods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	/*@Reference(timeout = 100000)
	private ItemSearchService itemSearchService;*/
	/*@Reference(timeout = 40000)
	private ItemPageService itemPageService;*/
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Destination queueSolrDestination;
	@Autowired
	private Destination queueSolrDeleteDestination; // 用于导入solr索引库的消息目标（点对点）
	@Autowired
	private Destination topicPageDestination; // 用于生成商品详情页的消息目标（发布订阅）
	@Autowired
	private Destination topicPageDeleteDestination;// 用于删除商品详情页的消息目标（发布订阅）

	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}

	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@Transactional
	public Result delete(final Long [] ids){
		try {
			goodsService.delete(ids);
			//从索引库中删除
			//itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});

			jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});
			// 删除每个服务器上的详情页面

			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}

	/**
	 * 更新商品状态.
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateStatus")
	@Transactional
	public Result updateStatus(@RequestBody Long[] ids, String status) {
		System.out.println(status);
		try {

		    goodsService.updateStatus(ids, status);
			if (GoodsEnums.NORMAL_STATUS.getStatus().equals(status)) { // 审核通过
				// 得到需要导入的SKU列表
                // solr 索引库更新
				List<TbItem> itemList = goodsService.findItemListByGoodsIdListAndStatus(ids, status);

				final String jsonString = JSON.toJSONString(itemList);//转换成json,方便jms发送

				if (CollectionUtils.isNotEmpty(itemList)) {
					//itemSearchService.importList(itemList);
					jmsTemplate.send(queueSolrDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(jsonString);
						}
					});
				}

				// 商品详情页生成
                for (final Long goodsId : ids) {
                    //itemPageService.genItemHtml(goodsId);
					jmsTemplate.send(topicPageDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(goodsId + "");
						}
					});
				}

			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			return new Result(false, "处理失败");
		}
		return new Result(true, "处理成功");
	}

	@RequestMapping("/genHtml")
	public void genHtml(Long goodsId) {
		//itemPageService.genItemHtml(goodsId);
	}

}
