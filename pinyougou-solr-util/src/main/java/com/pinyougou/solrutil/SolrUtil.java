package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.enums.GoodsEnums;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author 邓鹏涛
 * @date 2019/2/20 21:00
 */
@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    public void importItemData() {

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(GoodsEnums.NORMAL_STATUS.getStatus()); // 审核通过才能导入
        List<TbItem> tbItems = itemMapper.selectByExample(example);

        System.out.println("--商品列表--");
        for (TbItem item : tbItems) {
            System.out.println(item.getTitle() + "  " + item.getBrand());
            Map specMap = JSON.parseObject(item.getSpec(), Map.class); // 从数据库中提取规格json字符串装换成map
            item.setSpecMap(specMap);
        }
        solrTemplate.saveBeans(tbItems);
        solrTemplate.commit();
        System.out.println("结束");

    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        solrUtil.importItemData();
    }

}
