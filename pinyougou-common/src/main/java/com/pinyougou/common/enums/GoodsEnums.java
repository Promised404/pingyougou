package com.pinyougou.common.enums;

/**
 * @author 邓鹏涛
 * @date 2019/2/17 16:17
 */
public enum GoodsEnums {

    ENABLE_SPEC("1", "启动规格列表"),
    DISABLE_SPEC("0", "无规格列表"),
    NORMAL_STATUS("1", "正常"),
    LOWER_STATUS("2", "下架"),
    DELETE_STATUS("0", "删除");

    private String status;

    private String title;

    GoodsEnums(String status, String title) {
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
