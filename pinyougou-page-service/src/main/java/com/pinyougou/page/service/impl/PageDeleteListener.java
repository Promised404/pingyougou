package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * @author 邓鹏涛
 * @date 2019/2/28 21:58
 */
@Component("pageDeleteListener")
public class PageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage)message;
        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("接收到消息：" + goodsIds.toString());
            boolean b = itemPageService.deleteItemHtml(goodsIds);
            System.out.println("删除结果：" + b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
