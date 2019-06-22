package entity;

import java.io.Serializable;

/**
 * Json返回结果实体.
 *
 * @author 邓鹏涛
 * @date 2019/2/10 11:32
 */
public class Result implements Serializable {

    private boolean success; //是否成功
    private String message; //返回的信息

    public Result() {
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
