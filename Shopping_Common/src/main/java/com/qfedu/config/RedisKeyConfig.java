package com.qfedu.config;

/**
 * projectName: Shopping
 * author: 崔
 * time: 2020/11/05  21:41
 * description:
 */

public class RedisKeyConfig {
    //下单接口 分布式锁
    public static String ORDER_LOCK="order:create:";//skuid

    //存储订单信息 String类型  -有效期2小时
    public static String ORDER_V2="order:v2:";//订单id  值：订单对象
    //存储订单详情-Hash类型 key field:skuid  value:订单详情对象
    public static String ORDERITEM_V2="order:item:";//订单id
    //订单数据 有效期2小时
    public static int ORDER_TIME=2;
}
