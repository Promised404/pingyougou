package com.pinyougou.pay.service;

import java.util.Map;

/**
 * 微信支付.
 * @author 邓鹏涛
 * @date 2019/3/5 12:27
 */
public interface WeixinPayService {

    /**
     * 生成二维码需要的url
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    Map createNative(String out_trade_no, String total_fee);

    /**
     * 查询订单
     * @param out_trade_no
     * @return
     */
    Map queryPayStatus(String out_trade_no);

    /**
     * 关闭订单
     * @param out_trade_no
     * @return
     */
    Map closePay(String out_trade_no);
}
