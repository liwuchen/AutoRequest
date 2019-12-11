package com.example.hello.model;

import java.util.List;

/**
 * @Package: com.example.hello.model
 * @ClassName: OrderInfo
 * @Description: {"status":1000,"msg":"\u6b63\u5e38","data":[{"id":"445382","dateline":"1576046950","order_sn":"201912111449109876","total":"39.11","price":"39.00","atime":"2019-12-11 14:49:10"}]}
 * @Author: liwuchen
 * @CreateDate: 2019/12/11
 */
public class OrderCheck {
    private int status;
    private String msg;
    private List<OrderDetail> data;

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

    public List<OrderDetail> getData() {
        return data;
    }

    public void setData(List<OrderDetail> data) {
        this.data = data;
    }

    class OrderDetail {
        String id;
        String order_sn;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }
    }
}
