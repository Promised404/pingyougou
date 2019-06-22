package com.pinyougou.common.enums;

/**
 * 用户状态.
 *
 * @author 邓鹏涛
 * @date 2019/2/16 12:25
 */
public enum UserEnums {

    USER_AUDITPASS("1","审核通过");

    UserEnums(String status, String title) {
        this.status = status;
        this.title = title;
    }

    private String status;

    private String title;

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }
}
