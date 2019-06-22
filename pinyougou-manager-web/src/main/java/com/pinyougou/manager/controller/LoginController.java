package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录Controller.
 *
 * @author 邓鹏涛
 * @date 2019/2/12 16:19
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    /**
     * 获取登录名
     * @return
     */
    @RequestMapping("/name")
    public Map<String, Object> name() {
        String name = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", name);
        return map;
    }

}
