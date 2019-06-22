package com.pinyougou.task;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 邓鹏涛
 * @date 2019/3/9 13:20
 */
@Component
public class SeckillTask {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void refreshSeckillGoods() {
        System.out.println("执行了秒杀商品增量调度更新任务调度" + new Date());
        List goodIdList = new ArrayList(redisTemplate.boundHashOps("seckillGoods").keys());
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1"); //审核通过的商品
        criteria.andStockCountGreaterThanOrEqualTo(0); //库存数大于0
        criteria.andStartTimeLessThanOrEqualTo(new Date());
        criteria.andEndTimeGreaterThanOrEqualTo(new Date());
        if (goodIdList.size() > 0) {
            criteria.andIdNotIn(goodIdList);
        }

        List<TbSeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
        for (TbSeckillGoods goods : seckillGoods) {
            redisTemplate.boundHashOps("seckillGoods").put(goods.getId(), goods);
            System.out.println("增量更新秒杀商品id:" + goods.getId());
        }

    }


    @Scheduled(cron = "* * * * * ?")
    public void removeSeckillGoods() {
        List<TbSeckillGoods> seckillGoodList = redisTemplate.boundHashOps("seckillGoods").values();
        System.out.println("执行清除秒杀商品的任务");
        for (TbSeckillGoods seckillGood : seckillGoodList) {
            System.out.println(seckillGood.getId() + "的结束时间：" + seckillGood.getEndTime().getTime() + "和现在时间：" + new Date().getTime());
            System.out.println("差值：" + (new Date().getTime() - seckillGood.getEndTime().getTime()));
            if (seckillGood.getEndTime().getTime() < new Date().getTime()) {
                //同步到数据库
                seckillGoodsMapper.updateByPrimaryKey(seckillGood);
                // 清除缓存
                System.out.println("秒杀商品" + seckillGood.getId() + "已过期");
                redisTemplate.boundHashOps("seckillGoods").delete(seckillGood.getId());
            }
        }
        System.out.println("执行秒杀商品任务...end");
    }
}
