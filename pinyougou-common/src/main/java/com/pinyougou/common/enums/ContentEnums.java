package com.pinyougou.common.enums;

/**
 * @author 邓鹏涛
 * @date 2019/2/19 16:23
 */
public enum  ContentEnums {

    ENABLE_CONTENT("1", "启动广告"),
    DISABLE_CONTENT("0", "取消广告");

    private String status;

    private String title;

    ContentEnums(String status, String title) {
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
