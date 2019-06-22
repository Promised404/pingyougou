package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听消息，生成商品详情页面
 * @author 邓鹏涛
 * @date 2019/2/28 21:40
 */
@Component("pageListener")
public class PageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage)message;
        try {
            String goodsId = textMessage.getText();
            System.out.println("接收到的消息：" + goodsId);
            boolean b = itemPageService.genItemHtml(Long.parseLong(goodsId));
            System.out.println("页面生成结果：" + b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
