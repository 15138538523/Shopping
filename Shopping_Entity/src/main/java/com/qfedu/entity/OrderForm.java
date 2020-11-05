package com.qfedu.entity;

import lombok.Data;
import java.util.Date;

/**
 * projectName: Shopping
 * author: 崔
 * time: 2020/11/05  11:43
 * description:订单表
 */
@Data
public class OrderForm {
    private Integer id; //主键id
    private Integer aid;//收货地址
    private Integer uid;//卖家地址
    private Integer totalmoney;// 单位分 总金额
    private Integer paymoney;//单位分 付款金额
    private Integer freemoney;//优惠的总金额
    private Integer flag;//订单的状态
    private Date ctime;//创建订单的时间
    private Date utime;//提交订单的时间
}
