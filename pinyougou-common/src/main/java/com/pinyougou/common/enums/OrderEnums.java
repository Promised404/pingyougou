package com.pinyougou.common.enums;

/**
 * @author 邓鹏涛
 * @date 2019/2/17 16:17
 */
public enum OrderEnums {

    UNPAID("1", "未付款");

    private String status;

    private String title;

    OrderEnums(String status, String title) {
        this.status = status;
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }
}
