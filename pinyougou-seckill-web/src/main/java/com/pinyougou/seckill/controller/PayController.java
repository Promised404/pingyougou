package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 邓鹏涛
 * @date 2019/3/5 13:37
 */
@RequestMapping("/pay")
@RestController
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/createNative")
    public Map createNative() {
        // 获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 提取支付日志
        IdWorker idWorker = new IdWorker();
        TbSeckillOrder seckillOrder = seckillOrderService.searchOrderFromRedisByUserId(username);
        // 3.调用微信支付接口
        if (seckillOrder != null) {
            return weixinPayService.createNative(seckillOrder.getId() + "", (long)(seckillOrder.getMoney().doubleValue() * 100) + "");
        } else {
            return new HashMap<>();
        }
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        // 获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Result result = null;
        int num = 0;
        while (true) {
            Map<String, String> map = weixinPayService.queryPayStatus(out_trade_no);
            if (map == null) {
                result = new Result(false, "支付发生错误");
                break;
            }
            if (map.get("trade_state").equals("SUCCESS")) {//支付成功
                result = new Result(true, "支付成功");
                seckillOrderService.saveOrderFromRedisToDb(username, Long.valueOf(map.get("out_trade_no")), map.get("transaction_id"));
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            num++;
            if (num > 100) {
                result = new Result(false, "二维码超时");
                // 关闭支付
                Map mapResult = weixinPayService.closePay(out_trade_no);
                if (mapResult != null && "FAIL".equals(mapResult.get("return_code"))) {
                    if ("ORDERPAID".equals(mapResult.get("error_code"))) {
                        result = new Result(true, "支付成功");
                        seckillOrderService.saveOrderFromRedisToDb(username, Long.valueOf(map.get("out_trade_no")), map.get("transaction_id"));
                    }
                }
                // 删除订单
                if (result.isSuccess() == false) {
                    seckillOrderService.deleteOrderFromRedis(username, Long.valueOf(out_trade_no));
                }
                break;
            }
        }
        return result;
    }

}
