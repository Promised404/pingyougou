package entity;

import java.io.Serializable;
import java.util.List;

/**
 *分页结果类.
 *
 * @author 邓鹏涛
 * @date 2019/2/9 20:29
 */
public class PageResult implements Serializable {

    private long total; //总记录数
    private List rows; //当前页记录数

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
