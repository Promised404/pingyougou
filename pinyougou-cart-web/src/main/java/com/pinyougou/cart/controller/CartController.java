package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.dto.Cart;
import entity.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 邓鹏涛
 * @date 2019/3/3 16:01
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Reference(timeout = 6000)
    private CartService cartService;

    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins = "http://localhost:9105", allowCredentials = "true") //springmvc 4.2
    public Result addGoodsToCartList(Long itemId, Integer num) {

        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105"); //可以访问的域,注解替代
        //response.setHeader("Access-Control-Allow-Origin", "*"); //可以访问的域
        // 对cookie操作时需要加下一句，如果操作cookie，则这个地址必须是指定的，不能是通配符*
        //response.setHeader("Access-Control-Allow-Credentials", "true");

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // 添加购物车到cookie中
        List<Cart> cartList = findCartList();
        // 调用服务方法操作购物车
        cartList = cartService.addGoodsToCartList(cartList, itemId, num);
        try {
            if (name.equals("anonymousUser")) {// 判断是否登录，spring security 未登录默认是anonymousUser
                String cartListString = JSON.toJSONString(cartList);
                // 将新的购物车存入cookie
                CookieUtil.setCookie(request, response, "cartList", cartListString, 3600 * 24, "UTF-8");
                System.out.println("向cookie存储购物车");
            } else {
                // 添加购物车到redis中
                cartService.saveCartListToRedis(name, cartList);
                System.out.println("向redis存储购物车");
            }
            return new Result(true, "存入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "存入购物车失败");
        }
    }

    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // 从cookie中提取购物车
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (StringUtils.isBlank(cartListString)) {
            cartListString = "[]";
        }
        List<Cart> cartListFromCookie = JSON.parseArray(cartListString, Cart.class);
        if (name.equals("anonymousUser")) {// 判断是否登录，spring security 未登录默认是anonymousUser
            // 未登录，存入cookie中
            System.out.println(name + "从cookie中获取");
            return cartListFromCookie;
        } else {
            // 在redis中查找
            List<Cart> cartListFromRedis = cartService.findCartListFromRedis(name);
            if (cartListFromRedis.size() > 0) { //判断当本地购物车中存在数据
                // 得到合并后的购物车
                List<Cart> cartList = cartService.mergeCartList(cartListFromCookie, cartListFromRedis);
                // 将合并后的购物车存入redis
                cartService.saveCartListToRedis(name, cartList);
                // 合并后将cookie中数据清除， 以防下次多次合并
                CookieUtil.deleteCookie(request, response, "cartList");
                System.out.println("执行合并的逻辑");
                return cartList;
            }
            return cartListFromRedis;
        }
    }

    @RequestMapping("/isLogin")
    public Result isLogin() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name.equals("anonymousUser")) {
            return new Result(false,"未登录");
        } else {
            return new Result(true, "已登录");
        }
    }
}
