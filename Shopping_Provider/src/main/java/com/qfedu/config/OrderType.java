package com.qfedu.config;

public enum OrderType {
    待支付(1),待发货(2),待签收(3),待确认(4),待评价(5),已评价(6),取消订单(7),超时订单(8);
    private int val;
    private OrderType(int v){
        val=v;
    }

    public int getVal() {
        return val;
    }

}
