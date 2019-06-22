package com.pinyougou.common.enums;

/**
 * 买家用户状态.
 *
 * @author 邓鹏涛
 * @date 2019/3/1 19:58
 */
public enum UsersEnums {

    USER_SOURCE_PC("1","PC端来源"),
    USER_SOURCE_H5("2","H5端来源"),
    USER_SOURCE_ANDROID("3","Android端来源"),
    USER_SOURCE_IOS("4","IOS端来源"),
    USER_SOURCE_WECHAT("5","wechat端来源");

    UsersEnums(String type, String title) {
        this.type = type;
        this.title = title;
    }

    private String type;

    private String title;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
