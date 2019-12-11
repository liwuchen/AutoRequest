package com.example.hello.model;

/**
 * @Package: com.example.hello.model
 * @ClassName: OrderInfo
 * @Description:
 * @Author: liwuchen
 * @CreateDate: 2019/12/11
 */
public class OrderInfo {
    private int status;
    private String msg;
    private String id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
